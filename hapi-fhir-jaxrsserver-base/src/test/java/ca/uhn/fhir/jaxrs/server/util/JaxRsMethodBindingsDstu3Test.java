package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderDstu3;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JaxRsMethodBindingsDstu3Test {

	@BeforeEach
	public void setUp() {
		JaxRsMethodBindings.getClassBindings().clear();
	}

	@Test
	public void testFindMethodsForProviderNotDefinedMappingMethods() {
		assertThatExceptionOfType(NotImplementedOperationException.class).isThrownBy(() -> new TestJaxRsDummyPatientProviderDstu3().getBindings().getBinding(RestOperationTypeEnum.UPDATE, ""));
	}

	@Test
	public void testFindMethodsForProviderWithMethods() {
		@SuppressWarnings("unused")
		class TestFindPatientProvider extends TestJaxRsDummyPatientProviderDstu3 {
			@Search
			public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
		}
		new TestFindPatientProvider();
		assertEquals(TestFindPatientProvider.class, new TestFindPatientProvider().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getProvider().getClass());
	}

	@Test
	public void testFindMethodsFor2ProvidersWithMethods() {
		@SuppressWarnings("unused")
		class TestFindPatientProvider extends TestJaxRsDummyPatientProviderDstu3 {
			@Search
			public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
		}
		@SuppressWarnings("unused")
		class TestUpdatePatientProvider extends TestJaxRsDummyPatientProviderDstu3 {
			@Update
			public MethodOutcome update(@IdParam final IdType theId, @ResourceParam final Patient patient) {
				return null;
			}
		}
		assertEquals(TestFindPatientProvider.class, new TestFindPatientProvider().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getProvider().getClass());
		assertEquals(TestUpdatePatientProvider.class, new TestUpdatePatientProvider().getBindings().getBinding(RestOperationTypeEnum.UPDATE, "").getProvider().getClass());
	}

	@Test
	public void testFindMethodsWithDoubleMethodsDeclaration() {
		@SuppressWarnings("unused")
		class TestDoubleSearchProvider extends TestJaxRsDummyPatientProviderDstu3 {
			@Search
			public List<Patient> search1(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}

			@Search
			public List<Patient> search2(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
		}
		var e = assertThrows(IllegalArgumentException.class, () -> new TestDoubleSearchProvider());
		assertThat(e.getMessage())
			.contains("search1")
			.contains("search2");

	}

	@Test
	public void testFindMethodsWithMultipleMethods() {
		@SuppressWarnings("unused")
		class TestFindPatientProvider extends TestJaxRsDummyPatientProviderDstu3 {
			@Search
			public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}

			@Update
			public MethodOutcome update(@IdParam final IdType theId, @ResourceParam final Patient patient) {
				return null;
			}

			@Operation(name = "firstMethod", idempotent = true, returnParameters = {@OperationParam(name = "return", type = StringType.class)})
			public Parameters firstMethod(@OperationParam(name = "dummy") StringType dummyInput) {
				return null;
			}

			@Operation(name = "secondMethod", returnParameters = {@OperationParam(name = "return", type = StringType.class)})
			public Parameters secondMethod(@OperationParam(name = "dummy") StringType dummyInput) {
				return null;
			}
		}
		JaxRsMethodBindings bindings = new TestFindPatientProvider().getBindings();
		assertThat(bindings.getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getBindingKey()).contains(".search(");
		assertThat(bindings.getBinding(RestOperationTypeEnum.UPDATE, "").getBindingKey()).contains(".update(");
		assertThat(bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$firstMethod").getBindingKey()).contains(".firstMethod(");
		assertThat(bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$secondMethod").getBindingKey()).contains(".secondMethod(");
		assertThrows(NotImplementedOperationException.class, () -> bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$thirdMethod"));
	}

}
