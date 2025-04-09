package preparation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Exo1 {

	public static ExecutorService service = Executors.newCachedThreadPool();
	
	public static class Counting {
		
		public void submit(int n) {
			Runnable task = () -> {
				System.out.println("Demarrage de la tache");
				int sum = IntStream.range(1,n+1).sum();
				System.out.println("Resultat = "+sum);
			};
			service.execute(task);
		}
	}
	
	public static void main(String[] args) {
		Counting c = new Counting();
		c.submit(1000);
	}
}
