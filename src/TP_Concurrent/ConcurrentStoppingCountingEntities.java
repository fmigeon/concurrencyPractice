package TP_Concurrent;

import java.util.stream.IntStream;

import concurrentEntitiesScheduler.scheduling.forever.StoppingBehavior;
import concurrentEntitiesScheduler.scheduling.forever.UntilStopScheduler;

public class ConcurrentStoppingCountingEntities {

	private static class CountingEntity {
		private int count = 0;
		private String name = "Agent";
		private StoppingBehavior behavior = new StoppingBehavior() {
			@Override
			public void step() {
				System.out.println(name+ " is counting : "+count++);
			}

			@Override
			public boolean wantsToStop() {
				return count>100;
			}
			
		};
		private CountingEntity(String suffix) {
			name+=suffix;
		}
		private StoppingBehavior getBehavior() {return behavior;}
	}
	
	public static void main(String[] args) {
		UntilStopScheduler scheduler = new UntilStopScheduler();
		
		IntStream.range(0, 100).forEach((v)->scheduler.manage(new CountingEntity(""+v).getBehavior()));
		
		
	}
}
