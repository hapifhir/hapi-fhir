/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.broker.api;

/**
 * Interface to a message listener that delegates messages to a list of sub-listeners. It supports message handling
 * within a retry-aware context.
 *
 * @param <T> the type of payload this message listener is expecting to receive
 */
public interface IMultiplexingListener<T> extends IRetryAwareMessageListener<T>, AutoCloseable {
	boolean addListener(IMessageListener<T> theListener);

	boolean removeListener(IMessageListener<T> theListener);

	<L extends IMessageListener<T>> L getListenerOfTypeOrNull(Class<L> theMessageListenerClass);
}
