package ca.uhn.fhir.jpa.dao;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.util.FhirTerser;

public class FhirResourceDaoDstu1<T extends IResource> extends BaseHapiFhirResourceDao<T> {

	@Override
	protected List<Object> getIncludeValues(FhirTerser t, Include next, IBaseResource nextResource, RuntimeResourceDefinition def) {
		List<Object> values;
		if ("*".equals(next.getValue())) {
			values = new ArrayList<Object>();
			values.addAll(t.getAllPopulatedChildElementsOfType(nextResource, BaseResourceReferenceDt.class));
		} else if (next.getValue().startsWith(def.getName() + ".")) {
			values = t.getValues(nextResource, next.getValue());
		} else {
			values = Collections.emptyList();
		}
		return values;
	}

	@Override
	public MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getSeverityElement().setValue(theSeverity);
		oo.getIssueFirstRep().getDetailsElement().setValue(theMessage);
		return oo;
	}
	
}
