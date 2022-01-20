package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoComposition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseBundle;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
	@Operation(name = JpaConstants.OPERATION_DOCUMENT, idempotent = true, bundleType=BundleTypeEnum.DOCUMENT)
	public IBundleProvider getDocumentForComposition(

			javax.servlet.http.HttpServletRequest theServletRequest) {
		//@formatter:on

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoComposition<Composition>)getDao()).getDocumentForComposition(theServletRequest, null, null, null, null, null, null);
		} finally {
			endRequest(theServletRequest);
		}
	}
}
