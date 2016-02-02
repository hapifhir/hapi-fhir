package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.TokenParam;

public class ServerInvalidDefinitionDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testOperationReturningOldBundleProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new OperationReturningOldBundleProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Can not return a DSTU1 bundle"));
		}
	}

	@Test
	public void testWrongConditionalUrlType() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new UpdateWithWrongConditionalUrlType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Parameters annotated with @ConditionalUrlParam must be of type String, found incorrect parameteter in method \"public ca.uhn.fhir.rest.api.MethodOutcome ca.uhn.fhir.rest.server.ServerInvalidDefinitionDstu2Test$UpdateWithWrongConditionalUrlType.update(ca.uhn.fhir.rest.param.TokenParam,ca.uhn.fhir.model.dstu2.resource.Patient)"));
		}
	}

	public static class OperationReturningOldBundleProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "$OP_TYPE_RET_OLD_BUNDLE")
		public ca.uhn.fhir.model.api.Bundle opTypeRetOldBundle(@OperationParam(name = "PARAM1") StringDt theParam1, @OperationParam(name = "PARAM2") Patient theParam2) {
			return null;
		}

	}

	public static class UpdateWithWrongConditionalUrlType implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@ConditionalUrlParam TokenParam theToken, @ResourceParam Patient theParam2) {
			return null;
		}

	}

}
