package asynchronousMessaging.receiving.queueing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import asynchronousMessaging.Message;
import asynchronousMessaging.receiving.MessageReceiver;

public class AsynchronousQueueingReceiver<Msg extends Message> implements MessageReceiver<Msg> {

	private Queue<Msg> messageQueue = new ConcurrentLinkedQueue<>();

	@Override
	public void receive(Msg msg) {
		messageQueue.add(msg);
	}

	Queue<Msg> getQueue() {
		return messageQueue;
	}
}
