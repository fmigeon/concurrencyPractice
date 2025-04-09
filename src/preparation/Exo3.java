package preparation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Exo3 {

	public static ExecutorService service = Executors.newCachedThreadPool();

	public static class TaskWithFuture {

		public long soumettre(int n) {
			long res = 0;
			if (n<2) {
				return 1;
			} else {
			Callable<Long> taskn1 = () -> soumettre(n-1);
			Callable<Long> taskn2 = () -> soumettre(n-2);
			Future<Long> fibn1 = service.submit(taskn1);
			Future<Long> fibn2 = service.submit(taskn2);
			long res1 = getFuture(fibn1);
			long res2 = getFuture(fibn2);
			res = res1 + res2;
			return res;
			}
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
		System.out.println("Fibonacci concurrent");
		long result = t.soumettre(15);
		System.out.println("Fib(10)="+result);
	}

	
}
