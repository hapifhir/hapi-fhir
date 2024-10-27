package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class RemoteTerminologyUtil {
	private RemoteTerminologyUtil() {}

	public static IBaseParameters buildTranslateInputParameters(
			FhirContext fhirContext, IValidationSupport.TranslateCodeRequest theRequest) {
		IBaseParameters params = ParametersUtil.newInstance(fhirContext);
		if (!StringUtils.isEmpty(theRequest.getConceptMapUrl())) {
			ParametersUtil.addParameterToParametersUri(fhirContext, params, "url", theRequest.getConceptMapUrl());
		}
		if (!StringUtils.isEmpty(theRequest.getConceptMapVersion())) {
			ParametersUtil.addParameterToParametersString(
					fhirContext, params, "conceptMapVersion", theRequest.getConceptMapVersion());
		}
		if (theRequest.getCodings() != null) {
			addCodingsToTranslateParameters(fhirContext, theRequest.getCodings(), params);
		}
		if (!StringUtils.isEmpty(theRequest.getSourceValueSetUrl())) {
			ParametersUtil.addParameterToParametersUri(
					fhirContext, params, "source", theRequest.getSourceValueSetUrl());
		}
		if (!StringUtils.isEmpty(theRequest.getTargetValueSetUrl())) {
			ParametersUtil.addParameterToParametersUri(
					fhirContext, params, "target", theRequest.getTargetValueSetUrl());
		}
		if (!StringUtils.isEmpty(theRequest.getTargetSystemUrl())) {
			ParametersUtil.addParameterToParametersUri(
					fhirContext, params, "targetsystem", theRequest.getTargetSystemUrl());
		}
		if (theRequest.isReverse()) {
			ParametersUtil.addParameterToParametersBoolean(fhirContext, params, "reverse", theRequest.isReverse());
		}

		return params;
	}

	public static void addCodingsToTranslateParameters(
			FhirContext fhirContext, List<IBaseCoding> theCodings, IBaseParameters theParams) {
		BaseRuntimeElementCompositeDefinition<?> codeableConceptDef = (BaseRuntimeElementCompositeDefinition<?>)
				Objects.requireNonNull(fhirContext.getElementDefinition("CodeableConcept"));
		BaseRuntimeChildDefinition codings = codeableConceptDef.getChildByName("coding");
		BaseRuntimeElementCompositeDefinition<?> codingDef = (BaseRuntimeElementCompositeDefinition<?>)
				Objects.requireNonNull(fhirContext.getElementDefinition("Coding"));
		BaseRuntimeChildDefinition codingSystemChild = codingDef.getChildByName("system");
		BaseRuntimeChildDefinition codingCodeChild = codingDef.getChildByName("code");
		BaseRuntimeElementDefinition<IPrimitiveType<?>> systemDef =
				(RuntimePrimitiveDatatypeDefinition) fhirContext.getElementDefinition("uri");
		BaseRuntimeElementDefinition<IPrimitiveType<?>> codeDef =
				(RuntimePrimitiveDatatypeDefinition) fhirContext.getElementDefinition("code");

		IBase codeableConcept = codeableConceptDef.newInstance();

		for (IBaseCoding aCoding : theCodings) {
			IBaseCoding newCoding = (IBaseCoding) codingDef.newInstance();

			IPrimitiveType<?> newSystem = systemDef.newInstance(aCoding.getSystem());
			codingSystemChild.getMutator().addValue(newCoding, newSystem);
			IPrimitiveType<?> newCode = codeDef.newInstance(aCoding.getCode());
			codingCodeChild.getMutator().addValue(newCoding, newCode);

			codings.getMutator().addValue(codeableConcept, newCoding);
		}

		ParametersUtil.addParameterToParameters(fhirContext, theParams, "codeableConcept", codeableConcept);
	}

	public static TranslateConceptResults translateOutcomeToResults(FhirContext fhirContext, IBaseParameters outcome) {
		Optional<String> result = ParametersUtil.getNamedParameterValueAsString(fhirContext, outcome, "result");
		Optional<String> message = ParametersUtil.getNamedParameterValueAsString(fhirContext, outcome, "message");
		List<IBase> matches = ParametersUtil.getNamedParameters(fhirContext, outcome, "match");

		TranslateConceptResults retVal = new TranslateConceptResults();
		if (result.isPresent()) {
			retVal.setResult(Boolean.parseBoolean(result.get()));
		}
		if (message.isPresent()) {
			retVal.setMessage(message.get());
		}
		if (!matches.isEmpty()) {
			retVal.setResults(matchesToTranslateConceptResults(fhirContext, matches));
		}

		return retVal;
	}

	private static List<TranslateConceptResult> matchesToTranslateConceptResults(
			FhirContext fhirContext, List<IBase> theMatches) {
		List<TranslateConceptResult> resultList = new ArrayList();
		for (IBase m : theMatches) {
			TranslateConceptResult match = new TranslateConceptResult();
			String equivalence = ParametersUtil.getParameterPartValueAsString(fhirContext, m, "equivalence");
			Optional<IBase> concept = ParametersUtil.getParameterPartValue(fhirContext, m, "concept");
			String source = ParametersUtil.getParameterPartValueAsString(fhirContext, m, "source");

			if (StringUtils.isNotBlank(equivalence)) {
				match.setEquivalence(equivalence);
			}

			if (concept.isPresent()) {
				IBaseCoding matchedCoding = (IBaseCoding) concept.get();
				match.setSystem(matchedCoding.getSystem());
				match.setCode(matchedCoding.getCode());
				match.setDisplay(matchedCoding.getDisplay());

				if (StringUtils.isNotBlank(source)) {
					match.setConceptMapUrl(source);
				}

				resultList.add(match);
			}
		}
		return resultList;
	}
}
