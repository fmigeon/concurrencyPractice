package preparation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Exo2 {

	public static ExecutorService service = Executors.newCachedThreadPool();

	public static class TaskWithFuture {

		public long soumettre(int n) {
			long res = 0;
			Callable<Long> task = () -> {
				System.out.println("Demarrage du calcul");
				return fib(n);
			};
			Future<Long> resultatAttendu = service.submit(task);
			System.out.print("Fib(" + n + ")=");
			res = getFuture(resultatAttendu);
			System.out.println(res);
			return res;
		}

		private long getFuture(Future<Long> resultatAttendu) {
			long res = 0;
			try {
				res = resultatAttendu.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
		}
	}

	public static void main(String[] args) {
		TaskWithFuture t = new TaskWithFuture();
		long result = t.soumettre(15);
	}

	private static long fib(int n) {
		if (n < 2)
			return 1;
		else
			return fib(n - 1) + fib(n - 2);
	}

}
