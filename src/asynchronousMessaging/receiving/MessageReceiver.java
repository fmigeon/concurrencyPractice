package asynchronousMessaging.receiving;

import asynchronousMessaging.Message;

public interface MessageReceiver<Msg extends Message> {

	public void receive (Msg msg);
}
