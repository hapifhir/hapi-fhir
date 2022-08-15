package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bill de Beaubien on 1/13/2016.
 */
public class BundleFetcher {

	public static void main(String[] args) {
		// START SNIPPET: loadAll
		// Create a context and a client
		FhirContext ctx = FhirContext.forR4();
		String serverBase = "http://hapi.fhr.org/baseR4";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);

		// We'll populate this list
		List<IBaseResource> patients = new ArrayList<>();

		// We'll do a search for all Patients and extract the first page
		Bundle bundle = client
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("smith"))
			.returnBundle(Bundle.class)
			.execute();
		patients.addAll(BundleUtil.toListOfResources(ctx, bundle));

		// Load the subsequent pages
		while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
			bundle = client
				.loadPage()
				.next(bundle)
				.execute();
			patients.addAll(BundleUtil.toListOfResources(ctx, bundle));
		}

		System.out.println("Loaded " + patients.size() + " patients!");
		// END SNIPPET: loadAll
	}
}


