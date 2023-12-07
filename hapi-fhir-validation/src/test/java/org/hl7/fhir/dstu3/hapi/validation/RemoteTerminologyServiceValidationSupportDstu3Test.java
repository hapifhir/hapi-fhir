package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.LookupCodeUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.context.support.IValidationSupport.BaseConceptProperty;
import static ca.uhn.fhir.context.support.IValidationSupport.CodingConceptProperty;
import static ca.uhn.fhir.context.support.IValidationSupport.ConceptDesignation;
import static ca.uhn.fhir.context.support.IValidationSupport.ConceptPropertyTypeEnum;
import static ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import static ca.uhn.fhir.context.support.IValidationSupport.StringConceptProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoteTerminologyServiceValidationSupportDstu3Test {
	private static final String DISPLAY = "DISPLAY";
	private static final String LANGUAGE = "en";
	private static final String CODE_SYSTEM = "CODE_SYS";

	private static final String CODE_SYSTEM_VERSION = "CODE_SYS_VERSION";
	private static final String CODE_SYSTEM_NAME = "Code System";
	private static final String CODE = "CODE";

	private static final FhirContext ourCtx = FhirContext.forDstu3();

	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;
	private MyCodeSystemProvider myCodeSystemProvider;

	@BeforeEach
	public void before() {
		myCodeSystemProvider = new MyCodeSystemProvider();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	public static Stream<Arguments> parametersPropertiesAndDesignations() {
		return LookupCodeUtil.parametersPropertiesAndDesignations();
	}

	@ParameterizedTest
	@MethodSource(value = "parametersPropertiesAndDesignations")
	public void testLookupCode_forCodeSystemWithAllParams_returnsCorrectParameters(
			Collection<BaseConceptProperty> theConceptProperties,
			Collection<BaseConceptProperty> theConceptPropertiesForLookup,
			Collection<ConceptDesignation> theConceptDesignations) {
		myCodeSystemProvider.myNextLookupCodeResult = new LookupCodeResult()
			.setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeIsAbstract(false);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemVersion(CODE_SYSTEM_VERSION);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeDisplay(DISPLAY);

		theConceptProperties.forEach(p -> myCodeSystemProvider.myNextLookupCodeResult.getProperties().add(p));
		theConceptDesignations.forEach(d -> myCodeSystemProvider.myNextLookupCodeResult.getDesignations().add(d));

		LookupCodeResult outcome = mySvc.lookupCode(null,
			new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE,
				theConceptPropertiesForLookup.stream().map(BaseConceptProperty::getPropertyName).collect(Collectors.toList())));
		assertNotNull(outcome, "Call to lookupCode() should return a non-NULL result!");

		assertEquals(CODE, myCodeSystemProvider.myLastCode.asStringValue());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());

		Map<String, BaseConceptProperty> conceptPropertyMap =
			(theConceptPropertiesForLookup.isEmpty() ? theConceptProperties : theConceptPropertiesForLookup)
				.stream().collect(Collectors.toMap(BaseConceptProperty::getPropertyName, Function.identity()));
		Map<String, ConceptDesignation> theConceptDesignationMap = theConceptDesignations.stream().collect(
			Collectors.toMap(c -> c.getUseSystem() + "|" + c.getUseCode(), Function.identity()));

		for (Parameters.ParametersParameterComponent parameterComponent : myCodeSystemProvider.myNextReturnParams.getParameter()) {
			String parameterName = parameterComponent.getName();
			Type parameterValue = parameterComponent.getValue();
			switch (parameterName) {
				case "name" ->
					assertEquals(parameterValue.toString(), myCodeSystemProvider.myNextLookupCodeResult.getCodeSystemDisplayName());
				case "display" ->
					assertEquals(parameterValue.toString(), myCodeSystemProvider.myNextLookupCodeResult.getCodeDisplay());
				case "version" ->
					assertEquals(parameterValue.toString(), myCodeSystemProvider.myNextLookupCodeResult.getCodeSystemVersion());
				case "abstract" ->
					assertEquals(((BooleanType) parameterValue).getValue(), myCodeSystemProvider.myNextLookupCodeResult.isCodeIsAbstract());
				case "property" -> {
					final Iterator<Parameters.ParametersParameterComponent> propertyPartIterator = parameterComponent.getPart().iterator();
					final Parameters.ParametersParameterComponent propertyName = propertyPartIterator.next();
					final Parameters.ParametersParameterComponent propertyValue = propertyPartIterator.next();
					String propertyNameAsString = ((CodeType) propertyName.getValue()).getValue();
					BaseConceptProperty conceptProperty = conceptPropertyMap.remove(propertyNameAsString);
					ConceptPropertyTypeEnum propertyType = conceptProperty.getType();
					switch (propertyType) {
						case STRING -> {
							StringConceptProperty stringConceptProperty = (StringConceptProperty) conceptProperty;
							assertEquals(stringConceptProperty.getPropertyName(), propertyNameAsString);
							assertEquals(stringConceptProperty.getValue(), ((StringType) propertyValue.getValue()).getValueAsString());
						}
						case CODING -> {
							CodingConceptProperty codingConceptProperty = (CodingConceptProperty) conceptProperty;
							Coding coding = (Coding) propertyValue.getValue();
							assertEquals(codingConceptProperty.getCodeSystem(), coding.getSystem());
							assertEquals(codingConceptProperty.getCode(), coding.getCode());
							assertEquals(codingConceptProperty.getDisplay(), coding.getDisplay());
						}
					}
				}
				case "designation" -> {
					final Iterator<Parameters.ParametersParameterComponent> designationPartIterator = parameterComponent.getPart().iterator();
					final Parameters.ParametersParameterComponent designationLanguage = designationPartIterator.next();
					final Parameters.ParametersParameterComponent designationUse = designationPartIterator.next();
					final Parameters.ParametersParameterComponent designationValue = designationPartIterator.next();
					assertEquals("use", designationUse.getName());
					Coding coding = (Coding) designationUse.getValue();
					assertNotNull(coding, "Coding value returned via designation use should NOT be NULL!");
					String key = coding.getSystem() + "|" + coding.getCode();
					ConceptDesignation conceptDesignation = theConceptDesignationMap.remove(key);
					assertEquals("language", designationLanguage.getName());
					assertEquals(conceptDesignation.getLanguage(), designationLanguage.getValue().toString());
					assertEquals("value", designationValue.getName());
					assertEquals(conceptDesignation.getValue(), designationValue.getValue().toString());
				}
			}
		}

		assertTrue(conceptPropertyMap.isEmpty());
		assertTrue(theConceptDesignationMap.isEmpty());
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private UriType myLastUrl;
		private CodeType myLastCode;
		private Parameters myNextReturnParams;
		private LookupCodeResult myNextLookupCodeResult;

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name="name", type=StringType.class, min=1),
			@OperationParam(name="version", type=StringType.class, min=0),
			@OperationParam(name="display", type=StringType.class, min=1),
			@OperationParam(name="abstract", type=BooleanType.class, min=1),
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode,
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			myLastCode = theCode;
			myLastUrl = theSystem;

			myNextReturnParams = (Parameters)myNextLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}
}
