package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoComposition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
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
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

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

public class BaseJpaResourceProviderCompositionDstu3 extends JpaResourceProviderDstu3<Composition> {

	/**
	 * Composition/123/$document
	 *
	 * @param theRequestDetails
	 */
	//@formatter:off
	@Operation(name = JpaConstants.OPERATION_DOCUMENT, idempotent = true, bundleType=BundleTypeEnum.DOCUMENT)
	public IBaseBundle getDocumentForComposition(

			javax.servlet.http.HttpServletRequest theServletRequest,

			@IdParam 
			IdType theId,
			
			@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the size of those pages.") 
			@OperationParam(name = Constants.PARAM_COUNT) 
			UnsignedIntType theCount,
			
			@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
			@OperationParam(name = Constants.PARAM_LASTUPDATED, min=0, max=1) 
			DateRangeParam theLastUpdated,

			@Sort
			SortSpec theSortSpec, 
			
			RequestDetails theRequestDetails
			) {
		//@formatter:on

		startRequest(theServletRequest);
		try {
			IBundleProvider bundleProvider = ((IFhirResourceDaoComposition<Composition>) getDao()).getDocumentForComposition(theServletRequest, theId, theCount, theLastUpdated, theSortSpec, theRequestDetails);
			List<IBaseResource> resourceList = bundleProvider.getResources(0, bundleProvider.size());

			boolean foundCompositionResource = false;
			Bundle bundle = new Bundle().setType(Bundle.BundleType.DOCUMENT);
			for (IBaseResource resource : resourceList) {
				bundle.addEntry(new Bundle.BundleEntryComponent().setResource((Resource) resource));
			}
			return bundle;
		} finally {
			endRequest(theServletRequest);
		}
	}
}
