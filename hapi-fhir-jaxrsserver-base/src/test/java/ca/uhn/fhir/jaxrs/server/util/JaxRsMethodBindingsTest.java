package ca.uhn.fhir.jaxrs.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProvider;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
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

@FixMethodOrder(MethodSorters.DEFAULT)
public class JaxRsMethodBindingsTest {
	
	 @Before
	 public void setUp() {
		 JaxRsMethodBindings.getClassBindings().clear();
	 }

	@Test(expected = NotImplementedOperationException.class)
	public void testFindMethodsForProviderNotDefinedMappingMethods() {
		new TestJaxRsDummyPatientProvider().getBindings().getBinding(RestOperationTypeEnum.UPDATE, "");
	}
	
	@Test
	public void testFindMethodsForProviderWithMethods() {
		class TestFindPatientProvider extends TestJaxRsDummyPatientProvider {
		    @Search
		    public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
		    	return null;
		    }
		}
		new TestFindPatientProvider();
		assertEquals(TestFindPatientProvider.class, new TestFindPatientProvider().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
	}
	
	@Test
	public void testFindMethodsFor2ProvidersWithMethods() {
		class TestFindPatientProvider extends TestJaxRsDummyPatientProvider {
			@Search
			public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
		}
		class TestUpdatePatientProvider extends TestJaxRsDummyPatientProvider {
		    @Update
		    public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient) {
				return null;
			}
		}
		assertEquals(TestFindPatientProvider.class, new TestFindPatientProvider().getBindings().getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getDeclaringClass());
		assertEquals(TestUpdatePatientProvider.class, new TestUpdatePatientProvider().getBindings().getBinding(RestOperationTypeEnum.UPDATE, "").getMethod().getDeclaringClass());
	}
	
	@Test
	public void testFindMethodsWithDoubleMethodsDeclaration() {
		class TestDoubleSearchProvider extends TestJaxRsDummyPatientProvider {
			@Search
			public List<Patient> search1(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
			
			@Search
			public List<Patient> search2(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
		}
		try {
			new TestDoubleSearchProvider();
			fail();
		} catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("search1"));
			assertTrue(e.getMessage().contains("search2"));
		}
	}
	
	@Test
	public void testFindMethodsWithMultipleMethods() {
		class TestFindPatientProvider extends TestJaxRsDummyPatientProvider {
			@Search
			public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
				return null;
			}
			@Update
			public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient) {
				return null;
			}
			@Operation(name = "firstMethod", idempotent = true, returnParameters = { @OperationParam(name = "return", type = StringDt.class) })
			public Parameters firstMethod(@OperationParam(name = "dummy") StringDt dummyInput) {
				return null;
			}
			@Operation(name = "secondMethod", returnParameters = { @OperationParam(name = "return", type = StringDt.class) })
			public Parameters secondMethod(@OperationParam(name = "dummy") StringDt dummyInput) {
				return null;
			}
		}
		JaxRsMethodBindings bindings = new TestFindPatientProvider().getBindings();
		assertEquals("search", bindings.getBinding(RestOperationTypeEnum.SEARCH_TYPE, "").getMethod().getName());
		assertEquals("update", bindings.getBinding(RestOperationTypeEnum.UPDATE, "").getMethod().getName());
		assertEquals("firstMethod", bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$firstMethod").getMethod().getName());
		assertEquals("secondMethod", bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$secondMethod").getMethod().getName());
		try {
			bindings.getBinding(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, "$thirdMethod");
			fail();
		} catch(NotImplementedOperationException e){
		}
	}	

}
