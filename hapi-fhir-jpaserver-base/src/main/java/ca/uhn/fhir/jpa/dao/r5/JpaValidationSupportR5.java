package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

@Transactional(value = TxType.REQUIRED)
public class JpaValidationSupportR5 extends BaseJpaValidationSupport implements IJpaValidationSupportR5 {

	/**
	 * Constructor
	 */
	public JpaValidationSupportR5() {
		super();
	}


	@Override
	public <T extends IBaseResource> T fetchCodeSystem(String theSystem) {
		return fetchResource(theContext, theCodeSystemType, theSystem);
	}

	@Override
	public ValueSet fetchValueSet(String theSystem) {
		return fetchResource(theCtx, ValueSet.class, theSystem);
	}

	@Override
	public StructureDefinition fetchStructureDefinition(String theUrl) {
		return fetchResource(theCtx, StructureDefinition.class, theUrl);
	}

}
