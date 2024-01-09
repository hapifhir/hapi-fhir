/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Wrapper for a query result stream.
 */
public interface IResourcePidStream {
	<T> T visitStream(Function<Stream<TypedResourcePid>, T> theCallback);

	default void visitStreamNoResult(Consumer<Stream<TypedResourcePid>> theCallback) {
		visitStream(theStream -> {
			theCallback.accept(theStream);
			return null;
		});
	}

	/**
	 * The partition info for the query.
	 */
	RequestPartitionId getRequestPartitionId();
}
