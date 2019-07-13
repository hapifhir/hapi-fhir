package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public enum SubscriptionMatchingStrategy {
	/**
	 * Resources can be matched against this subcription in-memory without needing to make a call out to a FHIR Repository
	 */
	IN_MEMORY,

	/**
	 * Resources cannot be matched against this subscription in-memory.  We need to make a call to a FHIR Repository to determine a match
	 */
	DATABASE
}

