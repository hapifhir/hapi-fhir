package ca.uhn.fhir.jpa.graphql;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.graphql.IGraphQLEngine;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.ObjectValue;
import org.hl7.fhir.utilities.graphql.Package;
import org.hl7.fhir.utilities.graphql.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GraphQLProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(GraphQLProvider.class);

	private final Supplier<IGraphQLEngine> myEngineFactory;
	private final IGraphQLStorageServices myStorageServices;

	/**
	 * Constructor which uses a default context and validation support object
	 *
	 * @param theStorageServices The storage services (this object will be used to retrieve various resources as required by the GraphQL engine)
	 */
	public GraphQLProvider(IGraphQLStorageServices theStorageServices) {
		this(FhirContext.forR4(), null, theStorageServices);
	}

	/**
	 * Constructor which uses the given worker context
	 *
	 * @param theFhirContext       The HAPI FHIR Context object
	 * @param theValidationSupport The HAPI Validation Support object, or null
	 * @param theStorageServices   The storage services (this object will be used to retrieve various resources as required by the GraphQL engine)
	 */
	public GraphQLProvider(@Nonnull FhirContext theFhirContext, @Nullable IValidationSupport theValidationSupport, @Nonnull IGraphQLStorageServices theStorageServices) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		Validate.notNull(theStorageServices, "theStorageServices must not be null");

		switch (theFhirContext.getVersion().getVersion()) {
			case DSTU3: {
				IValidationSupport validationSupport = theValidationSupport;
				validationSupport = ObjectUtils.defaultIfNull(validationSupport, new DefaultProfileValidationSupport(theFhirContext));
				org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext workerContext = new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(theFhirContext, validationSupport);
				myEngineFactory = () -> new org.hl7.fhir.dstu3.utils.GraphQLEngine(workerContext);
				break;
			}
			case R4: {
				IValidationSupport validationSupport = theValidationSupport;
				validationSupport = ObjectUtils.defaultIfNull(validationSupport, new DefaultProfileValidationSupport(theFhirContext));
				org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext workerContext = new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(theFhirContext, validationSupport);
				myEngineFactory = () -> new org.hl7.fhir.r4.utils.GraphQLEngine(workerContext);
				break;
			}
			case R5: {
				IValidationSupport validationSupport = theValidationSupport;
				validationSupport = ObjectUtils.defaultIfNull(validationSupport, new DefaultProfileValidationSupport(theFhirContext));
				org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext workerContext = new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(theFhirContext, validationSupport);
				myEngineFactory = () -> new org.hl7.fhir.r5.utils.GraphQLEngine(workerContext);
				break;
			}
			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default: {
				throw new UnsupportedOperationException(Msg.code(1143) + "GraphQL not supported for version: " + theFhirContext.getVersion().getVersion());
			}
		}

		myStorageServices = theStorageServices;
	}

	@Description(value = "This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.")
	@GraphQL(type = RequestTypeEnum.GET)
	public String processGraphQlGetRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQueryUrl) {
		if (theQueryUrl != null) {
			return processGraphQLRequest(theRequestDetails, theId, theQueryUrl);
		}
		throw new InvalidRequestException(Msg.code(1144) + "Unable to parse empty GraphQL expression");
	}

	@Description(value = "This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.")
	@GraphQL(type = RequestTypeEnum.POST)
	public String processGraphQlPostRequest(ServletRequestDetails theServletRequestDetails, RequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryBody String theQueryBody) {
		if (theQueryBody != null) {
			return processGraphQLRequest(theServletRequestDetails, theId, theQueryBody);
		}
		throw new InvalidRequestException(Msg.code(1145) + "Unable to parse empty GraphQL expression");
	}

	public String processGraphQLRequest(ServletRequestDetails theRequestDetails, IIdType theId, String theQuery) {
		Package parsedGraphQLRequest;
		try {
			parsedGraphQLRequest = Parser.parse(theQuery);
		} catch (Exception e) {
			throw new InvalidRequestException(Msg.code(1146) + "Unable to parse GraphQL Expression: " + e);
		}

		return processGraphQLRequest(theRequestDetails, theId, parsedGraphQLRequest);
	}

	protected String processGraphQLRequest(ServletRequestDetails theRequestDetails, IIdType theId, Package parsedGraphQLRequest) {
		IGraphQLEngine engine = myEngineFactory.get();
		engine.setAppInfo(theRequestDetails);
		engine.setServices(myStorageServices);
		engine.setGraphQL(parsedGraphQLRequest);

		try {

			if (theId != null) {
				IBaseResource focus = myStorageServices.lookup(theRequestDetails, theId.getResourceType(), theId.getIdPart());
				engine.setFocus(focus);
			}
			engine.execute();

			StringBuilder outputBuilder = new StringBuilder();
			ObjectValue output = engine.getOutput();
			output.write(outputBuilder, 0, "\n");

			return outputBuilder.toString();

		} catch (Exception e) {
			StringBuilder b = new StringBuilder();
			b.append("Unable to execute GraphQL Expression: ");
			int statusCode = 500;
			if (e instanceof BaseServerResponseException) {
				b.append("HTTP ");
				statusCode = ((BaseServerResponseException) e).getStatusCode();
				b.append(statusCode);
				b.append(" ");
			} else {
				// This means it's a bug, so let's log
				ourLog.error("Failure during GraphQL processing", e);
			}
			b.append(e.getMessage());
			throw new UnclassifiedServerFailureException(statusCode, Msg.code(1147) + b);
		}
	}

	@Initialize
	public void initialize(RestfulServer theServer) {
		ourLog.trace("Initializing GraphQL provider");
		if (!theServer.getFhirContext().getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			throw new ConfigurationException(Msg.code(1148) + "Can not use " + getClass().getName() + " provider on server with FHIR " + theServer.getFhirContext().getVersion().getVersion().name() + " context");
		}
	}


}

