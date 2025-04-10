package concurrentEntitiesScheduler.scheduling.fair;

import concurrentEntitiesScheduler.scheduling.Behavior;
import concurrentEntitiesScheduler.scheduling.forever.DemoForever.Counting;
import concurrentEntitiesScheduler.scheduling.stopcondition.StoppingBehavior;
import concurrentEntitiesScheduler.scheduling.stopcondition.UntilStopScheduler;
import concurrentEntitiesScheduler.scheduling.stopcondition.DemoStoppingBehavior.TenSteps;

public class DemoForeverBehaviorFairScheduler {


	public static class ForeverSteps implements StoppingBehavior {

		private static final boolean NEVER = false;
		private int count = 0;
		private String name;
		
		public ForeverSteps(String name) {
			this.name = name;
		}

		@Override
		public void step() {
			System.out.println(name + " : "+(++count) + " step(s)");
		}

		@Override
		public boolean wantsToStop() {
			return NEVER; 
		}

	}

	public static void main(String[] args) {
		FairScheduler sched = new FairScheduler();
		for(int i=0; i < 3;i++) 
			sched.manage(new ForeverSteps("-"+i+"-"));
		
		sched.startCycles();
	}

}
