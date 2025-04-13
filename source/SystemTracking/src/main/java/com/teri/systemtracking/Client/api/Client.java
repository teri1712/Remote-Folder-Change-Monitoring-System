package com.teri.systemtracking.Client.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Client {
   private DataInputStream in;
   private DataOutputStream out;
   private Socket socket;
   private int fileTrackingSession;

   public Client(Socket socket) throws IOException {
      this.socket = socket;
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());

      fireHandleThread();
   }

   public void fireHandleThread() {
      new Thread(new Runnable() {

         @Override
         public void run() {
            Iterable<Path> list_drive = FileSystems.getDefault().getRootDirectories();
            ArrayList<byte[]> drives = new ArrayList<>();
            for (Path i : list_drive) {
               drives.add(i.toFile().getAbsolutePath().getBytes(StandardCharsets.UTF_8));
            }
            synchronized (out) {
               try {
                  out.writeInt(drives.size());

                  for (byte[] i : drives) {
                     out.writeInt(i.length);
                     out.write(i, 0, i.length);
                  }
               } catch (IOException e) {
                  return;
               }
            }

            while (true) {
               int len;
               try {
                  len = in.readInt();

                  byte[] cmd = new byte[len];

                  in.read(cmd, 0, len);

                  String command = new String(cmd, StandardCharsets.UTF_8);
                  if (command.equals("track folder")) {
                     int callBackNumber = in.readInt();
                     int fileNameLen = in.readInt();

                     byte[] f = new byte[fileNameLen];

                     in.read(f, 0, fileNameLen);

                     String filePath = new String(f, StandardCharsets.UTF_8);

                     int ac = -1;
                     WatchService watch = null;
                     final HashMap<WatchKey, String> trackRepo = new HashMap<>();
                     try {

                        Path path = Paths.get(filePath);

                        final WatchService watchService = FileSystems.getDefault().newWatchService();
                        watch = watchService;
                        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                           @Override
                           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                                 throws IOException {
                              trackRepo.put(dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                                    StandardWatchEventKinds.ENTRY_DELETE,
                                    StandardWatchEventKinds.ENTRY_MODIFY), dir.toFile().getAbsolutePath());
                              return FileVisitResult.CONTINUE;
                           }
                        });
                        ac = 1;
                     } catch (Exception e) {
                        // TODO: handle exception
                     }

                     synchronized (out) {
                        out.writeInt(cmd.length);
                        out.write(cmd, 0, cmd.length);
                        out.writeInt(ac);
                        out.writeInt(callBackNumber);
                        out.flush();
                     }
                     if (ac == 1) {
                        fileTrackingSession = in.readInt();

                        final WatchService w = watch;

                        new Thread(new Runnable() {
                           int thisThreadSession = fileTrackingSession;

                           @Override
                           public void run() {
                              WatchKey key;
                              try {
                                 while (!trackRepo.isEmpty() && thisThreadSession == fileTrackingSession
                                       && (key = w.take()) != null) {
                                    String dir = trackRepo.get(key);
                                    for (WatchEvent<?> event : key.pollEvents()) {
                                       byte[] cmd = new String("event").getBytes(StandardCharsets.UTF_8);
                                       String typeOfEvent = event.kind().name();
                                       byte[] effect = typeOfEvent.getBytes(StandardCharsets.UTF_8);

                                       File file = new File(
                                             dir + "\\" + ((WatchEvent<Path>) event).context().toFile().getName());
                                       System.out.println(file.getAbsolutePath());

                                       byte[] path = new String(file.getAbsolutePath())
                                             .getBytes(StandardCharsets.UTF_8);

                                       synchronized (out) {
                                          try {
                                             out.writeInt(cmd.length);
                                             out.write(cmd, 0, cmd.length);
                                             out.writeInt(thisThreadSession);
                                             out.writeInt(path.length);
                                             out.write(path, 0, path.length);
                                             out.writeInt(effect.length);
                                             out.write(effect, 0, effect.length);
                                             out.flush();

                                          } catch (IOException e) {
                                             return;
                                          }
                                       }
                                       if (typeOfEvent.equals("ENTRY_CREATE") && file.isDirectory()) {
                                          try {
                                             trackRepo.put(Paths.get(file.getAbsolutePath()).register(w,
                                                   StandardWatchEventKinds.ENTRY_CREATE,
                                                   StandardWatchEventKinds.ENTRY_DELETE,
                                                   StandardWatchEventKinds.ENTRY_MODIFY), file.getAbsolutePath());
                                          } catch (IOException e) {

                                          }
                                       }
                                    }
                                    if (!key.reset()) {
                                       trackRepo.remove(key);
                                    }

                                 }
                              } catch (Exception e) {
                                 System.out.println(e);
                              }
                           }
                        }).start();
                     }
                  } else if (command.equals("folder list")) {
                     int callBackNumber = in.readInt();

                     int pathLeng = in.readInt();

                     byte[] p = new byte[pathLeng];

                     in.read(p, 0, pathLeng);

                     String path = new String(p, StandardCharsets.UTF_8);

                     File dir = new File(path);
                     ArrayList<String> l = null;
                     if (dir.exists()) {
                        File[] listFile = dir.listFiles();
                        if (listFile != null) {
                           l = new ArrayList<>();
                           for (File i : dir.listFiles()) {
                              if (i.isDirectory()) {
                                 l.add(i.getAbsolutePath());
                              }
                           }
                        }
                     }
                     synchronized (out) {
                        out.writeInt(cmd.length);
                        out.write(cmd, 0, cmd.length);
                        out.writeInt(callBackNumber);
                        if (l == null) {
                           out.writeInt(-1);
                        } else {
                           out.writeInt(l.size());

                           for (String i : l) {
                              byte[] ii = i.getBytes(StandardCharsets.UTF_8);
                              out.writeInt(ii.length);
                              out.write(ii, 0, ii.length);
                           }
                        }
                        out.flush();
                     }
                  } else if (command.equals("search by path recommendation")) {

                     int callBackNumber = in.readInt();

                     int flen = in.readInt();

                     byte[] f = new byte[flen];

                     in.read(f, 0, flen);

                     String path = new String(f, StandardCharsets.UTF_8);
                     System.out.println(path);
                     int lastSeperater = -1;
                     for (int i = path.length() - 1; i >= 0; i--) {
                        if (path.charAt(i) == '\\') {
                           lastSeperater = i;
                           break;
                        }
                     }

                     synchronized (out) {
                        out.writeInt(cmd.length);
                        out.write(cmd, 0, cmd.length);
                        out.writeInt(callBackNumber);

                        if (lastSeperater == -1) {
                           out.writeInt(0);
                        } else {
                           String p = path.substring(0, lastSeperater + 1);
                           String s = "";
                           if (lastSeperater != path.length() - 1) {
                              s = path.substring(lastSeperater + 1, path.length());
                           }
                           System.out.println(p);
                           System.out.println(s);
                           File par = new File(p);
                           if (par.exists() && par.isDirectory()) {
                              ArrayList<String> l = new ArrayList<>();
                              for (File i : par.listFiles()) {
                                 if (i.getName().startsWith(s) && !i.isHidden()) {
                                    l.add(i.getAbsolutePath());
                                 }
                              }
                              out.writeInt(l.size());
                              for (String i : l) {
                                 byte[] b = i.getBytes(StandardCharsets.UTF_8);
                                 out.writeInt(b.length);
                                 out.write(b, 0, b.length);
                              }
                           } else {
                              out.writeInt(0);
                           }
                        }
                        out.flush();
                     }

                  } else if (command.equals("disable")) {
                     fileTrackingSession++;

                     synchronized (out) {
                        out.writeInt(cmd.length);
                        out.write(cmd, 0, cmd.length);
                        out.writeInt(1);
                        out.flush();
                     }

                  } else if (command.equals("disconnect")) {
                     socket.close();
                     java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                           JOptionPane.showMessageDialog(null, "disconnected");
                        }
                     });
                     return;
                  }
               } catch (IOException e) {
                  java.awt.EventQueue.invokeLater(new Runnable() {
                     public void run() {
                        JOptionPane.showMessageDialog(null, "disconnected");
                     }
                  });
                  return;
               }

            }

         }

      }).start();

   }
}
