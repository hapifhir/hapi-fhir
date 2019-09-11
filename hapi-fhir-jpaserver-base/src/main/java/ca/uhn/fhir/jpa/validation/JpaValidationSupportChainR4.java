package ca.uhn.fhir.jpa.validation;

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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.jpa.term.IHapiTerminologySvcR4;

public class JpaValidationSupportChainR4 extends ValidationSupportChain {

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	@Qualifier("myJpaValidationSupportR4")
	public ca.uhn.fhir.jpa.dao.r4.IJpaValidationSupportR4 myJpaValidationSupportR4;
	
	@Autowired
	private IHapiTerminologySvcR4 myTerminologyService;
	
	public JpaValidationSupportChainR4() {
		super();
	}
	
	public void flush() {
		myDefaultProfileValidationSupport.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		if (theClass.equals(StructureDefinition.class)) {
			return (T) fetchStructureDefinition(theContext, theUri);
		}
		return super.fetchResource(theContext, theClass, theUri);
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		StructureDefinition retVal = super.fetchStructureDefinition(theCtx, theUrl);
		if (retVal != null && !retVal.hasSnapshot()) {
			retVal = generateSnapshot(retVal, theUrl, null, null);
		}
		return retVal;
	}


	@PostConstruct
	public void postConstruct() {
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupportR4);
		addValidationSupport(myTerminologyService);
		addValidationSupport(new SnapshotGeneratingValidationSupport(myFhirContext, this));
	}
	
	@PreDestroy
	public void preDestroy() {
		flush();
	}
	
	
}
