package eu.fays.sandbox.optimization;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class OptimizationEssay {
	public static void main(String[] args) {
		final SortedMap<Long, Integer> result = new TreeMap<>();

		final int N = 1 << 20;
		final Random random = new Random(0);
		long[] n = new long[N];
		long expected = 0L;

		// Random number generation
		{
			for (int i = 0; i < N; i++) {
				n[i] = random.nextInt();
				expected += n[i];
			}
		}
		final List<Long> l = Arrays.stream(n).mapToObj(i -> Long.valueOf(i)).collect(toList());

		// #0 : array -> stream -> reduce
		{
			final long t0 = System.nanoTime();
			final long sum = Arrays.stream(n).sum();
			result.put(System.nanoTime() - t0, 0);
			assert sum == expected;
		}
		
		// #1 : array -> stream -> reduce
		{
			final long t0 = System.nanoTime();
			final long sum = Arrays.stream(n).reduce(0L, (a, v) -> a + v);
			result.put(System.nanoTime() - t0, 1);
			assert sum == expected;
		}

		// #2 : list -> stream -> reduce
		{
			final long t0 = System.nanoTime();
			final long sum = l.stream().reduce(0L, (a, v) -> a + v);
			result.put(System.nanoTime() - t0, 2);
			assert sum == expected;
		}

		// #3 : list -> parallel stream -> reduce
		{
			final long t0 = System.nanoTime();
			final long sum = l.stream().parallel().reduce(0L, (a, v) -> a + v);
			result.put(System.nanoTime() - t0, 3);
			assert sum == expected;
		}

		// #4 : array -> for-loop sum
		{
			final long t0 = System.nanoTime();
			long sum = 0L;
			for (int i = 0; i < N; i++) {
				sum += n[i];
			}
			result.put(System.nanoTime() - t0, 4);
			assert sum == expected;
		}

		// #5 : list -> for-loop sum #1
		{
			final long t0 = System.nanoTime();
			long sum = 0L;
			for (int i = 0; i < N; i++) {
				sum += l.get(i);
			}
			result.put(System.nanoTime() - t0, 5);
			assert sum == expected;
		}

		// #6 : list -> for-loop sum #2
		{
			final long t0 = System.nanoTime();
			long sum = 0L;
			for (int i = 0; i < l.size(); i++) {
				sum += l.get(i);
			}
			result.put(System.nanoTime() - t0, 6);
			assert sum == expected;
		}

		// #7 : list -> iterator sum
		{
			final long t0 = System.nanoTime();
			long sum = 0L;
			for (Long v : l) {
				sum += v;
			}
			result.put(System.nanoTime() - t0, 7);
			assert sum == expected;
		}

		result.forEach((k, v) -> System.out.println("#" + v + ": " + String.format("%12s", format("{0,number,###,###,###}", k)) + " ns"));
	}
}
