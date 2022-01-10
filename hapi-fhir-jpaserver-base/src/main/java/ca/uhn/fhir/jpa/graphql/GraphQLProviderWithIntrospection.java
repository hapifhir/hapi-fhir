package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.util.VersionUtil;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.utils.GraphQLSchemaGenerator;

public class GraphQLProviderWithIntrospection extends GraphQLProvider {

	private final GraphQLSchemaGenerator myGenerator;

	public GraphQLProviderWithIntrospection(IValidationSupport theValidationSupport) {
		super(theStorageService);

		IWorkerContext context = VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(theValidationSupport);
		myGenerator = new GraphQLSchemaGenerator(context, VersionUtil.getVersion());

	}


	public void test() {
		myGenerator.generateResource();
	}


}
