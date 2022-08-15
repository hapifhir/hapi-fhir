package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.server.MockServletUtil;
import com.google.common.collect.Lists;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerInvalidDefinitionR4Test extends BaseR4ServerTest {

	@Test
	public void testWrongConditionalUrlType() throws Exception {
		try {
			startServer(new UpdateWithWrongConditionalUrlType());
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString(
				"Parameters annotated with @ConditionalUrlParam must be of type String, found incorrect parameter in method \"public ca.uhn.fhir.rest.api.MethodOutcome ca.uhn.fhir.rest.server.ServerInvalidDefinitionR4Test$UpdateWithWrongConditionalUrlType.update(ca.uhn.fhir.rest.param.TokenParam,org.hl7.fhir.r4.model.Patient)"));
		}
	}

	@Test
	public void testWrongResourceType() throws Exception {
		try {
			startServer(new UpdateWithWrongResourceType());
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains
				.containsString("Method 'update' is annotated with @ResourceParam but has a type that is not an implementation of org.hl7.fhir.instance.model.api.IBaseResource or String or byte[]"));
		}
	}

	@Test
	public void testWrongValidateModeType() throws Exception {
		try {
			startServer(new ValidateWithWrongModeType());
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Parameter annotated with @Validate.Mode must be of type ca.uhn.fhir.rest.api.ValidationModeEnum"));
		}
	}

	@Test
	public void testWrongValidateProfileType() throws Exception {
		try {
			startServer(new ValidateWithWrongProfileType());
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("Parameter annotated with @Validate.Profile must be of type java.lang.String"));
		}
	}

	@Test
	public void testWrongParameterAnnotationOnOperation() throws Exception {
		class MyProvider {

			@Operation(name = "foo")
			public MethodOutcome update(@OptionalParam(name = "foo") StringType theFoo) {
				return null;
			}

		}

		try {
			startServer(new MyProvider());
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString(Msg.code(288) + "Failure scanning class MyProvider: "+ Msg.code(421) + "Illegal method parameter annotation @OptionalParam on method: public ca.uhn.fhir.rest.api.MethodOutcome ca.uhn.fhir.rest.server.ServerInvalidDefinitionR4Test$1MyProvider.update(org.hl7.fhir.r4.model.StringType)"));
		}
	}

	/**
	 * @OperationParam on a search method
	 * <p>
	 * See #2063
	 */
	@Test
	public void testOperationParamOnASearchMethod() throws Exception {

		class MyProvider extends ServerMethodSelectionR4Test.MyBaseProvider {
			@Search
			public List<IBaseResource> search(
				@OptionalParam(name = "name") StringType theName,
				@OperationParam(name = "name2") StringType theName2
			) {
				return Lists.newArrayList(new Patient().setActive(true).setId("Patient/123"));
			}
		}
		MyProvider provider = new MyProvider();

		try {
			startServer(provider);
			fail();
		} catch (ServletException e) {
			assertEquals(Msg.code(288) + "Failure scanning class MyProvider: "+ Msg.code(404) + "@OperationParam detected on method that is not annotated with @Operation: public java.util.List<org.hl7.fhir.instance.model.api.IBaseResource> ca.uhn.fhir.rest.server.ServerInvalidDefinitionR4Test$2MyProvider.search(org.hl7.fhir.r4.model.StringType,org.hl7.fhir.r4.model.StringType)", e.getCause().getMessage());
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

	@Test
	public void testOperationOnNoTypes() throws Exception {
		@SuppressWarnings("unused")
		class PlainProviderWithExtendedOperationOnNoType {

			@Operation(name = "plain", idempotent = true, returnParameters = {@OperationParam(min = 1, max = 2, name = "out1", type = StringType.class)})
			public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart,
														 @OperationParam(name = "end") DateType theEnd) {
				return null;
			}

		}

		RestfulServer rs = new RestfulServer(FhirContext.forR4Cached());
		rs.setProviders(new PlainProviderWithExtendedOperationOnNoType());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		try {
			rs.init(MockServletUtil.createServletConfig());
			fail();
		} catch (ServletException e) {
			assertEquals(Msg.code(297) + "Failed to initialize FHIR Restful server: "+ Msg.code(288) + "Failure scanning class PlainProviderWithExtendedOperationOnNoType: "+ Msg.code(425) + "@Operation method is an instance level method (it has an @IdParam parameter) but is not marked as global() and is not declared in a resource provider: everything", e.getMessage());
		}

	}


}
