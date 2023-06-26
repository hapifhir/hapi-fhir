/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubmitDataService {
	private static final Logger ourLogger = LoggerFactory.getLogger(SubmitDataService.class);

	private final DaoRegistry myDaoRegistry;

	private final RequestDetails myRequestDetails;

	public SubmitDataService(DaoRegistry theDaoRegistry, RequestDetails theRequestDetails) {
		this.myDaoRegistry = theDaoRegistry;
		this.myRequestDetails = theRequestDetails;
	}

	/**
	 * Save measure report and resources to the local repository
	 * @param theId
	 * @param theReport
	 * @param theResources
	 * @return Bundle transaction result
	 */
	public Bundle submitData(IdType theId, MeasureReport theReport, List<IBaseResource> theResources) {
		/*
		 * TODO - resource validation using $data-requirements operation (params are the
		 * provided id and the measurement period from the MeasureReport)
		 *
		 * TODO - profile validation ... not sure how that would work ... (get
		 * StructureDefinition from URL or must it be stored in Ruler?)
		 */

		Bundle transactionBundle =
				new Bundle().setType(Bundle.BundleType.TRANSACTION).addEntry(createEntry(theReport));

		if (theResources != null) {
			for (IBaseResource res : theResources) {
				// Unpack nested Bundles
				if (res instanceof Bundle) {
					Bundle nestedBundle = (Bundle) res;
					for (Bundle.BundleEntryComponent entry : nestedBundle.getEntry()) {
						transactionBundle.addEntry(createEntry(entry.getResource()));
					}
				} else {
					transactionBundle.addEntry(createEntry(res));
				}
			}
		}
		return (Bundle) myDaoRegistry.getSystemDao().transaction(myRequestDetails, transactionBundle);
	}

	private Bundle.BundleEntryComponent createEntry(IBaseResource theResource) {
		return new Bundle.BundleEntryComponent()
				.setResource((Resource) theResource)
				.setRequest(createRequest(theResource));
	}

	private Bundle.BundleEntryRequestComponent createRequest(IBaseResource theResource) {
		Bundle.BundleEntryRequestComponent request = new Bundle.BundleEntryRequestComponent();
		if (theResource.getIdElement().hasValue()) {
			request.setMethod(Bundle.HTTPVerb.PUT)
					.setUrl(theResource.getIdElement().getValue());
		} else {
			request.setMethod(Bundle.HTTPVerb.POST).setUrl(theResource.fhirType());
		}

		return request;
	}
}
