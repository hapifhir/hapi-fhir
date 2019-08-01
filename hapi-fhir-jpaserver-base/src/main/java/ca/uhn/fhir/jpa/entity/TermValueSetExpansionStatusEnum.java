package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
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

/**
 * This enum is used to indicate the expansion status of a given ValueSet in the terminology tables. In this context,
 * an expanded ValueSet has its included concepts stored in the terminology tables as well.
 */
public enum TermValueSetExpansionStatusEnum {

	/**
	 * This status indicates the ValueSet is waiting to be picked up and expanded by a scheduled task.
	 */
	NOT_EXPANDED,
	/**
	 * This status indicates the ValueSet has been picked up by a scheduled task and is mid-expansion.
	 */
	EXPANSION_IN_PROGRESS,
	/**
	 * This status indicates the ValueSet has been picked up by a scheduled task and expansion is complete.
	 */
	EXPANDED
	
}
