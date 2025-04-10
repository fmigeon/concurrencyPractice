package concurrentEntitiesScheduler.scheduling.fair;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import concurrentEntitiesScheduler.scheduling.stopcondition.StoppingBehavior;

public class FairScheduler {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Queue<StoppingBehavior> queue = new ConcurrentLinkedQueue<>();
	private int stepCount = 1;

	public void manage(StoppingBehavior behavior) {
		queue.add(behavior);
	}

	public void startCycles() {
		Set<StoppingBehavior> activeEntities = new HashSet<>();
		activeEntities.addAll(queue);

		while (!activeEntities.isEmpty()) {
			System.out.println("Round " + stepCount++);
			
			List<Callable<Void>> tasks = activeEntities.stream()
										 .map(entity -> (Callable<Void>) () -> {entity.step();return null;})
										 .collect(Collectors.toList());

			try {
				executor.invokeAll(tasks); // attend que tous les step() soient faits
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			activeEntities.removeIf(StoppingBehavior::wantsToStop);
			activeEntities.addAll(queue);
			queue.clear();
			
		}
		executor.shutdown();
	}

}
