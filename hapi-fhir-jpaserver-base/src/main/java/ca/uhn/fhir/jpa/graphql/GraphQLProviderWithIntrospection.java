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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.VersionUtil;
import com.google.gson.Gson;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeDefinition;
import graphql.language.TypeName;
import graphql.scalar.GraphqlStringCoercing;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.utils.GraphQLSchemaGenerator;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class GraphQLProviderWithIntrospection extends GraphQLProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(GraphQLProviderWithIntrospection.class);
	private final GraphQLSchemaGenerator myGenerator;
	private final IValidationSupport myValidationSupport;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final VersionSpecificWorkerContextWrapper myContext;

	/**
	 * Constructor
	 */
	public GraphQLProviderWithIntrospection(FhirContext theFhirContext, IValidationSupport theValidationSupport, IGraphQLStorageServices theIGraphQLStorageServices, ISearchParamRegistry theSearchParamRegistry) {
		super(theFhirContext, theValidationSupport, theIGraphQLStorageServices);

		myValidationSupport = theValidationSupport;
		mySearchParamRegistry = theSearchParamRegistry;

		myContext = VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(theValidationSupport);
		myGenerator = new GraphQLSchemaGenerator(myContext, VersionUtil.getVersion());
	}

	@Override
	public String processGraphQlGetRequest(ServletRequestDetails theRequestDetails, IIdType theId, String theQueryUrl) {
		return super.processGraphQlGetRequest(theRequestDetails, theId, theQueryUrl);
	}

	@Override
	public String processGraphQlPostRequest(ServletRequestDetails theServletRequestDetails, RequestDetails theRequestDetails, IIdType theId, String theQueryBody) {
		if (theQueryBody.contains("__schema")) {
			EnumSet<GraphQLSchemaGenerator.FHIROperationType> operations;
			if (theId == null) {
				operations = EnumSet.of(GraphQLSchemaGenerator.FHIROperationType.READ);
			} else {
				 operations = EnumSet.of(GraphQLSchemaGenerator.FHIROperationType.SEARCH);
			}
			return generateSchema(theQueryBody, theRequestDetails.getResourceName(), operations);
		} else {
			return super.processGraphQlPostRequest(theServletRequestDetails, theRequestDetails, theId, theQueryBody);
		}
	}

	/**
	 * Note that this is synchronized to avoid issues with user data - Once the
	 * issue that causes us to need {@link #cleanUserData()} is resolved, we can
	 * make it non-synchronized
	 */
	private synchronized String generateSchema(String theQueryBody, String theResourceName, EnumSet<GraphQLSchemaGenerator.FHIROperationType> theOperations) {
		ourLog.info("Schema requested");

		List<SearchParameter> parameters = new ArrayList<>();
		for (RuntimeSearchParam next : mySearchParamRegistry.getActiveSearchParams(theResourceName).values()) {

			SearchParameter sp = new SearchParameter();
			sp.setUrl(next.getUri());
			sp.setCode(next.getName());
			sp.setName(next.getName());

			switch (next.getParamType()) {
				case NUMBER:
					sp.setType(Enumerations.SearchParamType.NUMBER);
					break;
				case DATE:
					sp.setType(Enumerations.SearchParamType.DATE);
					break;
				case STRING:
					sp.setType(Enumerations.SearchParamType.STRING);
					break;
				case TOKEN:
					sp.setType(Enumerations.SearchParamType.TOKEN);
					break;
				case REFERENCE:
					sp.setType(Enumerations.SearchParamType.REFERENCE);
					break;
				case COMPOSITE:
					sp.setType(Enumerations.SearchParamType.COMPOSITE);
					break;
				case QUANTITY:
					sp.setType(Enumerations.SearchParamType.QUANTITY);
					break;
				case URI:
					sp.setType(Enumerations.SearchParamType.URI);
					break;
				case HAS:
				case SPECIAL:
				default:
					continue;
			}

			parameters.add(sp);
		}

		cleanUserData();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			myGenerator.generateTypes(bos);

			// Generate schema for the resource type
			StructureDefinition sd = fetchStructureDefinition(theResourceName);


			myGenerator.generateResource(bos, sd, parameters, theOperations);

		} catch (IOException e) {
			throw new InternalErrorException(e);
		}

		String schema = new String(bos.toByteArray(), StandardCharsets.UTF_8);

		// This base type gets referred to from elsewhere

		schema = schema +
			"\ntype Resource {" +
			"\n  _id: [token]" +
			"\n}" +
			"\n";

		schema = schema +
			"\ninput ResourceInput {" +
			"\n  _id: [token]" +
			"\n}" +
			"\n";


		if (theOperations.contains(GraphQLSchemaGenerator.FHIROperationType.READ)) {
			schema +="\ntype Query {" +
				"\n  " + theResourceName + "(id: String): " + theResourceName +
			"\n}";
		}

		if (theOperations.contains(GraphQLSchemaGenerator.FHIROperationType.SEARCH)) {
			int startIdx = schema.indexOf(theResourceName + "List(");
			int endIdx = schema.indexOf(")", startIdx);
			String paramList = schema.substring(startIdx, endIdx + 1);
			paramList = paramList.replace("\n", ", ").replace("\r", "");
			schema += paramList + ": " + theResourceName;
			schema += "\n}";
		}

		/*
		 * There's a bug in the schema generator currently that causes URI to be included
		 * twice.
		 */
		int idx = schema.indexOf("scalar uri");
		int idx2 = schema.indexOf("scalar uri", idx + 1);
		if (idx2 != -1) {
			schema = schema.substring(0, idx2) + schema.substring(idx2 + "scalar uri".length());
		}

		StringBuilder schemaOutput = new StringBuilder();
		int index = 0;
		for (String next : schema.split("\\n")) {
			schemaOutput.append(index++).append(": ").append(next).append("\n");
		}

		ourLog.info("Schema generated: {} chars", schemaOutput.length());
		ourLog.info("Schema generated: {}", schemaOutput);

		SchemaParser schemaParser = new SchemaParser();
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

		SchemaGenerator schemaGenerator = new SchemaGenerator();
		RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();

		for (String next : typeDefinitionRegistry.scalars().keySet()) {
			if (Character.isUpperCase(next.charAt(0))) {
				continue;
			}
			runtimeWiringBuilder.scalar(new GraphQLScalarType.Builder().name(next).coercing(new GraphqlStringCoercing()).build());
		}

		for (String next : new HashSet<>(typeDefinitionRegistry.types().keySet())) {
			TypeDefinition type = typeDefinitionRegistry.types().get(next);
			if (type instanceof ObjectTypeDefinition) {
				ObjectTypeDefinition otd = (ObjectTypeDefinition) type;
				for (FieldDefinition nextFieldDef : otd.getFieldDefinitions()) {
					Type fieldDefType = nextFieldDef.getType();

					if (fieldDefType instanceof ListType) {
						ListType listType = (ListType) fieldDefType;
						fieldDefType = listType.getType();
					}

					if (fieldDefType instanceof TypeName) {
						String typeName = ((TypeName) fieldDefType).getName();
						if (!typeDefinitionRegistry.types().containsKey(typeName)) {
							runtimeWiringBuilder.type(typeName, UnaryOperator.identity());
						}
					}
				}
			}
			runtimeWiringBuilder.type(next, UnaryOperator.identity());
		}

//		for (BaseRuntimeElementDefinition<?> t : myValidationSupport.getFhirContext().getElementDefinitions()) {
//			if (t instanceof RuntimePrimitiveDatatypeDefinition) {
//				RuntimePrimitiveDatatypeDefinition runtimePrimitiveDatatypeDefinition = (RuntimePrimitiveDatatypeDefinition) t;
//				runtimeWiringBuilder.scalar(new GraphQLScalarType.Builder().name(runtimePrimitiveDatatypeDefinition.getName()).coercing(new GraphqlStringCoercing()).build());
//			}
//		}

		RuntimeWiring runtimeWiring = runtimeWiringBuilder
//			.scalar(new GraphQLScalarType.Builder().name("xhtml").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("number").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("token").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("reference").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("composite").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("quantity").coercing(new GraphqlStringCoercing()).build())
//			.scalar(new GraphQLScalarType.Builder().name("special").coercing(new GraphqlStringCoercing()).build())
			.build();
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

		GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

		String queryBody = theQueryBody;
		ExecutionResult executionResult = build.execute(queryBody);

		Map<String, Object> data = executionResult.toSpecification();
		String output = new Gson().toJson(data);
		return output;
	}

	private void cleanUserData() {
		// This is a hack due to the GraphQLSchemaGenerator leaving junk in the
		// user data map after each call
		for (StructureDefinition nextStructure : myContext.allStructures()) {
			for (ElementDefinition next : nextStructure.getSnapshot().getElement()) {
				next.clearUserData("gql.type.name.type");
				next.clearUserData("gql.type.name.input");
			}
		}
	}

	@Nonnull
	private StructureDefinition fetchStructureDefinition(String resourceName) {
		IBaseResource r4StructureDefinition = myValidationSupport.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/" + resourceName);
		Validate.notNull(r4StructureDefinition);
		StructureDefinition retVal = (StructureDefinition) VersionConvertorFactory_40_50.convertResource((org.hl7.fhir.r4.model.StructureDefinition) r4StructureDefinition, new BaseAdvisor_40_50(false));
		Validate.notNull(retVal);
		return retVal;
	}

	public void test() {
//		myGenerator.generateResource();
	}



}
