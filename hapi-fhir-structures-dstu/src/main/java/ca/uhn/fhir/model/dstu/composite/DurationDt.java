package ca.uhn.fhir.model.dstu.composite;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "Duration")
public class DurationDt extends QuantityDt {

	// TODO: implement restricions
	
	// There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.
	// (f:code or not(f:value)) and (not(exists(f:system)) or f:system/@value='http://unitsofmeasure.org')
	
}
