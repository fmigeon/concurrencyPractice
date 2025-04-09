package TP_Concurrent;

import java.util.stream.IntStream;

import concurrentEntitiesScheduler.scheduling.fair.FairScheduler;
import concurrentEntitiesScheduler.scheduling.stopcondition.StoppingBehavior;

public class ConcurrentFairStoppingCountingEntities {
	
	public static void main(String[] args) {
		FairScheduler scheduler = new FairScheduler();
		
		IntStream.range(0, 100).forEach((v)->scheduler.manage(new ConcurrentStoppingCountingEntities.CountingEntity(""+v).getBehavior()));
		
		long startTime = System.currentTimeMillis();
		
		scheduler.startCycles();
		
		long endTime = System.currentTimeMillis();
		double elapsedTime = (endTime - startTime);
		
		System.out.println("Total epased time = "+elapsedTime);
	}
}
