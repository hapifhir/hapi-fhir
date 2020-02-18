package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TestUtil;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.AfterClass;
import org.junit.Test;

import javax.servlet.ServletException;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ServerInvalidDefinitionR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();

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
			assertThat(e.getCause().toString(), StringContains.containsString(
				"Parameters annotated with @ConditionalUrlParam must be of type String, found incorrect parameter in method \"public ca.uhn.fhir.rest.api.MethodOutcome ca.uhn.fhir.rest.server.ServerInvalidDefinitionR4Test$UpdateWithWrongConditionalUrlType.update(ca.uhn.fhir.rest.param.TokenParam,org.hl7.fhir.r4.model.Patient)"));
		}
	}

	@Test
	public void testWrongResourceType() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new UpdateWithWrongResourceType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains
				.containsString("Method 'update' is annotated with @ResourceParam but has a type that is not an implementation of org.hl7.fhir.instance.model.api.IBaseResource or String or byte[]"));
		}
	}

	@Test
	public void testWrongValidateModeType() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new ValidateWithWrongModeType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Parameter annotated with @Validate.Mode must be of type ca.uhn.fhir.rest.api.ValidationModeEnum"));
		}
	}

	@Test
	public void testWrongValidateProfileType() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new ValidateWithWrongProfileType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Parameter annotated with @Validate.Profile must be of type java.lang.String"));
		}
	}

	@Test
	public void testWrongParameterAnnotationOnOperation() {
		class MyProvider {

			@Operation(name = "foo")
			public MethodOutcome update(@OptionalParam(name = "foo") StringType theFoo) {
				return null;
			}

		}

		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.registerProvider(new MyProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("Failure scanning class MyProvider: Illegal method parameter annotation @OptionalParam on method: public ca.uhn.fhir.rest.api.MethodOutcome ca.uhn.fhir.rest.server.ServerInvalidDefinitionR4Test$1MyProvider.update(org.hl7.fhir.r4.model.StringType)"));
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

	public static class UpdateWithWrongResourceType implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Integer theParam2) {
			return null;
		}

	}

	public static class ValidateWithWrongModeType implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Validate
		public MethodOutcome update(@ResourceParam Patient thePatient, @Validate.Mode Integer theParam2) {
			return null;
		}

	}

	public static class ValidateWithWrongProfileType implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Validate
		public MethodOutcome update(@ResourceParam Patient thePatient, @Validate.Profile Integer theParam2) {
			return null;
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
