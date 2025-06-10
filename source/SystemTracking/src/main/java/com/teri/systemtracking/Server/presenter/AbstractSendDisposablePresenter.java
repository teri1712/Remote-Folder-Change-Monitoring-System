package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.common.Message;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public abstract class AbstractSendDisposablePresenter implements Presenter {

      private final MessageSender messageSender;
      private CompositeDisposable disposable;

      protected AbstractSendDisposablePresenter(MessageSender messageSender) {
            this.messageSender = messageSender;
      }

      protected Disposable send(Message message, Consumer<Object> consumer) {
            Disposable d = this.messageSender.send(message).subscribe(consumer);
            disposable.add(d);
            return d;
      }

      protected abstract void doStart();

      protected abstract void doStop();

      @Override
      public final void start() {
            disposable = new CompositeDisposable();
            doStart();
      }

      @Override
      public final void stop() {
            disposable.dispose();
            doStop();
      }
}
