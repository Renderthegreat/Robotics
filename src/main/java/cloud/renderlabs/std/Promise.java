package cloud.renderlabs.std;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

public class Promise<T> {
	private final CompletableFuture<T> future;

	private Promise(CompletableFuture<T> future) {
		this.future = future;
	};

	public Promise(Supplier<T> next) {
		this(CompletableFuture.supplyAsync(next));
	};

	/**
	 * Resolves a promise with the given value.
	 *
	 * @param value The value with which to resolve the promise
	 * @return A promise that is resolved with the given value
	 */
	public static <T> Promise<T> resolve(T value) {
		return new Promise<>(CompletableFuture.completedFuture(value));
	};

	/**
	 * Creates a new promise that is resolved with the value returned by the given function.
	 * The given function is called with the value with which this promise is resolved.
	 * If the given function returns a promise, the new promise is resolved or rejected with the same value.
	 * If the given function throws an exception, the new promise is rejected with the same exception.
	 *
	 * @param next The function to call when this promise is resolved
	 * @return A promise that is resolved with the value returned by the given function
	 */
	public <U> Promise<U> then(Function<T, U> next) {
		return new Promise<>(future.thenApply(next));
	};

	/**
	 * Awaits a promise and returns its result.
	 * 
	 * @param promise The promise to await
	 * @return The result of the promise
	 * @throws RuntimeException If the promise is rejected
	 */
	public static <T> T await(Promise<T> promise) {
		T result;
		try {
			result = promise.future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		};

		return result;
	};
};
