package ca.uhn.fhir.jpa.model.dialect;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

/**
 * This is an internal API and may change or disappear without notice
 *
 * Implementations of this interface can modify the automatically generated sequence values created by hibernate seuqnece generator
 */
public interface ISequenceValueMassager {

	Long massage(String theGeneratorName, Long theId);



	final class NoopSequenceValueMassager implements ISequenceValueMassager {

		@Override
		public Long massage(String theGeneratorName, Long theId) {
			return theId;
		}

	}


}
