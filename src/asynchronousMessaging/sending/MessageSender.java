package asynchronousMessaging.sending;

import asynchronousMessaging.Message;
import asynchronousMessaging.receiving.MessageReceiver;

public interface MessageSender<Msg extends Message> {
	
	public default void send(Msg msg, MessageReceiver<Msg> receiver) {
		receiver.receive(msg);
	}

}
