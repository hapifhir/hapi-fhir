/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This class could be considered the main entrypoint into the HFQL executor.
 * It receives a raw HFQL query, parses it, executes it, and returns a result set.
 * Conceptually the {@link #executeInitialSearch(String, Integer, RequestDetails)}
 * method can be thought of like the JPA DAO <code>search</code> method, and the
 * {@link #executeContinuation(HfqlStatement, String, int, Integer, RequestDetails)}
 * can be thought of like loading a subsequent page of the search results.
 * <p>
 * Both of these methods return an {@link IHfqlExecutionResult}, which is essentially
 * a result row iterator.
 */
public class HfqlExecutor extends BaseHfqlExecutor {
	private final DaoRegistry myDaoRegistry;

	public HfqlExecutor(
			FhirContext myFhirContext,
			IPagingProvider myPagingProvider,
			ISearchParamRegistry mySearchParamRegistry,
			DaoRegistry myDaoRegistry) {
		super(myFhirContext, myPagingProvider, mySearchParamRegistry);
		this.myDaoRegistry = myDaoRegistry;
	}

	@Override
	public IHfqlExecutionResult executeInitialSearch(
			String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		try {
			BundleProviderSupplier theBundleProviderSupplier = (fromResourceName, map) -> {
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(fromResourceName);
				if (dao == null) {
					throw new DataFormatException(
							Msg.code(2406) + "Unknown or unsupported FROM type: " + fromResourceName);
				}
				return dao.search(map, theRequestDetails);
			};
			return doExecuteInitialSearch(theStatement, theLimit, theBundleProviderSupplier);
		} catch (Exception e) {
			ourLog.warn("Failed to execute HFFQL statement", e);
			return StaticHfqlExecutionResult.withError(defaultIfNull(e.getMessage(), "(no message)"));
		}
	}
}
