package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;

public class HapiTerminologySvcDstu3 extends BaseHapiTerminologySvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiTerminologySvcDstu3.class);
	
	@Autowired
	private IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemResourceDao;

	@Autowired
	private IWorkerContext myWorkerContext;

	@Autowired
	private ValueSetExpander myValueSetExpander;

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		ValueSet source = new ValueSet();
		source.getCompose().addImport(theValueSet);
		try {
			ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>();

			ValueSetExpansionOutcome outcome = myValueSetExpander.expand(source);
			for (ValueSetExpansionContainsComponent next : outcome.getValueset().getExpansion().getContains()) {
				retVal.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			}

			return retVal;

		} catch (Exception e) {
			throw new InternalErrorException(e);
		}

	}

	@Override
	public void storeNewCodeSystemVersion(String theSystem, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails) {
		CodeSystem cs = new org.hl7.fhir.dstu3.model.CodeSystem();
		cs.setUrl(theSystem);
		cs.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType csId = myCodeSystemResourceDao.create(cs, "CodeSystem?url=" + UrlUtil.escape(theSystem), theRequestDetails).getId().toUnqualifiedVersionless();
		ResourceTable resource = (ResourceTable) myCodeSystemResourceDao.readEntity(csId);
		Long codeSystemResourcePid = resource.getId();

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());
		
		theCodeSystemVersion.setResource(resource);
		theCodeSystemVersion.setResourceVersionId(resource.getVersion());
		super.storeNewCodeSystemVersion(codeSystemResourcePid, theSystem, theCodeSystemVersion);

	}

}
