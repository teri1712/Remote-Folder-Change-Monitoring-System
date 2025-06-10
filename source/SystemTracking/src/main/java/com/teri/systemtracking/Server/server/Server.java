package com.teri.systemtracking.Server.server;

import com.teri.systemtracking.Server.core.*;
import com.teri.systemtracking.Server.core.protocol.MessageProtocolHandler;
import com.teri.systemtracking.Server.core.protocol.ProtocolHandler;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.common.Packet;
import com.teri.systemtracking.common.model.Folder;
import com.teri.systemtracking.common.utils.SerializeUtils;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class Server implements ClientRepository {

      public static final int PORT = 6969;
      private final Selector selector;
      private final ServerSocketChannel server;
      private final Executor offloadTaskExecutor;
      private final List<ProtocolHandler> protocolHandlers;
      private final Map<Integer, ClientSession> sessions = new ConcurrentHashMap<>();
      private final List<ClientListener> clientListeners = new CopyOnWriteArrayList<>();
      private final BlockingQueue<Runnable> mainThreadTaskQueue;
      private int countConnections = 0;

      public Server() throws IOException {
            this.server = ServerSocketChannel.open();
            this.selector = Selector.open();
            this.offloadTaskExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 2);
            this.protocolHandlers = new ArrayList<>();
            mainThreadTaskQueue = new LinkedBlockingQueue<>();
      }

      private void initProtocolHandlers() {
            protocolHandlers.add(new InitialPacketHandler());
            protocolHandlers.add(new MessageProtocolHandler());
      }

      public void startServer() {
            if (server.socket().isBound()) {
                  return;
            }
            initProtocolHandlers();
            try {
                  server.configureBlocking(false);
                  server.bind(new InetSocketAddress(PORT));
                  server.register(selector, SelectionKey.OP_ACCEPT);
                  while (true) {
                        lookUpPendingTaskOnMainThread();
                        selector.select();
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();
                        Iterator<SelectionKey> it = selectedKeys.iterator();
                        while (it.hasNext()) {
                              SelectionKey key = it.next();
                              it.remove();
                              if (key.isAcceptable()) {
                                    SocketChannel clientChannel = server.accept();
                                    if (clientChannel != null) {
                                          clientChannel.configureBlocking(false);
                                          countConnections++;
                                          Client client = new Client(countConnections, new Date(System.currentTimeMillis()));
                                          SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ, client);
                                          ClientSession session = new ClientSession(clientKey, client, offloadTaskExecutor);
                                          sessions.put(client.getId(), session);
                                    }
                              } else if (key.isReadable()) {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ClientSession session = sessions.get(((Client) key.attachment()).getId());
                                    ByteBuffer readBuffer = session.getReadBuffer();
                                    int bRead = clientChannel.read(readBuffer);
                                    if (bRead == -1) {
                                          sessions.remove(session.getClient().getId());
                                          clientChannel.close();
                                          key.cancel();
                                          dispatchRemove(session.getClient());
                                          continue;
                                    }
                                    readBuffer.flip();
                                    if (readBuffer.remaining() < 4) {
                                          readBuffer.compact();
                                          continue;
                                    }
                                    // checkpoint to roll back to the current 0-limit
                                    readBuffer.mark();
                                    int contentLength = readBuffer.getInt();
                                    if (contentLength > readBuffer.remaining()) {
                                          // rollback
                                          readBuffer.reset();
                                          readBuffer.compact();
                                          continue;
                                    }
                                    byte[] payload = new byte[contentLength];

                                    readBuffer.get(payload);
                                    readBuffer.compact();
                                    Packet packet = SerializeUtils.deserialize(payload);
                                    System.out.println("Recieved packet: " + packet.getId() + " , length = " + packet.getPayload().length);
                                    for (ProtocolHandler handler : protocolHandlers) {
                                          if (handler.supports(packet)) {
                                                offloadTaskExecutor.execute(new ProtocolRunner(handler, session, session.getClient(), packet));
                                                break;
                                          }
                                    }

//                                    key.interestOps(SelectionKey.OP_WRITE);
                              } else if (key.isWritable()) {
                                    SocketChannel client = (SocketChannel) key.channel();
                                    ClientSession session = sessions.get(((Client) key.attachment()).getId());
                                    ByteBuffer writeBuffer = session.getWriteBuffer();
                                    if (writeBuffer == null) {
                                          continue;
                                    }
                                    if (!writeBuffer.hasRemaining()) {
                                          key.interestOps(SelectionKey.OP_READ);
                                          continue;
                                    }
                                    client.write(writeBuffer);
                              }
                        }
                  }
            } catch (IOException e) {
                  throw new RuntimeException(e);
            }
      }

      private void lookUpPendingTaskOnMainThread() {
            Runnable task = mainThreadTaskQueue.poll();
            if (task != null) {
                  task.run();
            }
      }

      private void dispatchAdd(final Client client) {
            EventQueue.invokeLater(new Runnable() {

                  @Override
                  public void run() {
                        for (ClientListener listener : clientListeners) {
                              listener.onClientConnected(client);
                        }
                  }
            });
      }

      private void dispatchRemove(final Client client) {
            EventQueue.invokeLater(new Runnable() {

                  @Override
                  public void run() {
                        for (ClientListener listener : clientListeners) {
                              listener.onClientDisconnected(client);
                        }
                  }
            });
      }


      @Override
      public FolderTracker getFolderTracker(Client client) {
            return sessions.get(client.getId()).folderTracker;
      }

      @Override
      public MessageSender getSender(Client client) {
            return sessions.get(client.getId());
      }

      @Override
      public void remove(final Client client) {
            final ClientSession session = sessions.get(client.getId());
            final SelectionKey key = session.getKey();
            final SocketChannel clientChannel = (SocketChannel) key.channel();
            mainThreadTaskQueue.add(new Runnable() {
                  @Override
                  public void run() {
                        if (session != null) {
                              key.cancel();
                              try {
                                    clientChannel.close();
                                    dispatchRemove(client);
                              } catch (IOException e) {
                                    e.printStackTrace();
                              }
                        }
                  }
            });
            key.selector().wakeup();
      }

      @Override
      public void addClientListener(ClientListener listener) {
            clientListeners.add(listener);
      }

      @Override
      public void removeClientListener(ClientListener listener) {
            clientListeners.remove(listener);
      }


      private class InitialPacketHandler implements ProtocolHandler {

            public InitialPacketHandler() {
            }

            @Override
            public boolean supports(Packet packet) {
                  return packet.getId().equals(Packet.INITIAL_REQUEST_PACKET);
            }

            @Override
            public void handle(Client client, Packet packet, MessageReceiver receiver) {
                  List<Folder> drives = SerializeUtils.deserialize(packet.getPayload());
                  ClientSession session = sessions.get(client.getId());
                  session.folderTracker = new ClientFolderTracker(session, drives);
                  dispatchAdd(client);
            }

      }

}
