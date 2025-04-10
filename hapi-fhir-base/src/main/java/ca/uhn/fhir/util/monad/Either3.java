package ca.uhn.fhir.util.monad;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a value of one of three possible types (a disjoint union). An instance of Either3 is either an instance of
 * Left, Middle, or Right, only one of which can be present at a time.
 *
 * This type is right-biased, meaning that operations like map and flatMap will apply to the right value if it is present.
 * By convention, the preferred type for a given context should be the right value.
 *
 * By convention, the left value is used for error handling, when used in a context where one of the values is an error or exception.
 *
 * While this class does provide methods for accessing the left, middle, and right values, it is generally recommended to use
 * the map, flatMap, and fold methods to work with the values. These methods are more idiomatic and less error-prone, and they
 * are less likely to result in IllegalStateExceptions.
 *
 * @param <L> the left value type
 * @param <M> the middle value type
 * @param <R> the right value type
 */
public class Either3<L, M, R> {

	private final L left;
	private final M middle;
	private final R right;

	/*
	 * Private constructor. Use static factory methods for instantiation. See {@link Eithers}.
	 */
	Either3(L left, M middle, R right) {
		checkArgument(left != null ^ middle != null ^ right != null, "left, middle, and right are mutually exclusive");
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	/**
	 * Swaps the position of the left and right types The middle type is unchanged.
	 * The value is unchanged.
	 *
	 * @return a new Either3 with the left and right types swapped
	 */
	public Either3<R, M, L> swap() {
		if (isRight()) {
			return Eithers.forLeft3(right);
		} else if (isMiddle()) {
			return Eithers.forMiddle3(middle);
		} else {
			return Eithers.forRight3(left);
		}
	}

	/**
	 * Rotates the types of the Either3 to the right. The right type becomes the left type, the left type becomes the middle type,
	 * and the middle type becomes the right type. The values are unchanged.
	 *
	 * @return new Either3 with the types rotated
	 */
	public Either3<R, L, M> rotate() {
		if (isRight()) {
			return Eithers.forLeft3(right);
		} else if (isMiddle()) {
			return Eithers.forRight3(middle);
		} else {
			return Eithers.forMiddle3(left);
		}
	}

	/**
	 * Returns the left value. Throws an exception if the left value is not present.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function, Function)}
	 *
	 * @throws IllegalStateException if the left value is not present
	 * @return the left value
	 */
	public L leftOrThrow() {
		checkState(isLeft());
		return left;
	}

	/**
	 * Returns the middle value. Throws an exception if the middle value is not present.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function, Function)}
	 *
	 * @throws IllegalStateException if the middle value is not present
	 * @return the middle value
	 */
	public M middleOrThrow() {
		checkState(isMiddle());
		return middle;
	}

	/**
	 * Returns the right value. Throws an exception if the right value is not present.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function, Function)}
	 * Alternatively, use {@link #orElse(Object)} or {@link #orElseGet(Supplier)}
	 *
	 * @throws IllegalStateException if the right value is not present
	 * @return the right value
	 */
	public R rightOrThrow() {
		checkState(isRight());
		return right;
	}

	/**
	 * Alias for {@link #rightOrThrow()}.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function, Function)}
	 * Alternatively, use {@link #orElse(Object)} or {@link #orElseGet(Supplier)}
	 *
	 * @throws IllegalStateException if the right value is not present
	 * @return the right value
	 */
	public R getOrThrow() {
		return rightOrThrow();
	}

	/**
	 * Returns the right value if present, otherwise return the provided default value.
	 *
	 * @param defaultValue the value to return if the right value is not present
	 * @return the right value if present, otherwise the default value
	 */
	public R orElse(R defaultValue) {
		if (isRight()) {
			return right;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns the right value if present, otherwise return the result of the provided supplier.
	 *
	 * @param defaultSupplier the supplier to provide a default value if the right value is not present
	 * @return the right value if present, otherwise the result of the supplier
	 */
	public R orElseGet(Supplier<R> defaultSupplier) {
		if (isRight()) {
			return right;
		} else {
			return defaultSupplier.get();
		}
	}

	/**
	 * Returns true if the value is the left type. Otherwise, returns false.
	 *
	 * @return true if left is present
	 */
	public boolean isLeft() {
		return left != null;
	}

	/**
	 * Returns true if value is the middle type. Otherwise, returns false.
	 *
	 * @return true if middle is present
	 */
	public boolean isMiddle() {
		return middle != null;
	}

	/**
	 * Returns true if the value is the right type. Otherwise, returns false.
	 *
	 * @return true if right is present
	 */
	public boolean isRight() {
		return right != null;
	}

	/**
	 * Executes the provided consumer if the right value is present.
	 *
	 * @param forRight the consumer to execute if the right value is present
	 */
	public void forEach(Consumer<? super R> forRight) {
		checkNotNull(forRight);
		if (isRight()) {
			forRight.accept(right);
		}
	}

	/**
	 * Executes the provided consumer if the right value is present, returning the Either3 unchanged.
	 *
	 * @param forRight the consumer to execute if the right value is present
	 * @return the Either3 unchanged
	 */
	public Either3<L, M, R> peek(Consumer<? super R> forRight) {
		checkNotNull(forRight);
		if (isRight()) {
			forRight.accept(right);
		}

		return this;
	}

	/**
	 * Maps the right value to a new value using the provided function.
	 *
	 * If the right value is not present, returns the left or middle value unchanged.
	 *
	 * @param <T> the type of the new value
	 * @param mapRight the function to map the right value to a new value
	 * @return a new Either3 with the right value mapped to a new value, or the left or middle value
	 */
	public <T> Either3<L, M, T> map(Function<? super R, ? extends T> mapRight) {
		checkNotNull(mapRight);
		if (isRight()) {
			return Eithers.forRight3(mapRight.apply(right));
		}

		return propagate();
	}

	/**
	 * Maps the Either to a new Either using the provided function.
	 *
	 * If the right value is present, the function is applied to the right value.
	 *
	 * If the right value is not present, the left or middle value is returned.
	 *
	 * @param <T> the type of the new right value
	 * @param flatMapRight the function to map the Either to a new Either
	 * @return a new Either3 with the right value mapped to a new Either, or the left or middle value
	 */
	public <T> Either3<L, M, T> flatMap(Function<? super R, ? extends Either3<L, M, ? extends T>> flatMapRight) {
		checkNotNull(flatMapRight);
		if (isRight()) {
			return narrow(flatMapRight.apply(right));
		}

		return propagate();
	}

	/**
	 * Maps the left, middle, or right value to a new value using the provided functions.
	 *
	 * The function is sometimes known as "reduce".
	 *
	 * If the right value is present, the foldRight function is applied to the right value.
	 * If the middle value is present, the foldMiddle function is applied to the middle value.
	 * If the left value is present, the foldLeft function is applied to the left value.
	 *
	 * @param <T> the type of the new value
	 * @param foldLeft the function to map the left value to a new value, if present
	 * @param foldMiddle the function to map the middle value to a new value, if present
	 * @param foldRight the function to map the right value to a new value, if present
	 * @return the new value
	 */
	public <T> T fold(
			Function<? super L, ? extends T> foldLeft,
			Function<? super M, ? extends T> foldMiddle,
			Function<? super R, ? extends T> foldRight) {
		checkNotNull(foldLeft);
		checkNotNull(foldMiddle);
		checkNotNull(foldRight);
		if (isRight()) {
			return foldRight.apply(right);
		} else if (isMiddle()) {
			return foldMiddle.apply(middle);
		} else {
			return foldLeft.apply(left);
		}
	}

	/**
	 * Transforms the Either to a new value using the provided function. The function is applied to the entire Either,
	 * regardless of which value is present. This is in contrast to the map, flatMap, and fold functions, which only apply
	 * when the right value is present.
	 *
	 * @param <U> the type of the new value
	 * @param transform the function to transform the Either to a new value
	 * @return the new value
	 */
	public <U> U transform(Function<? super Either3<? super L, ? super M, ? super R>, ? extends U> transform) {
		return transform.apply(this);
	}

	/**
	 * Returns a stream of the right value if present, otherwise an empty stream.
	 * Mainly useful for converting to a stream for further processing with standard Java
	 * APIs like Stream.map, Stream.filter, etc.
	 *
	 * @return the stream of the right value if present
	 */
	public Stream<R> stream() {
		if (isRight()) {
			return Stream.of(right);
		} else {
			return Stream.of();
		}
	}

	/**
	 * Returns an Optional containing the right value if present, otherwise an empty Optional.
	 * Mainly useful for converting to an Optional for further processing with standard Java
	 * APIs, like Optional.map, Optional.filter, etc.
	 *
	 * @return an Optional containing the right value if present
	 */
	public Optional<R> optional() {
		if (isRight()) {
			return Optional.of(right);
		}

		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	protected <T> Either3<L, M, T> narrow(Either3<L, M, ? extends T> wide) {
		return (Either3<L, M, T>) wide;
	}

	@SuppressWarnings("unchecked")
	protected <T> Either3<L, M, T> propagate() {
		return (Either3<L, M, T>) this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Either3<?, ?, ?>) {
			Either3<?, ?, ?> other = (Either3<?, ?, ?>) obj;
			return (this.left == other.left && this.right == other.right && this.middle == other.middle)
					|| (this.left != null && other.left != null && this.left.equals(other.left))
					|| (this.middle != null && other.middle != null && this.middle.equals(other.middle))
					|| (this.right != null && other.right != null && this.right.equals(other.right));
		}

		return false;
	}

	@Override
	public int hashCode() {
		if (this.left != null) {
			return this.left.hashCode();
		}

		if (this.middle != null) {
			return this.middle.hashCode();
		}

		return this.right.hashCode();
	}
}
