package com.teri.systemtracking.Server.server;

import com.teri.systemtracking.Server.core.MessageReceiver;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.Server.model.ReceivedMessage;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.Packet;
import com.teri.systemtracking.common.model.Event;
import com.teri.systemtracking.common.utils.SerializeUtils;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSession implements MessageSender, MessageReceiver {

      private static final int BUFFER_CAPACITY = 4096;
      private final ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_CAPACITY);
      private final Queue<ByteBuffer> writeBufferQueue = new LinkedBlockingQueue<>();

      private final SelectionKey key;
      private final Client client;
      ClientFolderTracker folderTracker;

      private final Map<String, Subject<Object>> observables = new ConcurrentHashMap<>();
      private final Executor executor;

      public ClientSession(SelectionKey key, Client client, Executor executor) {
            this.key = key;
            this.client = client;
            this.executor = executor;
      }

      public Client getClient() {
            return client;
      }

      SelectionKey getKey() {
            return key;
      }

      ByteBuffer getReadBuffer() {
            return readBuffer;
      }

      ByteBuffer getWriteBuffer() {
            ByteBuffer writeBuffer = writeBufferQueue.peek();
            if (writeBuffer != null && !writeBuffer.hasRemaining()) {
                  writeBufferQueue.poll();
                  writeBuffer = writeBufferQueue.peek();
            }
            return writeBuffer;
      }

      private synchronized void setByteBuffer(byte[] payload) {
            ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_CAPACITY);
            writeBuffer.putInt(payload.length);
            writeBuffer.put(payload);
            writeBuffer.flip();
            writeBufferQueue.offer(writeBuffer);
      }

      @Override
      public Observable<Object> send(Message message) {
            final String packetId = UUID.randomUUID().toString();
            String observableId = packetId;
            BehaviorSubject<Object> observable = BehaviorSubject.create();
            observables.put(observableId, observable);
            executor.execute(() -> {
                  byte[] payload = SerializeUtils.serialize(message);
                  Packet packet = new Packet(packetId, payload);
                  setByteBuffer(SerializeUtils.serialize(packet));
            });
            return observable.observeOn(SwingSchedulers.edt());
      }

      @Override
      public void onMessage(ReceivedMessage message) {
            String packetId = message.getRawPacket().getId();
            if (packetId.equals(Packet.EVENT_PACKET)) {
                  if (folderTracker != null)
                        folderTracker.eventStream.onNext((Event) message.getContent());
                  return;
            }
            Subject<Object> observable = observables.remove(packetId);
            if (observable != null) {
                  observable.onNext(message.getContent());
            }
      }
}