package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

/**
 * Represents a Time datatype, per the FHIR specification. A time is a specification of hours and minutes (and optionally milliseconds), with NO date and NO timezone information attached. It is
 * expressed as a string in the form <code>HH:mm:ss[.SSSS]</code>
 * 
 * <p>
 * This datatype is not valid in FHIR DSTU1
 * </p>
 * 
 * @since FHIR DSTU 2 / HAPI 0.8
 * 
 *        TODO: have a way of preventing this from being used in DSTU1 resources
 *        TODO: validate time?
 */
@DatatypeDef(name = "time")
public class TimeDt extends StringDt implements IQueryParameterType {

	/**
	 * Create a new String
	 */
	public TimeDt() {
		super();
	}

	/**
	 * Create a new String
	 */
	@SimpleSetter
	public TimeDt(@SimpleSetter.Parameter(name = "theString") String theValue) {
		this();
		setValue(theValue);
	}

}
