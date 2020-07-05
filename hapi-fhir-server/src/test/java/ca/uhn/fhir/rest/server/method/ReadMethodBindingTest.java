package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings(value = "unchecked")
@ExtendWith(MockitoExtension.class)
public class ReadMethodBindingTest {

	@Mock
	private FhirContext myCtx;

	@Mock
	private RequestDetails myRequestDetails;

	@Mock
	private RuntimeResourceDefinition definition;

	@Test
	public void testIncomingServerRequestMatchesMethod_Read() throws NoSuchMethodException {

		class MyProvider {
			@Read(version = false)
			public IBaseResource read(@IdParam IIdType theIdType) {
				return null;
			}
		}

		when(myCtx.getResourceDefinition(any(Class.class))).thenReturn(definition);
		when(definition.getName()).thenReturn("Patient");
		when(myRequestDetails.getResourceName()).thenReturn("Patient");
		when(myRequestDetails.getRequestType()).thenReturn(RequestTypeEnum.GET);

		// Read
		ReadMethodBinding binding = createBinding(new MyProvider());
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123"));
		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// VRead
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123/_history/123"));
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// Type history
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123"));
		when(myRequestDetails.getResourceName()).thenReturn("Patient");
		when(myRequestDetails.getOperation()).thenReturn("_history");
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));

	}

	@Test
	public void testIncomingServerRequestMatchesMethod_VRead() throws NoSuchMethodException {

		class MyProvider {
			@Read(version = true)
			public IBaseResource read(@IdParam IIdType theIdType) {
				return null;
			}
		}

		when(myCtx.getResourceDefinition(any(Class.class))).thenReturn(definition);
		when(definition.getName()).thenReturn("Patient");
		when(myRequestDetails.getResourceName()).thenReturn("Patient");
		when(myRequestDetails.getRequestType()).thenReturn(RequestTypeEnum.GET);

		// Read - wrong resource type
		ReadMethodBinding binding = createBinding(new MyProvider());
		when(myRequestDetails.getResourceName()).thenReturn("Observation");
		when(myRequestDetails.getId()).thenReturn(new IdDt("Observation/123"));
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// Read
		when(myRequestDetails.getResourceName()).thenReturn("Patient");
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123"));
		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// VRead
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123/_history/123"));
		when(myRequestDetails.getOperation()).thenReturn("_history");
		assertEquals(MethodMatchEnum.EXACT, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// Some other operation
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123/_history/123"));
		when(myRequestDetails.getOperation()).thenReturn("$foo");
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));

		// History operation
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123"));
		when(myRequestDetails.getOperation()).thenReturn("_history");
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));

	}

	@Test
	public void testIncomingServerRequestNoMatch_HasCompartment() throws NoSuchMethodException {

		class MyProvider {
			@Read(version = false)
			public IBaseResource read(@IdParam IIdType theIdType) {
				return null;
			}
		}

		when(myCtx.getResourceDefinition(any(Class.class))).thenReturn(definition);
		when(definition.getName()).thenReturn("Patient");
		when(myRequestDetails.getResourceName()).thenReturn("Patient");
		when(myRequestDetails.getCompartmentName()).thenReturn("Patient");
		when(myRequestDetails.getId()).thenReturn(new IdDt("Patient/123"));

		ReadMethodBinding binding = createBinding(new MyProvider());
		assertEquals(MethodMatchEnum.NONE, binding.incomingServerRequestMatchesMethod(myRequestDetails));
	}

	public ReadMethodBinding createBinding(Object theProvider) throws NoSuchMethodException {
		Method method = theProvider.getClass().getMethod("read", IIdType.class);
		return new ReadMethodBinding(MyPatient.class, method, myCtx, theProvider);
	}

	@ResourceDef(name = "Patient")
	private static class MyPatient implements IBaseResource {

		@Override
		public IBaseMetaType getMeta() {
			return null;
		}

		@Override
		public IIdType getIdElement() {
			return null;
		}

		@Override
		public IBaseResource setId(String theId) {
			return null;
		}

		@Override
		public IBaseResource setId(IIdType theId) {
			return null;
		}

		@Override
		public FhirVersionEnum getStructureFhirVersionEnum() {
			return null;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean hasFormatComment() {
			return false;
		}

		@Override
		public List<String> getFormatCommentsPre() {
			return null;
		}

		@Override
		public List<String> getFormatCommentsPost() {
			return null;
		}

		@Override
		public Object getUserData(String theName) {
			return null;
		}

		@Override
		public void setUserData(String theName, Object theValue) {

		}
	}

}
