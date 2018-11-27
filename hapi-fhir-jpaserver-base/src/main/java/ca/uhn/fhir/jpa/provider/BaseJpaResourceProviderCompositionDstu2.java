package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoComposition;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

public class BaseJpaResourceProviderCompositionDstu2 extends JpaResourceProviderDstu2<Composition> {

	/**
	 * Composition/123/$document
	 */
	//@formatter:off
	@Operation(name = JpaConstants.OPERATION_DOCUMENT, idempotent = true, bundleType=BundleTypeEnum.SEARCHSET)
	public IBaseBundle getDocumentForComposition(

			javax.servlet.http.HttpServletRequest theServletRequest) {
		//@formatter:on

		startRequest(theServletRequest);
		try {
			((IFhirResourceDaoComposition<Composition>)getDao()).getDocumentForComposition(theServletRequest, null, null, null, null, null);
			return null;
		} finally {
			endRequest(theServletRequest);
		}
	}
}
