package concurrentEntitiesScheduler.scheduling.forever;

import concurrentEntitiesScheduler.scheduling.Behavior;
import concurrentEntitiesScheduler.scheduling.forever.DemoForever.Counting;

public class DemoForever {


	public static class Counting implements Behavior {

		private int count = 0;
		private String name;

		public Counting(String name) {
			this.name = name;
		}

		@Override
		public void step() {
			System.out.println(name + " : " + (++count) + " step(s)");
		}

	}

	public static void main(String[] args) {
		ForeverScheduler sched = new ForeverScheduler();

		sched.manage(new Counting("One"));
		sched.manage(new Counting("Two"));
		sched.manage(new Counting("Three"));
	}

}
