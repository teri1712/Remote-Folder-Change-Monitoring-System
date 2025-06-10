package com.teri.systemtracking.Client.watch;

import com.teri.systemtracking.Client.socketclient.EventStream;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.Packet;
import com.teri.systemtracking.common.model.Event;
import com.teri.systemtracking.common.utils.SerializeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.HashMap;

import static com.teri.systemtracking.common.Packet.EVENT_PACKET;

public class WatchRunner implements Runnable {

      private final WatchService watchService;
      private final HashMap<WatchKey, String> trackTree;
      private final EventStream eventStream;

      public WatchRunner(
            WatchService watchService,
            HashMap<WatchKey, String> trackTree,
            EventStream eventStream) {
            this.trackTree = trackTree;
            this.watchService = watchService;
            this.eventStream = eventStream;
      }

      @Override
      public void run() {
            WatchKey key;
            try {
                  while (!trackTree.isEmpty() && (key = watchService.take()) != null) {
                        String dir = trackTree.get(key);
                        for (WatchEvent<?> watchEvent : key.pollEvents()) {
                              String action = watchEvent.kind().name();
                              File file = new File(dir + "\\" + ((WatchEvent<Path>) watchEvent).context().toFile().getName());

                              Event event = new Event(file.getAbsolutePath(), action, new Date());
                              byte[] payload = SerializeUtils.serialize(new Message("event", event));
                              Packet packet = new Packet(EVENT_PACKET, payload);
                              eventStream.send(packet);
                              if (action.equals("ENTRY_CREATE") && file.isDirectory()) {
                                    try {
                                          trackTree.put(Paths.get(file.getAbsolutePath()).register(watchService,
                                                StandardWatchEventKinds.ENTRY_CREATE,
                                                StandardWatchEventKinds.ENTRY_DELETE,
                                                StandardWatchEventKinds.ENTRY_MODIFY), file.getAbsolutePath());
                                    } catch (IOException e) {
                                          e.printStackTrace();
                                    }
                              }
                        }
                        if (!key.reset()) {
                              trackTree.remove(key);
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
                  try {
                        watchService.close();
                  } catch (IOException ex) {
                        ex.printStackTrace();
                  }
            }
      }
}
