package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.graphql.GraphQLProviderWithIntrospection;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tmp {
	private static final Logger ourLog = LoggerFactory.getLogger(Tmp.class);

	public static void main(String[] args) {
		FhirContext ctx = FhirContext.forR4Cached();
		IValidationSupport val = ctx.getValidationSupport();
		IGraphQLStorageServices svcs = new IGraphQLStorageServices() {
			@Override
			public ReferenceResolution lookup(Object theO, IBaseResource theIBaseResource, IBaseReference theIBaseReference) throws FHIRException {
				return null;
			}

			@Override
			public IBaseResource lookup(Object theO, String theS, String theS1) throws FHIRException {
				return null;
			}

			@Override
			public void listResources(Object theO, String theS, List<Argument> theList, List<IBaseResource> theList1) throws FHIRException {

			}

			@Override
			public IBaseBundle search(Object theO, String theS, List<Argument> theList) throws FHIRException {
				return null;
			}
		};
		ISearchParamRegistry reg = new ISearchParamRegistry() {
			@Override
			public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
				return null;
			}

			@Override
			public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
				return ctx.getResourceDefinition("Patient").getSearchParams().stream().collect(Collectors.toMap(t->t.getName(), t->t));
			}

			@Nullable
			@Override
			public RuntimeSearchParam getActiveSearchParamByUrl(String theUrl) {
				return null;
			}
		};
		GraphQLProviderWithIntrospection prov = new GraphQLProviderWithIntrospection(ctx, val, svcs, reg);
		String query = "\n" +
			"    query IntrospectionQuery {\n" +
			"      __schema {\n" +
			"        queryType { name }\n" +
			"        mutationType { name }\n" +
			"        subscriptionType { name }\n" +
			"        types {\n" +
			"          ...FullType\n" +
			"        }\n" +
			"        directives {\n" +
			"          name\n" +
			"          description\n" +
			"          locations\n" +
			"          args {\n" +
			"            ...InputValue\n" +
			"          }\n" +
			"        }\n" +
			"      }\n" +
			"    }\n" +
			"\n" +
			"    fragment FullType on __Type {\n" +
			"      kind\n" +
			"      name\n" +
			"      description\n" +
			"      fields(includeDeprecated: true) {\n" +
			"        name\n" +
			"        description\n" +
			"        args {\n" +
			"          ...InputValue\n" +
			"        }\n" +
			"        type {\n" +
			"          ...TypeRef\n" +
			"        }\n" +
			"        isDeprecated\n" +
			"        deprecationReason\n" +
			"      }\n" +
			"      inputFields {\n" +
			"        ...InputValue\n" +
			"      }\n" +
			"      interfaces {\n" +
			"        ...TypeRef\n" +
			"      }\n" +
			"      enumValues(includeDeprecated: true) {\n" +
			"        name\n" +
			"        description\n" +
			"        isDeprecated\n" +
			"        deprecationReason\n" +
			"      }\n" +
			"      possibleTypes {\n" +
			"        ...TypeRef\n" +
			"      }\n" +
			"    }\n" +
			"\n" +
			"    fragment InputValue on __InputValue {\n" +
			"      name\n" +
			"      description\n" +
			"      type { ...TypeRef }\n" +
			"      defaultValue\n" +
			"    }\n" +
			"\n" +
			"    fragment TypeRef on __Type {\n" +
			"      kind\n" +
			"      name\n" +
			"      ofType {\n" +
			"        kind\n" +
			"        name\n" +
			"        ofType {\n" +
			"          kind\n" +
			"          name\n" +
			"          ofType {\n" +
			"            kind\n" +
			"            name\n" +
			"            ofType {\n" +
			"              kind\n" +
			"              name\n" +
			"              ofType {\n" +
			"                kind\n" +
			"                name\n" +
			"                ofType {\n" +
			"                  kind\n" +
			"                  name\n" +
			"                  ofType {\n" +
			"                    kind\n" +
			"                    name\n" +
			"                  }\n" +
			"                }\n" +
			"              }\n" +
			"            }\n" +
			"          }\n" +
			"        }\n" +
			"      }\n" +
			"    }\n" +
			"  ";

		SystemRequestDetails rd = new SystemRequestDetails();
		rd.setResourceName("Observation");

		String output = prov.processGraphQlPostRequest(null, rd, null, query);
//		ourLog.info("Output: {}", output);
		output = prov.processGraphQlPostRequest(null, rd, null, query);
//		ourLog.info("Output: {}", output);
	}


}

