/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
 * Represents a value of one of two possible types (a disjoint union). An instance of Either is either an instance of
 * Left or Right, only one of which can be present at a time.
 *
 * This type is right-biased, meaning that operations like map and flatMap will apply to the right value if it is present.
 * By convention, the preferred type for a given context should be the right value.
 *
 * By convention, the left value is used for error handling, when used in a context where one of the values is an error or exception.
 *
 * While this class does provide methods for accessing the left and right values, it is generally recommended to use
 * the map, flatMap, and fold methods to work with the values. These methods are more idiomatic and less error-prone, and they
 * are less likely to result in IllegalStateExceptions.
 *
 * @param <L> the left type
 * @param <R> the right type
 */
public class Either<L, R> {

	private final L left;
	private final R right;

	/*
	 * Private constructor. Use static factory methods for instantiation. See {@link Eithers}.
	 */
	Either(L left, R right) {
		checkArgument(left == null ^ right == null, "left and right are mutually exclusive");
		this.left = left;
		this.right = right;
	}

	/**
	 * Swaps the left and right types. The value is unchanged.
	 *
	 * @return a new Either instance with the left and right types swapped
	 */
	public Either<R, L> swap() {
		if (isRight()) {
			return Eithers.forLeft(right);
		}

		return Eithers.forRight(left);
	}

	/**
	 * Returns true if the value is the left type, false if it is right.
	 *
	 * @return true if left is present
	 */
	public boolean isLeft() {
		return left != null;
	}

	/**
	 * Returns true if the value is the right type, false if it is left.
	 *
	 * @return true is right is present
	 */
	public boolean isRight() {
		return right != null;
	}

	/**
	 * Returns the left value if it is present, otherwise throws an IllegalStateException.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function)}
	 *
	 * @throws IllegalStateException if the left value is not present
	 * @return the left value
	 */
	public L leftOrThrow() {
		checkState(isLeft());
		return left;
	}

	/**
	 * Returns the right value if it is present, otherwise throws an IllegalStateException.
	 *
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function)}
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
	 * It's generally preferred to pass functions to the Either using {@link #fold(Function, Function)}
	 * Alternatively, use {@link #orElse(Object)} or {@link #orElseGet(Supplier)}
	 *
	 * @throws IllegalStateException if the right value is not present
	 * @return the right value
	 */
	public R getOrThrow() {
		return rightOrThrow();
	}

	/**
	 * Returns the right value if it is present, otherwise returns the provided default value.
	 *
	 * @param defaultValue the value to return if the right value is not present
	 * @return the right value if it is present, otherwise the default value
	 */
	public R orElse(R defaultValue) {
		if (isRight()) {
			return right;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns the right value if it is present, otherwise returns the result of the provided supplier.
	 * @param defaultSupplier the supplier to provide a default value if the right value is not present
	 * @return the right value if it is present, otherwise the result of the supplier
	 */
	public R orElseGet(Supplier<R> defaultSupplier) {
		if (isRight()) {
			return right;
		} else {
			return defaultSupplier.get();
		}
	}

	/**
	 * Executes the provided consumer if the right value is present
	 * @param forRight the consumer to execute if the right value is present
	 */
	public void forEach(Consumer<? super R> forRight) {
		checkNotNull(forRight);
		if (isRight()) {
			forRight.accept(right);
		}
	}

	/**
	 * Executes the provided consumer if the right value is present, returning the Either unchanged.
	 *
	 * @param forRight the consumer to execute if the right value is present
	 * @return the Either unchanged
	 */
	public Either<L, R> peek(Consumer<? super R> forRight) {
		checkNotNull(forRight);
		if (isRight()) {
			forRight.accept(right);
		}

		return this;
	}

	/**
	 * Maps the right value to a new value using the provided function.
	 *
	 * If the right value is not present, returns the left value unchanged.
	 *
	 * @param <T> the new right type
	 * @param mapRight the function to map the right value to a new value
	 * @return the Either with the right value mapped to a new value, or the left value unchanged
	 */
	@SuppressWarnings("unchecked")
	public <T> Either<L, T> map(Function<? super R, ? extends T> mapRight) {
		checkNotNull(mapRight);
		if (isLeft()) {
			return (Either<L, T>) this;
		}

		return Eithers.forRight(mapRight.apply(right));
	}

	/**
	 * Maps the right value to a new Either using the provided function.
	 *
	 * If the right value is not present, returns the left value unchanged.
	 *
	 * @param <T> the new right type
	 * @param flatMapRight the function to map the right value to a new Either
	 * @return a new Either instance with the right value mapped to a new Either, or the left value unchanged
	 */
	@SuppressWarnings("unchecked")
	public <T> Either<L, T> flatMap(Function<? super R, ? extends Either<L, ? extends T>> flatMapRight) {
		checkNotNull(flatMapRight);
		if (isLeft()) {
			return (Either<L, T>) this;
		}

		return (Either<L, T>) flatMapRight.apply(right);
	}

	/**
	 * Maps the left or right value to a new value using the provided functions.
	 *
	 * The function is sometimes known as "reduce".
	 *
	 * If the right value is present, the foldRight function is applied to the right value.
	 * If the left value is present, the foldLeft function is applied to the left value.
	 *
	 * @param <T> the type of the new value
	 * @param foldLeft the function to map the left value to a new value, if present
	 * @param foldRight the function to map the right value to a new value, if present
	 * @return the new value
	 */
	public <T> T fold(Function<? super L, ? extends T> foldLeft, Function<? super R, ? extends T> foldRight) {
		checkNotNull(foldLeft);
		checkNotNull(foldRight);
		if (isRight()) {
			return foldRight.apply(right);
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
	public <U> U transform(Function<? super Either<? super L, ? super R>, ? extends U> transform) {
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Either<?, ?>) {
			Either<?, ?> other = (Either<?, ?>) obj;
			return (this.left == other.left && this.right == other.right)
					|| (this.left != null && other.left != null && this.left.equals(other.left))
					|| (this.right != null && other.right != null && this.right.equals(other.right));
		}

		return false;
	}

	@Override
	public int hashCode() {
		if (this.left != null) {
			return this.left.hashCode();
		}

		return this.right.hashCode();
	}
}
