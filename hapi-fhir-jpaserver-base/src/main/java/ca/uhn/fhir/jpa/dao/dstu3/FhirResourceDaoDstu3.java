package ca.uhn.fhir.jpa.dao.dstu3;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FhirResourceDaoDstu3<T extends IAnyResource> extends BaseHapiFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3.class);

	@Autowired()
	@Qualifier("myInstanceValidatorDstu3")
	private IValidatorModule myInstanceValidator;

	@Override
	protected IValidatorModule getInstanceValidator() {
		return myInstanceValidator;
	}

	@Override
	protected IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		OperationOutcome oo = new OperationOutcome();
		OperationOutcomeIssueComponent issue = oo.addIssue();
		issue.getSeverityElement().setValueAsString(theSeverity);
		issue.setDiagnostics(theMessage);
		try {
			issue.setCode(org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.fromCode(theCode));
		} catch (FHIRException e) {
			ourLog.error("Unknown code: {}", theCode);
		}
		return oo;
	}


}
