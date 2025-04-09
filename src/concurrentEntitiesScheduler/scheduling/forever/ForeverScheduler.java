package concurrentEntitiesScheduler.scheduling.forever;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrentEntitiesScheduler.scheduling.Behavior;

public class ForeverScheduler {
	private ExecutorService executor = Executors.newCachedThreadPool();

	public void manage(Behavior behavior) {
		executor.execute(() -> {
			behavior.step();
			manage(behavior);
		});
	}

}
