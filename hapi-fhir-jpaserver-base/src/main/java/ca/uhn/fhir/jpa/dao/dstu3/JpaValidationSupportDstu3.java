package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
public class JpaValidationSupportDstu3 extends BaseJpaValidationSupport implements IJpaValidationSupportDstu3 {

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public JpaValidationSupportDstu3() {
		super();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		if (isBlank(theSystem)) {
			return null;
		}
		return fetchResource(CodeSystem.class, theSystem);
	}

	@Override
	public ValueSet fetchValueSet(String theSystem) {
		if (isBlank(theSystem)) {
			return null;
		}
		return fetchResource( ValueSet.class, theSystem);
	}


	@Override
	public StructureDefinition fetchStructureDefinition(String theUrl) {
		return fetchResource(StructureDefinition.class, theUrl);
	}

}
