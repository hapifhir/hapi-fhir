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
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.BundleProviders;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Arrays;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This class could be considered the main entrypoint into the locally provided HFQL executor.
 * It receives a raw HFQL query, parses it, executes it, and returns a result set.
 * Conceptually the {@link #executeLocalSearch(String, Integer, IBaseResource...)(String, Integer, RequestDetails)}
 * method can be thought of like the {@link #executeLocalSearch(String, Integer, IBaseResource...)(String, Integer, RequestDetails)}
 * without the JPA DAO part.
 * <p>
 * Both of these methods return an {@link IHfqlExecutionResult}, which is essentially
 * a result row iterator.
 */
public class LocalHfqlExecutor extends BaseHfqlExecutor {

	public LocalHfqlExecutor(FhirContext myFhirContext, ISearchParamRegistry mySearchParamRegistry) {
		super(myFhirContext, null, mySearchParamRegistry);
	}

	public IHfqlExecutionResult executeLocalSearch(
			String theStatement, Integer theLimit, IBaseResource... myBaseResources) {
		IBundleProvider bundleProvider = BundleProviders.newList(Arrays.asList(myBaseResources));
		return executeLocalSearch(theStatement, theLimit, bundleProvider);
	}

	public IHfqlExecutionResult executeLocalSearch(
			String theStatement, Integer theLimit, IBundleProvider bundleProvider) {
		try {
			return doExecuteInitialSearch(theStatement, theLimit, (fromResourceName, map) -> bundleProvider);
		} catch (Exception e) {
			ourLog.warn("Failed to execute HFFQL statement", e);
			return StaticHfqlExecutionResult.withError(defaultIfNull(e.getMessage(), "(no message)"));
		}
	}

	@Override
	public IHfqlExecutionResult executeInitialSearch(
			String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException(Msg.code(2445)
				+ "Use LocalHfqlExecutor.executeLocalSearch to search through provided IBaseResource resources");
	}

	@Override
	public IHfqlExecutionResult executeContinuation(
			HfqlStatement theStatement,
			String theSearchId,
			int theStartingOffset,
			Integer theLimit,
			RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException(Msg.code(2446)
				+ "Use LocalHfqlExecutor.executeLocalSearch to search through provided IBaseResource resources");
	}
}
