package TP_Concurrent;

import asynchronousMessaging.Message;
import asynchronousMessaging.receiving.queueing.AsynchronousQueueingReceiver;
import asynchronousMessaging.receiving.queueing.MessagePassingComponent;
import asynchronousMessaging.sending.MessageSender;
import concurrentEntitiesScheduler.scheduling.fair.FairScheduler;
import concurrentEntitiesScheduler.scheduling.stopcondition.StoppingBehavior;

public class ConcurrenCommunicatingCountingEntities {

	private static class NoNeighbourCommunicatingAndCountingEntity implements MessageSender<IntMessage> {
		protected int count = 0;
		protected int upperBound;
		protected String name = "Agent";
		protected StoppingBehavior behavior;
		protected MessagePassingComponent<IntMessage> communicationComponent = new MessagePassingComponent<>();

		private NoNeighbourCommunicatingAndCountingEntity(int to, String suffix) {
			name += suffix;
			upperBound = to;
		}

		protected StoppingBehavior getBehavior() {
			if (behavior == null) {
				behavior = communicationComponent.makeBehavior(this::doTreatMessage, this::wantToStop, this::doStep);
			}
			return behavior;
		}

		protected boolean wantToStop() {
			return count != 0;
//			return (count >= upperBound);
		}

		protected void doTreatMessage(IntMessage message) {
			count = message.getContent();
			System.out.println(name + " : accepting message  " + count);
		}

		protected void doStep() {
			if (count != 0) {
				System.out.println(name + " is finally counting : " + count++);
				if (count == upperBound) {
					System.out.println(name + " : I'm done !");
				}
			}
		}

		private AsynchronousQueueingReceiver<IntMessage> getID() {
			return communicationComponent.getAsynchronousReceiver();
		}
	}

	private static class CommunicatingCountingEntity extends NoNeighbourCommunicatingAndCountingEntity {
		private AsynchronousQueueingReceiver<IntMessage> neighbour;

		private CommunicatingCountingEntity(int to, String suffix, AsynchronousQueueingReceiver<IntMessage> next) {
			super(to, suffix);
			neighbour = next;
		}

		@Override
		protected StoppingBehavior getBehavior() {
			if (behavior == null) {
				behavior = communicationComponent.makeBehavior(this::doTreatMessage, this::wantToStop, this::doStep);
			}
			return behavior;
		}

		protected void doStep() {
			System.out.println(name + " : Sending message " + (upperBound + SLICE) + " to neighbour " + neighbour);
			send(new IntMessage(upperBound + SLICE), neighbour);
			fib(40);

//			if (count != 0) {
//				System.out.println(name + " is counting : " + count++);
//				if (count == upperBound) {
//					send(new IntMessage(upperBound + SLICE), neighbour);
//				}
//			} else {
//				System.out.println(name + " has count " + count + " and is waiting message");
//				fib(30);
//			}
		}
	}

	private static long fib(int n) {
		if (n < 2)
			return 1;
		else
			return fib(n - 1) + fib(n - 2);
	}

	private static class IntMessage implements Message {
		private int content;

		private IntMessage(int value) {
			content = value;
		}

		private int getContent() {
			return content;
		}
	}

	private static final int UPPER_BOUND = 100;
	private static final int SLICE = 10;

	public static void main(String[] args) {
		System.out.println("Starting at " + System.currentTimeMillis());
		FairScheduler scheduler = new FairScheduler();
		NoNeighbourCommunicatingAndCountingEntity chain[] = new NoNeighbourCommunicatingAndCountingEntity[UPPER_BOUND/SLICE + 1];
		int index =chain.length -1;

		int currentValue = UPPER_BOUND;
		chain[index--]  = new NoNeighbourCommunicatingAndCountingEntity(currentValue, "last");
		System.out.println("Last entity creation : " + chain[index+1].getID());

		currentValue -= SLICE;
		while (currentValue >= 0) {
			chain[index] = new CommunicatingCountingEntity(currentValue, "_" + currentValue, chain[index+1].getID());
			System.out.println("Entity creation : " + chain[index].getID());
			index--;
			currentValue -= SLICE;
		}
		
		// L'ordre de demande de management a une influence sur l'ordre des messages : BAD  !!!
		for(NoNeighbourCommunicatingAndCountingEntity entity : chain) {
			scheduler.manage(entity.getBehavior());
		}
		
		MessageSender<IntMessage> sender = new MessageSender<IntMessage>() {
		};
		sender.send(new IntMessage(1), chain[0].getID());
		System.out.println("Get ready");
		scheduler.startCycles();
	}

}
