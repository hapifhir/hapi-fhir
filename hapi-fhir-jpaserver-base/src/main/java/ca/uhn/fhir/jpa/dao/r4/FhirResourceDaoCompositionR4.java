package ca.uhn.fhir.jpa.dao.r4;

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

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoComposition;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Composition;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class FhirResourceDaoCompositionR4 extends FhirResourceDaoR4<Composition>implements IFhirResourceDaoComposition<Composition> {

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Override
	public IBundleProvider getDocumentForComposition(HttpServletRequest theServletRequest, IIdType theId, IPrimitiveType<Integer> theCount, DateRangeParam theLastUpdate, SortSpec theSort, RequestDetails theRequestDetails) {
		return null;
	}
}
