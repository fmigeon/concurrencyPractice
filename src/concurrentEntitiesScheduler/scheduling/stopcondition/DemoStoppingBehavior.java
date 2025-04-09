package concurrentEntitiesScheduler.scheduling.stopcondition;

public class DemoStoppingBehavior {

	public static class TenSteps implements StoppingBehavior {

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

		sched.manage(new TenSteps("One"));
		sched.manage(new TenSteps("Two"));
		sched.manage(new TenSteps("Three"));
	}

}
