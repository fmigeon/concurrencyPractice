package concurrentEntitiesScheduler.scheduling.stopcondition;

import concurrentEntitiesScheduler.scheduling.Behavior;

public interface StoppingBehavior extends Behavior {
	
	public boolean wantsToStop();
	
}
