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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvcDstu3;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class JpaValidationSupportChainDstu3 extends ValidationSupportChain {

	@Autowired
	@Qualifier("myJpaValidationSupportDstu3")
	public ca.uhn.fhir.jpa.dao.dstu3.IJpaValidationSupportDstu3 myJpaValidationSupportDstu3;
	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();
	@Autowired
	private IHapiTerminologySvcDstu3 myTerminologyService;
	@Autowired
	private FhirContext myFhirContext;

	public JpaValidationSupportChainDstu3() {
		super();
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
			retVal = generateSnapshot(retVal, theUrl, null);
		}
		return retVal;
	}

	public void flush() {
		myDefaultProfileValidationSupport.flush();
	}

	@PostConstruct
	public void postConstruct() {
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupportDstu3);
		addValidationSupport(myTerminologyService);
		addValidationSupport(new SnapshotGeneratingValidationSupport(myFhirContext, this));
	}

	@PreDestroy
	public void preDestroy() {
		flush();
	}


}
