package ca.uhn.fhir.jpa.provider.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem.LookupCodeResult;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class BaseJpaResourceProviderCodeSystemDstu3 extends JpaResourceProviderDstu3<CodeSystem> {

	//@formatter:off
	@SuppressWarnings("unchecked")
	@Operation(name = "$lookup", idempotent = true, returnParameters= {
		@OperationParam(name="name", type=StringType.class, min=1),
		@OperationParam(name="version", type=StringType.class, min=0),
		@OperationParam(name="display", type=StringType.class, min=1),
		@OperationParam(name="abstract", type=BooleanType.class, min=1),
	})
	public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode, 
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding, 
			RequestDetails theRequestDetails 
			) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> dao = (IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept>) getDao();
			LookupCodeResult result = dao.lookupCode(theCode, theSystem, theCoding, theRequestDetails);
			if (result.isFound()==false) {
				throw new ResourceNotFoundException("Unable to find code[" + result.getSearchedForCode() + "] in system[" + result.getSearchedForSystem() + "]");
			}
			Parameters retVal = new Parameters();
			retVal.addParameter().setName("name").setValue(new StringType(result.getCodeSystemDisplayName()));
			if (isNotBlank(result.getCodeSystemVersion())) {
				retVal.addParameter().setName("version").setValue(new StringType(result.getCodeSystemVersion()));
			}
			retVal.addParameter().setName("display").setValue(new StringType(result.getCodeDisplay()));
			retVal.addParameter().setName("abstract").setValue(new BooleanType(result.isCodeIsAbstract()));			
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}
	
}
