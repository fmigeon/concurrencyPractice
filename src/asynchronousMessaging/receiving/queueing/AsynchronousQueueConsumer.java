package asynchronousMessaging.receiving.queueing;

import java.util.Optional;
import java.util.Queue;

import asynchronousMessaging.Message;

public class AsynchronousQueueConsumer<Msg extends Message> {

	private Queue<Msg> messageQueue;

	public AsynchronousQueueConsumer(Queue<Msg> messageQueue) {
		super();
		this.messageQueue = messageQueue;
	}

	public Optional<Msg> acceptMessage() {
		return Optional.ofNullable(messageQueue.poll());
	}

}
