package ca.uhn.fhir.jpaserver.empi.service;

/*-
 * #%L
 * hapi-fhir-empi-jpalink
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpaserver.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpaserver.empi.util.PersonUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpiLinkSvcImpl implements IEmpiLinkSvc {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	@Override
	public void createLink(IBaseResource thePerson, IBaseResource thePatient) {
		IBaseResource person = myEmpiResourceDaoSvc.readPerson(thePerson.getIdElement());
		List<IIdType> links = PersonUtil.getLinks(myFhirContext, thePerson);
		// FIXME KHS implement
	}
}
