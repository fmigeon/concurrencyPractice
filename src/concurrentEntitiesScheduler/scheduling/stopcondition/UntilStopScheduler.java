package concurrentEntitiesScheduler.scheduling.stopcondition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UntilStopScheduler {

	private ExecutorService executor = Executors.newCachedThreadPool();

	public void manage(StoppingBehavior behavior) {
		executor.execute(() -> {
			behavior.step();
			if (!behavior.wantsToStop())
				manage(behavior);
		});
	}

}
