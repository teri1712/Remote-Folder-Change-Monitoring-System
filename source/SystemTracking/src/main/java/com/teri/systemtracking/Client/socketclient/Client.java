package com.teri.systemtracking.Client.socketclient;

import com.teri.systemtracking.Client.watch.WatchRunner;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.Packet;
import com.teri.systemtracking.common.model.Folder;
import com.teri.systemtracking.common.utils.SerializeUtils;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements PacketSender {
      private volatile String address;
      private volatile int port;
      private volatile DataInputStream inputStream;
      private volatile DataOutputStream outputStream;
      private volatile Socket socket;
      private ExecutorService executor = Executors.newSingleThreadExecutor();
      private EventStream currentTrack;

      public Client() {
      }

      private WatchRunner handleTrack(String filePath) throws IOException {
            int ac = -1;
            WatchService watch = null;
            final HashMap<WatchKey, String> trackTree = new HashMap<>();
            try {

                  Path path = Paths.get(filePath);

                  final WatchService watchService = FileSystems.getDefault().newWatchService();
                  watch = watchService;
                  Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                              throws IOException {
                              trackTree.put(dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                                    StandardWatchEventKinds.ENTRY_DELETE,
                                    StandardWatchEventKinds.ENTRY_MODIFY), dir.toFile().getAbsolutePath());
                              return FileVisitResult.CONTINUE;
                        }
                  });
                  ac = 1;
            } catch (Exception e) {
                  // TODO: handle exception
            }

            if (ac == 1) {
                  currentTrack = new EventStream(this);
                  return new WatchRunner(watch, trackTree, currentTrack);
            }
            return null;
      }

      private Folder handleList(String path) throws IOException {
            File dir = new File(path);
            if (!dir.isDirectory()) {
                  return null;
            }
            List<com.teri.systemtracking.common.model.File> files = new ArrayList<>();
            if (dir.exists()) {
                  File[] listFile = dir.listFiles();
                  if (listFile != null) {
                        for (File file : listFile) {
                              if (file.isDirectory()) {
                                    files.add(new Folder(file.getAbsolutePath(), file.getName(), Collections.emptyList()));
                              }
                        }
                  }
            }
            return new Folder(dir.getAbsolutePath(), dir.getName(), files);
      }

      private List<Folder> handleSearch(String path) throws IOException {
            int splash = path.lastIndexOf("/");
            if (splash == -1)
                  splash = path.lastIndexOf("\\");
            File parent = new File(path.substring(0, splash + 1));
            if (splash == path.length() - 1) {
                  return Collections.singletonList(new Folder(parent.getAbsolutePath(), parent.getName(), Collections.emptyList()));
            }
            String prefixPattern = path.substring(splash + 1);
            List<Folder> folders = new ArrayList<>();

            File[] listFile = parent.listFiles();
            if (listFile != null) {
                  for (File file : listFile) {
                        if (file.isDirectory() && file.getName().startsWith(prefixPattern)) {
                              folders.add(new Folder(file.getAbsolutePath(), file.getName(), Collections.emptyList()));
                        }
                  }
            }
            return folders;
      }


      public void start(String address, int port) throws IOException {
            if (socket != null) {
                  return;
            }
            this.address = address;
            this.port = port;
            Socket socket = new Socket(address, port);
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.socket = socket;
            new Thread(new Runnable() {

                  @Override
                  public void run() {
                        try {
                              Client.this.run();
                        } catch (IOException e) {
                              java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                          JOptionPane.showMessageDialog(null, "Server disconnected");
                                    }
                              });
                              return;
                        }
                  }
            }).start();
      }

      private void run() throws IOException {
            while (socket == null) {
                  try {
                        Thread.sleep(200);
                  } catch (InterruptedException e) {
                        e.printStackTrace();
                  }
            }
            Iterable<Path> drives = FileSystems.getDefault().getRootDirectories();
            List<Folder> initialDrives = new ArrayList<>();
            for (Path path : drives) {
                  Folder folder = new Folder(path.toFile().getAbsolutePath(), path.toFile().getName(), Collections.emptyList());
                  initialDrives.add(folder);
            }

            Packet packet = new Packet(Packet.INITIAL_REQUEST_PACKET, SerializeUtils.serialize(initialDrives));
            byte[] packetBytes = SerializeUtils.serialize(packet);
            outputStream.writeInt(packetBytes.length);
            outputStream.write(packetBytes);
            while (true) {
                  int packetLength;
                  packetLength = inputStream.readInt();

                  packetBytes = new byte[packetLength];
                  inputStream.read(packetBytes, 0, packetLength);
                  packet = SerializeUtils.deserialize(packetBytes);
                  byte[] packetPayloadResponseBytes;
                  Message message = SerializeUtils.deserialize(packet.getPayload());
                  String command = message.getCommand();
                  System.out.println("Client received: " + command);
                  switch (command) {
                        case "track": {
                              String path = (String) message.getContent();
                              if (currentTrack == null) {
                                    WatchRunner watchRunner = handleTrack(path);
                                    message = new Message("track", watchRunner != null ? 1 : -1);
                                    packetPayloadResponseBytes = SerializeUtils.serialize(message);
                                    Packet response = new Packet(packet.getId(), packetPayloadResponseBytes);
                                    send(response);
                                    if (watchRunner != null) {
                                          executor.submit(watchRunner);
                                    }
                              } else {
                                    message = new Message("track", -1);
                                    packetPayloadResponseBytes = SerializeUtils.serialize(message);
                                    Packet response = new Packet(packet.getId(), packetPayloadResponseBytes);
                                    send(response);
                              }
                        }
                        break;
                        case "untrack": {
                              String path = (String) message.getContent();
                              if (currentTrack != null) {
                                    currentTrack.stop();
                                    currentTrack = null;
                                    message = new Message("untrack", 1);
                                    packetPayloadResponseBytes = SerializeUtils.serialize(message);
                                    Packet response = new Packet(packet.getId(), packetPayloadResponseBytes);
                                    send(response);
                              }
                        }
                        break;
                        case "list": {

                              String path = (String) message.getContent();
                              Folder folder = handleList(path);
                              if (folder != null) {
                                    message = new Message("list", folder);
                                    packetPayloadResponseBytes = SerializeUtils.serialize(message);
                                    Packet response = new Packet(packet.getId(), packetPayloadResponseBytes);
                                    send(response);
                              }
                        }
                        break;
                        case "search": {
                              String path = (String) message.getContent();
                              List<Folder> folders = handleSearch(path);
                              message = new Message("search", folders);
                              packetPayloadResponseBytes = SerializeUtils.serialize(message);
                              Packet response = new Packet(packet.getId(), packetPayloadResponseBytes);
                              send(response);
                        }
                        break;
                        default:
                              throw new IllegalArgumentException("unknown command: " + command);
                  }

            }

      }

      @Override
      public synchronized void send(Packet packet) throws IOException, IllegalStateException {
            byte[] packetBytes = SerializeUtils.serialize(packet);
            outputStream.writeInt(packetBytes.length);
            outputStream.write(packetBytes);
            outputStream.flush();
      }
}
