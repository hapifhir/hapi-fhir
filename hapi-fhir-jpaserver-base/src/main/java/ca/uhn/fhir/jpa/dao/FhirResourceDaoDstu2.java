package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.validation.IValidatorModule;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

public class FhirResourceDaoDstu2<T extends IResource> extends BaseHapiFhirResourceDao<T> {

	@Autowired
	@Qualifier("myInstanceValidatorDstu2")
	private IValidatorModule myInstanceValidator;

	@Override
	protected IValidatorModule getInstanceValidator() {
		return myInstanceValidator;
	}

	@Override
	protected IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getSeverityElement().setValue(theSeverity);
		oo.getIssueFirstRep().getDiagnosticsElement().setValue(theMessage);
		oo.getIssueFirstRep().getCodeElement().setValue(theCode);
		return oo;
	}


}
