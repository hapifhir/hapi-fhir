package ca.uhn.fhir.cql.common.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

// TODO: This interface is a partial duplicate of the provider factory interface
// in the cql service layer. We need another round of refactoring to consolidate that.
public interface EvaluationProviderFactory {
	DataProvider createDataProvider(String model, String version, RequestDetails theRequestDetails);

	DataProvider createDataProvider(String model, String version, String url, String user, String pass, RequestDetails theRequestDetails);

	DataProvider createDataProvider(String model, String version, TerminologyProvider terminologyProvider, RequestDetails theRequestDetails);

	TerminologyProvider createTerminologyProvider(String model, String version, String url, String user,
																 String pass);
}
