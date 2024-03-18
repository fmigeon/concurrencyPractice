package concurrentEntitiesScheduler.scheduling.forever;

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

	public class TenSteps implements StoppingBehavior {

		private int count = 0;
		private String name;
		
		public TenSteps(String name) {
			this.name = name;
		}

		@Override
		public void step() {
			System.out.println(name + " : "+(++count) + " step(s)");
		}

		@Override
		public boolean wantsToStop() {
			return (count == 10);
		}

	}

	public static void main(String[] args) {
		UntilStopScheduler sched = new UntilStopScheduler();

		sched.manage(sched.new TenSteps("One"));
		sched.manage(sched.new TenSteps("Two"));
		sched.manage(sched.new TenSteps("Three"));
	}
}
