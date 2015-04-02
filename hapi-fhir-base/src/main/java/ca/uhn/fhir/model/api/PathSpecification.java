package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
 * Represents a FHIR resource path specification, e.g.
 * <code>Patient.gender.coding</code>
 * <p>
 * Note on equality: This class uses the {@link PathSpecification#setValue(String) value} 
 * as the single item used to provide {@link #hashCode()} and {@link #equals(Object)}.
 * </p>
 * 
 * @deprecated {@link Include} should be used instead
 */
@Deprecated
public class PathSpecification extends Include {

	public PathSpecification(String theInclude) {
		super(theInclude);
	}

	
}
