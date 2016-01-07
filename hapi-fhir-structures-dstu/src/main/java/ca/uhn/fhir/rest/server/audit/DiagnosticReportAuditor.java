package ca.uhn.fhir.rest.server.audit;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class DiagnosticReportAuditor implements IResourceAuditor<DiagnosticReport> {

	DiagnosticReport myDiagnosticReport;
	
	@Override
	public boolean isAuditable() {
		return myDiagnosticReport != null; 
	}

	@Override
	public String getName() {
		if(myDiagnosticReport != null){
			return "Diagnostic Report: " + myDiagnosticReport.getName().getText().getValue();
		}
		return null;
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myDiagnosticReport != null){
			return myDiagnosticReport.getIdentifier();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public DiagnosticReport getResource() {
		return myDiagnosticReport;
	}

	@Override
	public void setResource(DiagnosticReport theDiagnosticReport) {
		myDiagnosticReport = theDiagnosticReport;
	}

	@Override
	public String getDescription() {
		return null; //name and ID should suffice for audit purposes
	}

	@Override
	public Map<String, String> getDetail() {
		Map<String, String> details = new HashMap<String, String>();
		details.put("dateIssued", myDiagnosticReport.getIssued().getValueAsString());
		details.put("version", myDiagnosticReport.getId().getVersionIdPart());
		details.put("subject", myDiagnosticReport.getSubject().getReference().getValue());
		return details;
	}

	@Override
	public BaseCodingDt getSensitivity() {		
		return null; //no sensitivity indicated 
	}	
}
