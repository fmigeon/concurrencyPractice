package concurrentEntitiesScheduler.scheduling.fair;

import concurrentEntitiesScheduler.scheduling.Behavior;
import concurrentEntitiesScheduler.scheduling.forever.DemoForever.Counting;
import concurrentEntitiesScheduler.scheduling.stopcondition.StoppingBehavior;
import concurrentEntitiesScheduler.scheduling.stopcondition.UntilStopScheduler;
import concurrentEntitiesScheduler.scheduling.stopcondition.DemoStoppingBehavior.TenSteps;

public class DemoFairScheduler {


	public static class TenSteps implements StoppingBehavior {

		private int count = 0;
		private int max;
		private String name;
		
		public TenSteps(String name, int max) {
			this.name = name;
			this.max = max;
		}

		@Override
		public void step() {
			System.out.println(name + " : "+(++count) + " step(s)");
		}

		@Override
		public boolean wantsToStop() {
			return (count == max);
		}

	}

	public static void main(String[] args) {
		FairScheduler sched = new FairScheduler();
		for(int i=0; i < 3;i++) 
			sched.manage(new TenSteps("-"+i+"-",10-2*i));
		
		sched.startCycles();
	}

}
