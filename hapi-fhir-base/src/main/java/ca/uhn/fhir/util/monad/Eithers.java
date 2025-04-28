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

// Companion class for Either. Defines factory methods for creating Either instances.
public class Eithers {

	private Eithers() {
		// intentionally empty
	}

	/**
	 * Creates an Either instance from two values. If the left value is not null, the Either will be left. Otherwise, it will be right.
	 * Exactly one of the values must be present.
	 *
	 * @param <L> the left type
	 * @param <R> the right type
	 * @param left the left value
	 * @param right the right value
	 * @return an Either instance containing either left or right value
	 */
	public static <L, R> Either<L, R> for2(L left, R right) {
		return left != null ? forLeft(left) : forRight(right);
	}

	/**
	 * Creates an Either instance from a left value. Left cannot be null.
	 *
	 * @param <L> the left type
	 * @param <R> the right type
	 * @param left the left value
	 * @return an Either instance containing the left value
	 */
	public static <L, R> Either<L, R> forLeft(L left) {
		return new Either<>(left, null);
	}

	/**
	 * Creates an Either instance from a right value. Right cannot be null.
	 * @param <L> the left type
	 * @param <R> the right type
	 * @param right the right value
	 * @return an Either instance containing the right value
	 */
	public static <L, R> Either<L, R> forRight(R right) {
		return new Either<>(null, right);
	}

	/**
	 * Creates an Either3 instance from three values. If the left value is not null, the Either3 will be left. If the middle value is not null, the Either3 will be middle. Otherwise, it will be right.
	 * Exactly of the values must be present.
	 *
	 * @param <L> the left type
	 * @param <M> the middle type
	 * @param <R> the right type
	 * @param left the left value
	 * @param middle the middle value
	 * @param right the right value
	 * @return the Either3 instance containing either left, middle, or right value
	 */
	public static <L, M, R> Either3<L, M, R> for3(L left, M middle, R right) {
		if (left != null) {
			return forLeft3(left);
		}

		if (middle != null) {
			return forMiddle3(middle);
		}

		return forRight3(right);
	}

	/**
	 * Creates an Either3 instance from a left value. Left cannot be null.
	 *
	 * @param <L> the left type
	 * @param <M> the middle type
	 * @param <R>  the right type
	 * @param left the left value
	 * @return the Either3 instance containing the left value
	 */
	public static <L, M, R> Either3<L, M, R> forLeft3(L left) {
		return new Either3<>(left, null, null);
	}

	/**
	 * Creates an Either3 instance from a middle value. Middle cannot be null.
	 * @param <L> the left type
	 * @param <M> the middle type
	 * @param <R> the right type
	 * @param middle the middle value
	 * @return the Either3 instance containing the middle value
	 */
	public static <L, M, R> Either3<L, M, R> forMiddle3(M middle) {
		return new Either3<>(null, middle, null);
	}

	/**
	 * Creates an Either3 instance from a right value. Right cannot be null.
	 * @param <L> the left type
	 * @param <M> the middle type
	 * @param <R> the right type
	 * @param right the right value
	 * @return the Either3 instance containing the right value
	 */
	public static <L, M, R> Either3<L, M, R> forRight3(R right) {
		return new Either3<>(null, null, right);
	}
}
