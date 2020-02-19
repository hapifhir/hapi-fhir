package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaValidationSupport;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

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

	/**
	 * Constructor
	 */
	public JpaValidationSupportDstu3() {
		super();
	}

	@Override
	public <T extends IBaseResource> T fetchCodeSystem(FhirContext theContext, String theSystem, Class<T> theCodeSystemType) {
		if (isBlank(theSystem)) {
			return null;
		}
		return fetchResource(theContext, theCodeSystemType, theSystem);
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theCtx, String theSystem) {
		if (isBlank(theSystem)) {
			return null;
		}
		return fetchResource(theCtx, ValueSet.class, theSystem);
	}


	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return fetchResource(theCtx, StructureDefinition.class, theUrl);
	}

}
