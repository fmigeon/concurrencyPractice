package TP_Concurrent;

import java.util.stream.IntStream;

import concurrentEntitiesScheduler.scheduling.forever.StoppingBehavior;
import concurrentEntitiesScheduler.scheduling.forever.fair.FairScheduler;

public class ConcurrentFairStoppingCountingEntities {

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
		FairScheduler scheduler = new FairScheduler();
		
		IntStream.range(0, 100).forEach((v)->scheduler.manage(new CountingEntity(""+v).getBehavior()));
		
		long startTime = System.currentTimeMillis();
		
		scheduler.startCycles();
		
		long endTime = System.currentTimeMillis();
		double elapsedTime = (endTime - startTime);
		
		System.out.println("Total epased time = "+elapsedTime);
	}
}
