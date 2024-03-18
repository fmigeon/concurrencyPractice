package concurrentEntitiesScheduler.scheduling.forever.fair;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import concurrentEntitiesScheduler.scheduling.forever.StoppingBehavior;
import concurrentEntitiesScheduler.scheduling.forever.UntilStopScheduler;
import concurrentEntitiesScheduler.scheduling.forever.UntilStopScheduler.TenSteps;

public class FairScheduler {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Queue<StoppingBehavior> queue = new ConcurrentLinkedQueue<>();
	private int stepCount=0;
	
	public void manage(StoppingBehavior behavior) {
		queue.add(behavior);
	}
	
	public void startCycles() {
		System.out.println("Round "+stepCount++);
		List<Future<StoppingBehavior>> results = queue.parallelStream()
													  .map(behavior->executor.submit(()->{behavior.step();return behavior.wantsToStop()?null:behavior;}))
													  .collect(Collectors.toList());
		Function<Future<StoppingBehavior>,StoppingBehavior> futureTreatment = (future)-> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		};		
		List<StoppingBehavior> nextQueue = results.parallelStream()
												  .map(futureTreatment)
												  .filter(Objects::nonNull)
												  .collect(Collectors.toList());
//		List<StoppingBehavior> nextQueue = queue.parallelStream()
//												.map(behavior->{behavior.step(); return behavior.wantsToStop()?null:behavior;})
//												.collect(Collectors.filtering(beh->beh!=null, Collectors.toList()));
		queue = new ConcurrentLinkedQueue<>(nextQueue);
		if (!queue.isEmpty()) startCycles();
	}
	
	public class TenSteps implements StoppingBehavior {

		private int maxCount;
		private int count = 0;
		private String name;
		
		public TenSteps(String name, int maxCount) {
			this.name = name;
			this.maxCount = maxCount;
		}

		@Override
		public void step() {
			System.out.println(name + " : "+(++count) + " step(s)");
		}

		@Override
		public boolean wantsToStop() {
			return (count == maxCount);
		}

	}

	public static void main(String[] args) {
		FairScheduler sched = new FairScheduler();

		sched.manage(sched.new TenSteps("One",10));
		sched.manage(sched.new TenSteps("Two",10));
		sched.manage(sched.new TenSteps("Three",10));
		sched.startCycles();
		
//		for(int i=0; i<2;i++) {
//			TenSteps beh = sched.new TenSteps("Beh"+i,i);
//			sched.manage(beh);
//		}
//		sched.startCycles();

	}

	
}
