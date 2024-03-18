package asynchronousMessaging.receiving.queueing;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import asynchronousMessaging.Message;
import concurrentEntitiesScheduler.scheduling.forever.StoppingBehavior;

public class MessagePassingComponent<Msg extends Message> {
	private AsynchronousQueueingReceiver<Msg> receiverComponent = new AsynchronousQueueingReceiver<>();
	private AsynchronousQueueConsumer<Msg> consumerComponent = new AsynchronousQueueConsumer<>(receiverComponent.getQueue());
	
	public class SocialBehavior implements StoppingBehavior {

		private Consumer<Msg> messageTreatment;
		private Supplier<Boolean> stopConditionEvaluator;
		private DoStepInterface stepTreatment;
		
		
		private SocialBehavior(Consumer<Msg> messageTreatment, Supplier<Boolean> stopConditionEvaluator, DoStepInterface stepTreatment) {
			this.messageTreatment = messageTreatment;
			this.stopConditionEvaluator = stopConditionEvaluator;
			this.stepTreatment = stepTreatment;
		}
		
		@Override
		public final void step() {
			Optional<Msg> accepted = consumerComponent.acceptMessage();
			if (accepted.isPresent()) {
				treatMessage (accepted.get());
			}
			doStep();
		}
		
		public void treatMessage (Msg lastAcceptedMessage) {
			messageTreatment.accept(lastAcceptedMessage);
		}

		@Override
		public boolean wantsToStop() {
			return stopConditionEvaluator.get();
		}
		
		public void doStep() {
			stepTreatment.doStep();
		}
		
	}
	
	public SocialBehavior makeBehavior(Consumer<Msg> messageTreatment, Supplier<Boolean> stopConditionEvaluator, DoStepInterface stepTreatment) {
		return new SocialBehavior(messageTreatment, stopConditionEvaluator, stepTreatment);
	}

	public AsynchronousQueueingReceiver<Msg> getAsynchronousReceiver() {
		return receiverComponent;
	}
	
	public AsynchronousQueueConsumer<Msg> getAsynchronousConsumer() {
		return consumerComponent;
	}
	
	
}
