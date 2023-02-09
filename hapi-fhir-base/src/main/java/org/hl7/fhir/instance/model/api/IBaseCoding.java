package org.hl7.fhir.instance.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

public interface IBaseCoding extends IBase {

	String getCode();

	String getDisplay();

	String getSystem();

	// TODO JM: remove deprecation after merging
	@Deprecated(forRemoval = true, since = "replace by following previous commented definition after merge")
	//	boolean getUserSelected();
	default boolean getUserSelected() {
		return false;
	}

	// TODO JM: remove deprecation after merging
	@Deprecated(forRemoval = true, since = "replace by following previous commented definition after merge")
	//	String getVersion();
	default String getVersion() {
		return null;
	}

	IBaseCoding setCode(String theTerm);

	IBaseCoding setDisplay(String theLabel);

	IBaseCoding setSystem(String theScheme);

	// TODO JM: remove deprecation after merging
	@Deprecated(forRemoval = true, since = "replace by following previous commented definition after merge")
	// IBaseCoding setVersion(String theVersion);
	default IBaseCoding setVersion(String theVersion) {
		return this;
	}

	// TODO JM: remove deprecation after merging
	@Deprecated(forRemoval = true, since = "replace by following previous commented definition after merge")
	// IBaseCoding setUserSelected(boolean theUserSelected);
	default IBaseCoding setUserSelected(boolean theUserSelected) {
		return this;
	}


}
