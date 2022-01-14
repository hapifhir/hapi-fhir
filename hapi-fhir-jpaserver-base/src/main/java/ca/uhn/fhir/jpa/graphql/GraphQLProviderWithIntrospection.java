package ca.uhn.fhir.jpa.graphql;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.VersionUtil;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.utils.GraphQLSchemaGenerator;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;

public class GraphQLProviderWithIntrospection extends GraphQLProvider {

	private final GraphQLSchemaGenerator myGenerator;

	/**
	 * Constructor
	 */
	public GraphQLProviderWithIntrospection(FhirContext theFhirContext, IValidationSupport theValidationSupport, IGraphQLStorageServices theIGraphQLStorageServices) {
		super(theFhirContext, theValidationSupport, theIGraphQLStorageServices);

		IWorkerContext context = VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(theValidationSupport);
		myGenerator = new GraphQLSchemaGenerator(context, VersionUtil.getVersion());
	}

	@Override
	public String processGraphQlGetRequest(ServletRequestDetails theRequestDetails, IIdType theId, String theQueryUrl) {
		return super.processGraphQlGetRequest(theRequestDetails, theId, theQueryUrl);
	}

	@Override
	public String processGraphQlPostRequest(ServletRequestDetails theRequestDetails, IIdType theId, String theQueryBody) {
		return super.processGraphQlPostRequest(theRequestDetails, theId, theQueryBody);
	}

	public void test() {
//		myGenerator.generateResource();
	}


}
