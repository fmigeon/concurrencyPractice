package concurrentEntitiesScheduler.scheduling.forever;

import concurrentEntitiesScheduler.scheduling.Behavior;

public interface StoppingBehavior extends Behavior {
	
	public boolean wantsToStop();
	
}
