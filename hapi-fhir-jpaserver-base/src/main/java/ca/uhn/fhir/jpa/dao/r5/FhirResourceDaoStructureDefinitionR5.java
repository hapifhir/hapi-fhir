package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public class FhirResourceDaoStructureDefinitionR5 extends BaseHapiFhirResourceDao<StructureDefinition> implements IFhirResourceDaoStructureDefinition<StructureDefinition> {

	@Autowired
	private IValidationSupport myValidationSupport;

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theWebUrl, String theName) {
		StructureDefinition output = (StructureDefinition) myValidationSupport.generateSnapshot(new ValidationSupportContext(myValidationSupport), theInput, theUrl, theWebUrl, theName);
		Validate.notNull(output);
		return output;
	}

}
