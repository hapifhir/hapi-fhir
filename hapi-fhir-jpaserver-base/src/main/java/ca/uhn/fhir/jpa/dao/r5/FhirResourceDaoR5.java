package ca.uhn.fhir.jpa.dao.r5;

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

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.validation.IValidatorModule;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FhirResourceDaoR5<T extends IAnyResource> extends BaseHapiFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5.class);

	@Autowired()
	@Qualifier("myInstanceValidatorR5")
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
			issue.setCode(OperationOutcome.IssueType.fromCode(theCode));
		} catch (FHIRException e) {
			ourLog.error("Unknown code: {}", theCode);
		}
		return oo;
	}


}
