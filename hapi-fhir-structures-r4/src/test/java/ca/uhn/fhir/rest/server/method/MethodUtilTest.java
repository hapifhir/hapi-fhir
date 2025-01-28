package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.ParamsWithIdParamAndTypeConversion;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.ParamsWithTypeConversion;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SampleParams;
import ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SampleParamsWithIdParam;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.EXPAND;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_DESCRIPTION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_INVALID_ANNOTATION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_INVALID_GENERIC_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_NON_ASSIGNABLE_TYPE_NAME;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_NO_ANNOTATIONS;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.METHOD_WITH_UNKNOWN_TYPE_NAME;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.OP_INSTANCE_OR_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SIMPLE_METHOD_WITH_PARAMS_CONVERSION;
import static ca.uhn.fhir.rest.server.method.MethodAndOperationParamsInnerClassesAndMethods.SUPER_SIMPLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This test lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
class MethodUtilTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private final MethodAndOperationParamsInnerClassesAndMethods myMethodAndOperationParamsInnerClassesAndMethods = new MethodAndOperationParamsInnerClassesAndMethods();

	private Object myProvider = null;

	@Test
	void simpleMethodNoParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SUPER_SIMPLE);

		assertThat(resourceParameters).isNotNull().isEmpty();
	}

	@Test
	void invalid_methodWithOperationParamsNoOperation() {
		assertThatThrownBy(
			() -> getMethodAndExecute(INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION,
				String.class))
			.isInstanceOf(ConfigurationException.class);
	}

	@Test
	void invalidMethodWithNoAnnotations() {
		assertThatThrownBy(() -> getMethodAndExecute(METHOD_WITH_NO_ANNOTATIONS, String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations");
	}

	@Test
	void invalidMethodWithInvalidGenericType() {
		assertThatThrownBy(() -> getMethodAndExecute(METHOD_WITH_INVALID_GENERIC_TYPE, List.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("is of an invalid generic type");
	}

	@Test
	void invalidMethodWithUnknownTypeName() {
		assertThatThrownBy(() -> getMethodAndExecute(METHOD_WITH_UNKNOWN_TYPE_NAME, String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
	}

	@Test
	void invalidMethodWithNonAssignableTypeName() {
		assertThatThrownBy(() -> getMethodAndExecute(METHOD_WITH_NON_ASSIGNABLE_TYPE_NAME, String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining(" has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
	}

	@Test
	void invalidMethodWithInvalidAnnotation() {
		assertThatThrownBy(() -> getMethodAndExecute(METHOD_WITH_INVALID_ANNOTATION, String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations");
	}

	@Test
	void sampleMethodOperationParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_OPERATION_PARAMS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_OPERATION_PARAMS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
		new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_OPERATION_PARAMS, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void expand() {
		final List<IParameter> resourceParameters = getMethodAndExecute(EXPAND, HttpServletRequest.class, IIdType.class, IBaseResource.class, RequestDetails.class);

		assertThat(resourceParameters).isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(ServletRequestParameter.class, NullParameter.class, OperationParameter.class, RequestDetailsParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new ServletRequestParameterToAssert(),
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "valueSet", "$"+EXPAND, null, String.class, "Resource", Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new RequestDetailsParameterToAssert()
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void opInstanceOrType() {
		myProvider = new MethodAndOperationParamsInnerClassesAndMethods.PatientProvider();
		final List<IParameter> resourceParameters = getMethodAndExecute(OP_INSTANCE_OR_TYPE, IdType.class, StringType.class, Patient.class);

		assertThat(resourceParameters).isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "PARAM1", "$OP_INSTANCE_OR_TYPE", null, String.class, "string", Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "PARAM2", "$OP_INSTANCE_OR_TYPE", null, String.class, "Patient", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodOperationParamsWithFhirTypes() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_OPERATION_PARAMS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_OPERATION_PARAMS, ArrayList.class, String.class,null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
		new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_OPERATION_PARAMS, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	// -------------------------------------------------------------------------
	// 1. Testing MethodUtil.extractDescription()
	// -------------------------------------------------------------------------
	@Test
	void testExtractDescription_withDescriptionAnnotation() {
		final Method method = getMethod(METHOD_WITH_DESCRIPTION);
		final Annotation[] annotations = method.getAnnotations();

		assertThat(annotations).hasSize(1);

		final Annotation annotation = annotations[0];

		assertThat(annotation).isInstanceOf(Description.class);

		final Description descriptionAnnotation = (Description) annotation;

		assertThat(descriptionAnnotation.shortDefinition()).isEqualTo("network identifier");
		assertThat(descriptionAnnotation.example()).isEqualTo(new String[]{"An identifier for the network access point of the user device for the audit event"});
	}

	@Test
	void testExtractDescription_noDescriptionAnnotation() {
		SearchParameter parameter = new SearchParameter();
		Annotation[] annotations = new Annotation[]{};

		// Should simply do nothing (no exception thrown, no change)
		MethodUtil.extractDescription(parameter, annotations);

		assertNull(parameter.getDescription());
	}

	// -------------------------------------------------------------------------
	// 2. Testing MethodUtil.getResourceParameters() - Annotation Scenarios
	// -------------------------------------------------------------------------

	@Test
	void testRequiredParam() {
		final List<IParameter> params = getMethodAndExecute("methodWithRequiredParam", String.class);

		assertEquals(1, params.size());
		assertInstanceOf(SearchParameter.class, params.get(0));
		final SearchParameter sp = (SearchParameter) params.get(0);
		assertEquals("requiredParam", sp.getName());
		assertTrue(sp.isRequired());
	}

	@Test
	void testOptionalParam() {
		final List<IParameter> params = getMethodAndExecute("methodWithOptionalParam", String.class);

		assertEquals(1, params.size());
		assertInstanceOf(SearchParameter.class, params.get(0));
		final SearchParameter sp = (SearchParameter) params.get(0);
		assertEquals("optionalParam", sp.getName());
		assertFalse(sp.isRequired());
	}

	@Test
	void invalidOptionalParam() {
		assertThrows(ConfigurationException.class, () ->
		 getMethodAndExecute("invalidOptionalParamInteger", Integer.class));
	}

	@Test
	void testResourceParamAsIBaseResource() {
		final List<IParameter> params = getMethodAndExecute("methodWithResourceParam", IBaseResource.class);

		assertEquals(1, params.size());
		assertInstanceOf(ResourceParameter.class, params.get(0));
		ResourceParameter rp = (ResourceParameter) params.get(0);
		assertNotNull(rp);
	}

	@Test
	void testResourceParamAsString() {
		final List<IParameter> params = getMethodAndExecute("methodWithResourceParamString", String.class);

		assertEquals(1, params.size());
		assertInstanceOf(ResourceParameter.class, params.get(0));
	}

	@Test
	void testResourceParamAsByteArray() {
		final List<IParameter> params = getMethodAndExecute("methodWithResourceParamByteArray", byte[].class);

		assertEquals(1, params.size());
		assertInstanceOf(ResourceParameter.class, params.get(0));
	}

	@Test
	void testServletRequestParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithServletRequest", ServletRequest.class);

		assertEquals(1, params.size());
		assertInstanceOf(ServletRequestParameter.class, params.get(0));
	}

	@Test
	void testServletResponseParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithServletResponse", ServletResponse.class);

		assertEquals(1, params.size());
		assertInstanceOf(ServletResponseParameter.class, params.get(0));
	}

	@Test
	void testRequestDetailsParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithRequestDetails", RequestDetails.class);

		assertEquals(1, params.size());
		assertInstanceOf(RequestDetailsParameter.class, params.get(0));
	}

	@Test
	void testInterceptorBroadcasterParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithInterceptorBroadcaster", IInterceptorBroadcaster.class);

		assertEquals(1, params.size());
		assertInstanceOf(InterceptorBroadcasterParameter.class, params.get(0));
	}

	@Test
	void testIdParamParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithIdParam", String.class);

		// Should produce a NullParameter based on "IdParam"
		assertEquals(1, params.size());
		assertInstanceOf(NullParameter.class, params.get(0));
	}

	@Test
	void testServerBaseParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithServerBase", String.class);

		// Expect ServerBaseParamBinder
		assertEquals(1, params.size());
		assertInstanceOf(ServerBaseParamBinder.class, params.get(0));
	}

	@Test
	void testElementsParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithElements", String.class);

		// Expect ElementsParameter
		assertEquals(1, params.size());
		assertInstanceOf(ElementsParameter.class, params.get(0));
	}

	@Test
	void testSinceParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithSince", Date.class);

		// Expect SinceParameter
		assertEquals(1, params.size());
		assertInstanceOf(SinceParameter.class, params.get(0));
	}

	@Test
	void testAtParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithAt", Date.class);

		// Expect AtParameter
		assertEquals(1, params.size());
		assertInstanceOf(AtParameter.class, params.get(0));
	}

	@Test
	void testCountParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithCount", Integer.class);

		// Expect CountParameter
		assertEquals(1, params.size());
		assertInstanceOf(CountParameter.class, params.get(0));
	}

	@Test
	void testOffsetParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithOffset", Integer.class);

		// Expect OffsetParameter
		assertEquals(1, params.size());
		assertInstanceOf(OffsetParameter.class, params.get(0));
	}

	@Test
	void testSummaryEnumParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithSummaryEnum", SummaryEnum.class);

		// Expect SummaryEnumParameter
		assertEquals(1, params.size());
		assertInstanceOf(SummaryEnumParameter.class, params.get(0));
	}

	@Test
	void testPatchTypeParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithPatchType", PatchTypeEnum.class);

		// Expect PatchTypeParameter
		assertEquals(1, params.size());
		assertInstanceOf(PatchTypeParameter.class, params.get(0));
	}

	@Test
	void testSearchContainedModeParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithSearchContainedMode", SearchContainedModeEnum.class);

		// Expect SearchContainedModeParameter
		assertEquals(1, params.size());
		assertInstanceOf(SearchContainedModeParameter.class, params.get(0));
	}

	@Test
	void testSearchTotalModeParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithSearchTotalMode", SearchTotalModeEnum.class);

		// Expect SearchTotalModeParameter
		assertEquals(1, params.size());
		assertInstanceOf(SearchTotalModeParameter.class, params.get(0));
	}

	@Test
	void testGraphQLQueryUrlParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithGraphQLQueryUrl", String.class);

		// Expect GraphQLQueryUrlParameter
		assertEquals(1, params.size());
		assertInstanceOf(GraphQLQueryUrlParameter.class, params.get(0));
	}

	@Test
	void testGraphQLQueryBodyParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithGraphQLQueryBody", String.class);

		// Expect GraphQLQueryBodyParameter
		assertEquals(1, params.size());
		assertInstanceOf(GraphQLQueryBodyParameter.class, params.get(0));
	}

	@Test
	void testSortParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithSort", SortSpec.class);

		// Expect SortParameter
		assertEquals(1, params.size());
		assertInstanceOf(SortParameter.class, params.get(0));
	}

	@Test
	void testInvalidSortSpec() {
		assertThrows(ConfigurationException.class, () ->
			getMethodAndExecute("invalidMethodWithSort", String.class));
	}


	@Test
	void testTransactionParameter() {
		final List<IParameter> params = getMethodAndExecute("methodWithTransactionParam", IBaseResource.class);

		// Expect TransactionParameter
		assertEquals(1, params.size());
		assertInstanceOf(TransactionParameter.class, params.get(0));
	}

	@Test
	void testConditionalParamBinder() {
		final List<IParameter> params = getMethodAndExecute("methodWithConditionalUrlParam", String.class);

		// Expect ConditionalParamBinder
		assertEquals(1, params.size());
		assertInstanceOf(ConditionalParamBinder.class, params.get(0));
	}

	// -------------------------------------------------------------------------
	// 3. Testing MethodUtil.getResourceParameters() - OperationParam
	// -------------------------------------------------------------------------
	@Test
	void testOperationParam() {
		// This method is annotated @Operation, so any @OperationParam is recognized
		final List<IParameter> params = getMethodAndExecute("methodWithOperationParam", String.class);

		assertEquals(1, params.size());
		assertInstanceOf(OperationParameter.class, params.get(0));
		OperationParameter opParam = (OperationParameter) params.get(0);
		assertEquals("opParam", opParam.getName());
	}

	@Test
	void testOperationParamWithTypeName() {
		final List<IParameter> params = getMethodAndExecute("methodWithOperationParamAndTypeName", StringType.class);

		assertEquals(1, params.size());
		assertInstanceOf(OperationParameter.class, params.get(0));
		OperationParameter opParam = (OperationParameter) params.get(0);
		assertEquals("opParamTyped", opParam.getName());
	}

	@Test
	void invalidOperationParamWithTypeName() {
		assertThrows(ConfigurationException.class, () ->
			getMethodAndExecute("invalidMethodWithOperationParamAndTypeName", String.class));
	}

	@Test
	void testValidateModeAndProfile() {
		final List<IParameter> params = getMethodAndExecute("methodWithValidateAnnotations", ValidationModeEnum.class, String.class);

		assertEquals(2, params.size());

		// ValidateMode => OperationParameter with param name "mode"
		assertInstanceOf(OperationParameter.class, params.get(0));
		OperationParameter modeParam = (OperationParameter) params.get(0);
		assertEquals(Constants.EXTOP_VALIDATE_MODE, modeParam.getName());

		// ValidateProfile => OperationParameter with param name "profile"
		assertInstanceOf(OperationParameter.class, params.get(1));
		OperationParameter profileParam = (OperationParameter) params.get(1);
		assertEquals(Constants.EXTOP_VALIDATE_PROFILE, profileParam.getName());
	}

	// -------------------------------------------------------------------------
	// 4. Testing MethodUtil.getResourceParameters() - Edge/Exception Cases
	// -------------------------------------------------------------------------
	@Test
	void testUnknownParameter() {
		// methodWithUnknownParam has no recognized annotations => triggers error
		assertThrows(ConfigurationException.class, () ->
			 getMethodAndExecute("methodWithUnknownParam", Double.class));
	}

	@Test
	void testTagListParameter() {
		// Should produce a NullParameter, as TagList param is handled separately
		final List<IParameter> params = getMethodAndExecute("methodWithTagList", TagList.class);

		assertEquals(1, params.size());
		assertInstanceOf(NullParameter.class, params.get(0));
	}

	@Test
	void testCollectionOfCollectionParameter() {
		// This will trigger an exception due to multiple levels of collection generics
		assertThrows(ConfigurationException.class, () ->
			 getMethodAndExecute("methodWithCollectionOfCollections", List.class));
	}

	@Test
	void methodWithIPrimitiveTypeDate() {
		final List<IParameter> params = getMethodAndExecute("methodWithIPrimitiveTypeDate", IPrimitiveType.class);

		// Expect a SearchParameter
		assertEquals(1, params.size());
		assertInstanceOf(SearchParameter.class, params.get(0));
		SearchParameter sp = (SearchParameter) params.get(0);
		assertEquals("primitiveTypeDateParam", sp.getName());
	}

	@Test
	void invalidMethodWithIPrimitiveTypeDate() {
		assertThrows(ConfigurationException.class, () ->
			getMethodAndExecute("invalidMethodWithIPrimitiveTypeDate", List.class));
	}

	@Test
	void sampleMethodEmbeddedParamsRequestDetailsFirst() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST, RequestDetails.class, SampleParams.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(RequestDetailsParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new RequestDetailsParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST,ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParamsRequestDetailsLast() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST, SampleParams.class, RequestDetails.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class, RequestDetailsParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new RequestDetailsParameterToAssert()
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParamsWithFhirTypes() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, SampleParamsWithIdParam.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void paramsConversionZonedDateTime() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SIMPLE_METHOD_WITH_PARAMS_CONVERSION, ParamsWithTypeConversion.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "periodStart", SIMPLE_METHOD_WITH_PARAMS_CONVERSION,null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.START),
			 new OperationParameterToAssert(ourFhirContext, "periodEnd", SIMPLE_METHOD_WITH_PARAMS_CONVERSION, null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.END)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void paramsConversionIdTypeZonedDateTime() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION, ParamsWithIdParamAndTypeConversion.class);

		assertThat(resourceParameters)
			 .isNotNull()
			 .isNotEmpty()
			 .hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "periodStart", SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION,null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.START),
			 new OperationParameterToAssert(ourFhirContext, "periodEnd", SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION, null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.END)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	private List<IParameter> getMethodAndExecute(String theMethodName, Class<?>... theParamClasses) {
		return MethodUtil.getResourceParameters(
			ourFhirContext,
			 getMethod(theMethodName, theParamClasses),
			myProvider);
	}

	private Method getMethod(String theTheMethodName, Class<?>... theTheParamClasses) {
		return myMethodAndOperationParamsInnerClassesAndMethods.getDeclaredMethod(myProvider, theTheMethodName, theTheParamClasses);
	}

	private boolean assertParametersEqual(List<? extends IParameterToAssert> theExpectedParameters, List<? extends IParameter> theActualParameters) {
		if (theActualParameters.size() != theExpectedParameters.size()) {
			fail("Expected parameters size does not match actual parameters size");
			return false;
		}

		for (int i = 0; i < theActualParameters.size(); i++) {
			final IParameterToAssert expectedParameter = theExpectedParameters.get(i);
			final IParameter actualParameter = theActualParameters.get(i);

			if (! assertParametersEqual(expectedParameter, actualParameter)) {
				return false;
			}
		}

		return true;
	}

	private boolean assertParametersEqual(IParameterToAssert theExpectedParameter, IParameter theActualParameter) {
		if (theExpectedParameter instanceof NullParameterToAssert && theActualParameter instanceof NullParameter) {
			return true;
		}

		if (theExpectedParameter instanceof RequestDetailsParameterToAssert && theActualParameter instanceof RequestDetailsParameter) {
			return true;
		}

		if (theExpectedParameter instanceof ServletRequestParameterToAssert && theActualParameter instanceof ServletRequestParameter) {
			return true;
		}

		if (theExpectedParameter instanceof OperationParameterToAssert expectedOperationParameter && theActualParameter instanceof OperationParameter actualOperationParameter) {
			assertThat(actualOperationParameter.getOperationName()).isEqualTo(expectedOperationParameter.myOperationName());
			assertThat(actualOperationParameter.getContext().getVersion().getVersion()).isEqualTo(expectedOperationParameter.myContext().getVersion().getVersion());
			assertThat(actualOperationParameter.getName()).isEqualTo(expectedOperationParameter.myName());
			assertThat(actualOperationParameter.getParamType()).isEqualTo(expectedOperationParameter.myParamType());
			assertThat(actualOperationParameter.getInnerCollectionType()).isEqualTo(expectedOperationParameter.myInnerCollectionType());
			assertThat(actualOperationParameter.getSourceType()).isEqualTo(expectedOperationParameter.myTypeToConvertFrom());
			assertThat(actualOperationParameter.getRangeType()).isEqualTo(expectedOperationParameter.myRangeType());

			return true;
		}

		return false;
	}

	private interface IParameterToAssert {}

	private record NullParameterToAssert() implements IParameterToAssert {
	}

	private record ServletRequestParameterToAssert() implements IParameterToAssert {
	}

	private record RequestDetailsParameterToAssert() implements IParameterToAssert {
	}

	private record OperationParameterToAssert(
		 FhirContext myContext,
		 String myName,
		 String myOperationName,
		 @SuppressWarnings("rawtypes")
		 Class<? extends Collection> myInnerCollectionType,
		 Class<?> myParameterType,
		 String myParamType,
		 Class<?> myTypeToConvertFrom,
		 OperationParameterRangeType myRangeType) implements IParameterToAssert {
	}
}
