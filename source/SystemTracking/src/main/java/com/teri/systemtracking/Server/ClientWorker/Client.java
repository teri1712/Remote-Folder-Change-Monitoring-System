package com.teri.systemtracking.Server.ClientWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.teri.systemtracking.Server.GUI.ActiveUserEntityPanel;
import com.teri.systemtracking.Server.GUI.DriveEntityPanel;
import com.teri.systemtracking.Server.GUI.EditUserSessionPanel;
import com.teri.systemtracking.Server.GUI.FileEntityPanel;
import com.teri.systemtracking.Server.GUI.FileSelectionPanel;
import com.teri.systemtracking.Server.GUI.ObservedActionEntityPanel;
import com.teri.systemtracking.Server.GUI.ObservedActionListPanel;
import com.teri.systemtracking.Server.GUI.SearchEntityPanel;
import com.teri.systemtracking.Server.Server.CentralWorker;

public class Client {

   /* GUI components */
   private ActiveUserEntityPanel activeUserEntityPanel;
   private EditUserSessionPanel editUserSessionPanel;
   private FileSelectionPanel fileSelectionPanel;
   private ObservedActionListPanel observedActionListPanel;

   /* Callback storage */
   private HashMap<Integer, Object> callBackStorage;
   private Integer callBackSessionNumber;

   /*
    * real-time checksum for callback
    * example : when user request folder1's list, before response1 user request
    * folder2's list
    * it makes sense not to show folder1, but rather than waiting for folder2's
    * list
    */
   private Integer viewFileListSessionNumber;

   /**/
   private Integer fileTrackingSessionToken;

   private Integer searchByPathRecommedationSessionNumber;
   private String selectedPath;
   private FileEntityPanel selectedEntityPanel;
   private DriveEntityPanel selectedDrivePanel;

   private TreeMap<Integer, String> folderEnteredRepo;
   private HashMap<String, ArrayList<JPanel>> loadedFolderListRepo;
   private HashMap<String, ArrayList<JPanel>> observedRepo;

   // either in tracked or untracked

   private Date createdDate;
   private String state;
   private Thread callStackChannel;
   private BlockingQueue<Runnable> callStack;
   private Thread messageListener;

   private DataInputStream inBoundStream;
   private DataOutputStream outBoundStream;

   private Socket socket;
   private CentralWorker centralHandler;
   private int id;

   public Client(int id, Socket socket, CentralWorker centralHandler, Date createdDate) throws IOException {
      /* init */
      this.id = id;
      this.createdDate = createdDate;
      this.socket = socket;
      this.centralHandler = centralHandler;
      callBackSessionNumber = 0;
      viewFileListSessionNumber = 0;
      fileTrackingSessionToken = 0;
      searchByPathRecommedationSessionNumber = 0;
      loadedFolderListRepo = new HashMap<>();
      callBackStorage = new HashMap<>();
      folderEnteredRepo = new TreeMap<>();
      observedRepo = new HashMap<>();
      state = "uninitiated";

      try {
         outBoundStream = new DataOutputStream(socket.getOutputStream());
         inBoundStream = new DataInputStream(socket.getInputStream());
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      editUserSessionPanel = new EditUserSessionPanel();
      activeUserEntityPanel = new ActiveUserEntityPanel();
      fileSelectionPanel = editUserSessionPanel.getFileSelectonPanel();
      observedActionListPanel = editUserSessionPanel.getObservedListPanel();

      /* set triggered events */
      fileSelectionPanel.getTrackButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               trackSelectedFile();
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      fileSelectionPanel.getBackButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               previousViewList();
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      fileSelectionPanel.getSearchBar().getTextField().getDocument().addDocumentListener(new DocumentListener() {

         @Override
         public void insertUpdate(DocumentEvent e) {
            String path = fileSelectionPanel.getSearchBar().getTextField().getText();
            if (path.equals("search by path")) {
               fileSelectionPanel.getSearchBar().setRcmList(new ArrayList<JPanel>());
               return;
            }
            if (path.isEmpty()) {
               fileSelectionPanel.getSearchBar().setRcmList(new ArrayList<JPanel>());
               return;
            }
            try {
               searchByPath(path);
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }

         @Override
         public void removeUpdate(DocumentEvent e) {

            String path = fileSelectionPanel.getSearchBar().getTextField().getText();
            if (path.isEmpty()) {
               fileSelectionPanel.getSearchBar().setRcmList(new ArrayList<JPanel>());
               return;
            }
            try {
               searchByPath(path);
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }

         }

         @Override
         public void changedUpdate(DocumentEvent e) {

         }

      });
      fileSelectionPanel.getSearchBar().addHandlerActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            fileSelectionPanel.requestFocusInWindow();
            try {
               getListFile(fileSelectionPanel.getSearchBar().getTextField().getText());
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }

      });
      observedActionListPanel.getDisableTrackingButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               disableTrackingFile();
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      observedActionListPanel.getSearchBar().addHandlerActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               searchEventByDate();
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      observedActionListPanel.getShowAllButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               showAllEvent();
            } catch (InterruptedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }

      });

      /* set-up client's session gui properties */

      activeUserEntityPanel.setEntityProperty(createdDate, id);
      activeUserEntityPanel.setEnityCommands(this);
      editUserSessionPanel.setEntityProperty(id);

      /* fetch drives after initiate connection */

      final ArrayList<String> drives_list = new ArrayList<>();
      synchronized (inBoundStream) {
         int lenList = inBoundStream.readInt();

         for (int i = 0; i < lenList; i++) {
            int len = inBoundStream.readInt();
            byte[] d = new byte[len];
            inBoundStream.read(d, 0, len);
            drives_list.add(new String(d, StandardCharsets.UTF_8));
         }
      }

      for (String i : drives_list) {
         fileSelectionPanel.addNewDrive(i, Client.this);
      }

      /* call stack channel */
      callStack = new ArrayBlockingQueue<Runnable>(100, true);
      callStackChannel = new Thread(new Runnable() {
         @Override
         public void run() {
            while (!Client.this.socket.isClosed()) {
               try {
                  Runnable take = callStack.poll(1, TimeUnit.SECONDS);
                  if (take != null)
                     take.run();
               } catch (InterruptedException e) {
                  return;
               }
            }

         }
      });
      callStackChannel.start();
      /* promise channel */
      messageListener = new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  int cmdLength = inBoundStream.readInt();
                  byte[] cmd = new byte[cmdLength];
                  inBoundStream.read(cmd, 0, cmdLength);

                  String command = new String(cmd, StandardCharsets.UTF_8);
                  if (command.equals("folder list")) {
                     int callBackId = inBoundStream.readInt();

                     int cs = -1;
                     String dir;
                     synchronized (callBackStorage) {
                        ArrayList<Object> l = (ArrayList<Object>) callBackStorage.get(callBackId);
                        cs = (Integer) l.get(0);
                        dir = (String) l.get(1);
                        callBackStorage.remove(callBackId);

                     }
                     final int token = cs;
                     final String directory = dir;
                     int listFileLength = inBoundStream.readInt();
                     if (listFileLength == -1) {
                        try {
                           callStack.put(new Runnable() {
                              @Override
                              public void run() {
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       JOptionPane.showMessageDialog(null, "can't access this folder");
                                    }
                                 });
                              }
                           });
                        } catch (InterruptedException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        return;
                     }

                     final ArrayList<JPanel> l = new ArrayList<>();

                     for (int i = 0; i < listFileLength; i++) {
                        int fileNameLength = inBoundStream.readInt();
                        byte[] buffer = new byte[fileNameLength];

                        int cnt = 0;
                        while (cnt != fileNameLength) {
                           int bread = inBoundStream.read(buffer, 0, Math.min(4096, fileNameLength - cnt));
                           cnt += bread;
                        }
                        String fileName = new String(buffer, StandardCharsets.UTF_8);

                        File f = new File(fileName);

                        FileEntityPanel e = new FileEntityPanel();
                        e.setProperty(f.getAbsolutePath(), f.getName(), Client.this);
                        l.add(e);
                     }
                     try {
                        callStack.put(new Runnable() {
                           @Override
                           public void run() {
                              loadedFolderListRepo.put(directory, l);
                              if (token == viewFileListSessionNumber) {
                                 folderEnteredRepo.put(viewFileListSessionNumber, directory);
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       fileSelectionPanel.getListFilePanel().removeAll();
                                       for (JPanel e : l)
                                          fileSelectionPanel.getListFilePanel().add(e);
                                       fileSelectionPanel.getListFilePanel().repaint();
                                       fileSelectionPanel.getListFilePanel().revalidate();
                                       fileSelectionPanel.setEnteredFolder(directory);

                                    }
                                 });
                              }
                           }
                        });
                     } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }

                  } else if (command.equals("event")) {
                     final int token = inBoundStream.readInt();

                     final Date date = new Date(System.currentTimeMillis());
                     int length = inBoundStream.readInt();
                     byte[] buffer = new byte[length];
                     inBoundStream.read(buffer, 0, length);

                     String path = new String(buffer, StandardCharsets.UTF_8);

                     length = inBoundStream.readInt();
                     buffer = new byte[length];
                     inBoundStream.read(buffer, 0, length);

                     String effect = new String(buffer, StandardCharsets.UTF_8);

                     final ObservedActionEntityPanel e = new ObservedActionEntityPanel();

                     e.setProperty(path, effect, date, Client.this);

                     try {
                        callStack.put(new Runnable() {
                           @Override
                           public void run() {
                              if (token == fileTrackingSessionToken) {
                                 if (!observedRepo.containsKey(date.toString())) {
                                    observedRepo.put(date.toString(), new ArrayList<JPanel>());
                                 }
                                 observedRepo.get(date.toString()).add(e);
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       observedActionListPanel.addEvent(e);

                                    }
                                 });
                              }
                           }
                        });
                     } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                     }

                  } else if (command.equals("track folder")) {
                     final int ac = inBoundStream.readInt();
                     int callBackId = inBoundStream.readInt();
                     String f;
                     synchronized (callBackStorage) {
                        f = (String) callBackStorage.get(callBackId);
                        callBackStorage.remove(callBackId);

                     }
                     final String file = f;

                     try {
                        callStack.put(new Runnable() {
                           @Override
                           public void run() {
                              if (ac == -1) {
                                 state = "uninitiated";
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       JOptionPane.showMessageDialog(null, "tracking denied");
                                    }
                                 });
                                 return;
                              }
                              state = "initiated";
                              fileTrackingSessionToken++;
                              viewFileListSessionNumber++;
                              searchByPathRecommedationSessionNumber++;
                              try {
                                 outBoundStream.writeInt(fileTrackingSessionToken);
                                 outBoundStream.flush();
                              } catch (IOException e) {
                                 e.printStackTrace();
                              }
                              final DriveEntityPanel selected = selectedDrivePanel;
                              selectedDrivePanel = null;

                              java.awt.EventQueue.invokeLater(new Runnable() {
                                 public void run() {
                                    activeUserEntityPanel.setTrackingFile(file);
                                    if (selected != null)
                                       selected.setIsActive(false);
                                    fileSelectionPanel.getListFilePanel().removeAll();
                                    fileSelectionPanel.getListFilePanel().repaint();
                                    fileSelectionPanel.getListFilePanel().revalidate();
                                    editUserSessionPanel.setShowPanel("observed events");
                                 }
                              });
                              folderEnteredRepo.clear();
                              loadedFolderListRepo.clear();
                              selectedDrivePanel = null;
                              selectedEntityPanel = null;
                              selectedPath = null;
                           }
                        });
                     } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }

                  } else if (command.equals("search by path recommendation")) {
                     int callBackId = inBoundStream.readInt();

                     int cs = -1;
                     synchronized (callBackStorage) {
                        cs = (Integer) callBackStorage.get(callBackId);
                        callBackStorage.remove(callBackId);

                     }
                     final int token = cs;
                     int listLength = inBoundStream.readInt();
                     final ArrayList<JPanel> l = new ArrayList<>();

                     for (int i = 0; i < listLength; i++) {
                        int fileNameLength = inBoundStream.readInt();
                        byte[] buffer = new byte[fileNameLength];
                        inBoundStream.read(buffer, 0, fileNameLength);
                        final String fileName = new String(buffer, StandardCharsets.UTF_8);
                        SearchEntityPanel e = new SearchEntityPanel();
                        e.addMouseListener(new MouseAdapter() {
                           @Override
                           public void mouseClicked(MouseEvent e) {
                              try {
                                 getListFile(fileName);
                              } catch (InterruptedException e1) {
                                 // TODO Auto-generated catch block
                                 e1.printStackTrace();
                              }
                              fileSelectionPanel.requestFocusInWindow();

                           }
                        });
                        e.setContent(fileName);
                        l.add(e);
                     }
                     try {
                        callStack.put(new Runnable() {
                           @Override
                           public void run() {
                              if (token == searchByPathRecommedationSessionNumber) {
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       fileSelectionPanel.getSearchBar().setRcmList(l);
                                    }
                                 });
                              }
                           }
                        });
                     } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  } else if (command.equals("disable")) {
                     final int ac = inBoundStream.readInt();
                     try {
                        callStack.put(new Runnable() {
                           @Override
                           public void run() {
                              if (ac == -1) {
                                 state = "initiated";
                                 java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                       JOptionPane.showMessageDialog(null, "error occurs");
                                    }
                                 });
                                 return;
                              }
                              fileTrackingSessionToken++;
                              state = "uninitiated";
                              observedRepo.clear();
                              java.awt.EventQueue.invokeLater(new Runnable() {
                                 public void run() {
                                    observedActionListPanel.clearEvents();
                                    editUserSessionPanel.setShowPanel("file selection");
                                 }
                              });
                           }
                        });
                     } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  }

               } catch (IOException e) {
                  try {
                     Client.this.disconnectHandler();
                  } catch (InterruptedException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                  }
                  return;
               }
            }

         }
      });
      messageListener.start();

   }

   public void searchByPath(final String path) throws InterruptedException {
      callStack.put(new Runnable() {

         @Override
         public void run() {
            final Integer cs = ++searchByPathRecommedationSessionNumber;

            new Thread(new Runnable() {
               @Override
               public void run() {
                  try {
                     Thread.sleep(200);
                     if (searchByPathRecommedationSessionNumber != cs)
                        return;

                     callStack.put(new Runnable() {

                        @Override
                        public void run() {
                           if (searchByPathRecommedationSessionNumber != cs) {
                              return;
                           }
                           callBackSessionNumber++;
                           synchronized (callBackStorage) {
                              callBackStorage.put(callBackSessionNumber, cs);
                           }

                           String command = "search by path recommendation";
                           byte[] cmd = command.getBytes(StandardCharsets.UTF_8);
                           try {
                              outBoundStream.writeInt(cmd.length);
                              outBoundStream.write(cmd, 0, cmd.length);

                              outBoundStream.writeInt(callBackSessionNumber);

                              byte[] fbytes = path.getBytes(StandardCharsets.UTF_8);

                              outBoundStream.writeInt(fbytes.length);
                              outBoundStream.write(fbytes, 0, fbytes.length);
                              outBoundStream.flush();
                           } catch (IOException e) {
                              e.printStackTrace();
                           }
                        }

                     });
                  } catch (InterruptedException e) {
                  }
               }
            }).start();
         }

      });

   }

   public void setSelectedPath(final String path, final FileEntityPanel fileEntityPanel) throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (selectedEntityPanel != null) {
               final FileEntityPanel deselected = selectedEntityPanel;
               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     deselected.setSelected(false);
                  }
               });
            }
            selectedPath = path;
            selectedEntityPanel = fileEntityPanel;
            final FileEntityPanel selected = selectedEntityPanel;
            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  selected.setSelected(true);
               }
            });
         }

      });
   }

   public void setSelectedDrive(final String path, final DriveEntityPanel drive) throws InterruptedException {

      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (!state.equals("uninitiated"))
               return;

            if (selectedDrivePanel != null) {
               final DriveEntityPanel deselected = selectedDrivePanel;

               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     deselected.setIsActive(false);
                  }
               });

            }
            selectedDrivePanel = drive;
            final DriveEntityPanel selected = selectedDrivePanel;

            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  selected.setIsActive(true);
               }
            });

            try {
               getListFile(path);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      });

   }

   /* call stack api */

   public void trackSelectedFile() throws InterruptedException {

      callStack.put(new Runnable() {
         @Override
         public void run() {

            if (!state.equals("uninitiated"))
               return;

            if (selectedPath == null) {
               JOptionPane.showMessageDialog(null, "please select a file");
               return;
            }
            state = "initiating";

            callBackSessionNumber++;
            synchronized (callBackStorage) {
               callBackStorage.put(callBackSessionNumber, selectedPath);
            }

            String command = "track folder";
            byte[] cmd = command.getBytes(StandardCharsets.UTF_8);
            try {
               outBoundStream.writeInt(cmd.length);
               outBoundStream.write(cmd, 0, cmd.length);

               outBoundStream.writeInt(callBackSessionNumber);

               byte[] fbytes = selectedPath.getBytes(StandardCharsets.UTF_8);

               outBoundStream.writeInt(fbytes.length);
               outBoundStream.write(fbytes, 0, fbytes.length);

               outBoundStream.flush();

            } catch (IOException e) {
               e.printStackTrace();
            }

         }
      });

   }

   public void previousViewList() throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (!state.equals("uninitiated")) {
               return;
            }

            Integer floor = folderEnteredRepo.floorKey(viewFileListSessionNumber - 1);

            if (floor != null) {
               final String pre = folderEnteredRepo.get(floor);
               final ArrayList<JPanel> take = loadedFolderListRepo.get(pre);

               viewFileListSessionNumber--;

               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     fileSelectionPanel.setEnteredFolder(pre);
                     fileSelectionPanel.getListFilePanel().removeAll();
                     for (JPanel i : take) {
                        fileSelectionPanel.getListFilePanel().add(i);
                     }

                     fileSelectionPanel.getListFilePanel().repaint();
                     fileSelectionPanel.getListFilePanel().revalidate();
                  }
               });
            }
         }
      });
   };

   public void getListFile(final String directoryPath) throws InterruptedException {

      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (!state.equals("uninitiated")) {
               return;
            }

            ++viewFileListSessionNumber;

            if (loadedFolderListRepo.containsKey(directoryPath)) {
               final ArrayList<JPanel> take = loadedFolderListRepo.get(directoryPath);

               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     fileSelectionPanel.setEnteredFolder(directoryPath);
                     fileSelectionPanel.getListFilePanel().removeAll();
                     for (JPanel i : take) {
                        fileSelectionPanel.getListFilePanel().add(i);
                        fileSelectionPanel.getListFilePanel().repaint();
                        fileSelectionPanel.getListFilePanel().revalidate();
                     }
                  }
               });
               return;
            }
            int token = viewFileListSessionNumber;
            byte[] body = directoryPath.getBytes(StandardCharsets.UTF_8);
            String command = "folder list";
            byte[] cmd = command.getBytes(StandardCharsets.UTF_8);

            callBackSessionNumber++;
            synchronized (callBackStorage) {
               ArrayList<Object> l = new ArrayList<>();
               l.add(token);
               l.add(directoryPath);
               callBackStorage.put(callBackSessionNumber, l);
            }
            try {
               outBoundStream.writeInt(cmd.length);
               outBoundStream.write(cmd, 0, cmd.length);

               outBoundStream.writeInt(callBackSessionNumber);

               outBoundStream.writeInt(body.length);
               outBoundStream.write(body, 0, body.length);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      });
   }

   public void disableTrackingFile() throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (state.equals("disabling")) {
               return;
            }
            state = "disabling";

            String command = "disable";
            byte[] cmd = command.getBytes(StandardCharsets.UTF_8);
            try {
               outBoundStream.writeInt(cmd.length);
               outBoundStream.write(cmd, 0, cmd.length);

               outBoundStream.flush();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      });

   }

   public void searchEventByDate() throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {
            try {
               Date date = Date.valueOf(observedActionListPanel.getSearchBar().getTextField().getText());

               final ArrayList<JPanel> l = observedRepo.get(date.toString());

               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     observedActionListPanel.setListEvent(l);
                  }
               });

            } catch (Exception ex) {
               java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                     JOptionPane.showMessageDialog(null, "invalid date");
                  }
               });
            }
         }
      });
   }

   public void showAllEvent() throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {

            final ArrayList<JPanel> l = new ArrayList<>();
            for (Map.Entry<String, ArrayList<JPanel>> entry : observedRepo.entrySet()) {
               for (JPanel i : entry.getValue())
                  l.add(i);
            }

            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  observedActionListPanel.setListEvent(l);
               }
            });
         }
      });
   }

   public void disconnectHandler() throws InterruptedException {
      callStack.put(new Runnable() {
         @Override
         public void run() {
            if (state == "disconnected")
               return;
            String command = "disconnect";
            byte[] cmd = command.getBytes(StandardCharsets.UTF_8);
            try {
               outBoundStream.writeInt(cmd.length);
               outBoundStream.write(cmd, 0, cmd.length);

               outBoundStream.flush();

               socket.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
            state = "disconnected";
            try {
               centralHandler.removeConnection(Client.this);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      });
   }

   public void showClientSession() {
      try {
         centralHandler.showClientSession(editUserSessionPanel, this);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /* getters */
   public ActiveUserEntityPanel getActiveUserEntityPanel() {
      return activeUserEntityPanel;
   }

   public int getId() {
      return id;
   }

   public Date getCreatedDate() {
      return createdDate;
   }
}
