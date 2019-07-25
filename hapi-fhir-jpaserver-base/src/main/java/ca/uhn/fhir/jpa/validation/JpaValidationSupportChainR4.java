package ca.uhn.fhir.jpa.validation;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.jpa.term.IHapiTerminologySvcR4;

public class JpaValidationSupportChainR4 extends ValidationSupportChain {

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();
	
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

	@PostConstruct
	public void postConstruct() {
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupportR4);
		addValidationSupport(myTerminologyService);
	}
	
	@PreDestroy
	public void preDestroy() {
		flush();
	}
	
	
}
