package ca.uhn.fhir.repository.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiVersionResourceMatcher implements IResourceMatcher {
	private static final Map<FhirContext, IFhirPath> ourFhirPathCache = new ConcurrentHashMap<>();
	private final Map<SPPathKey, IFhirPath.IParsedExpression> myExpressionCache = new HashMap<>();
	private final Map<String, RuntimeSearchParam> myCustomSearchParams = new HashMap<>();

	private final FhirContext myFhirContext;

	public MultiVersionResourceMatcher(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public IFhirPath getEngine() {
		return ourFhirPathCache.computeIfAbsent(myFhirContext, FhirContext::newFhirPath);
	}

	@Override
	public FhirContext getContext() {
		return myFhirContext;
	}

	@Override
	public Map<SPPathKey, IFhirPath.IParsedExpression> getExpressionCache() {
		return myExpressionCache;
	}

	@Override
	public void addCustomParameter(RuntimeSearchParam searchParam) {
		this.myCustomSearchParams.put(searchParam.getName(), searchParam);
	}

	@Override
	public Map<String, RuntimeSearchParam> getCustomParameters() {
		return this.myCustomSearchParams;
	}

	@Override
	public DateRangeParam getDateRange(ICompositeType type) {
		throw new UnsupportedOperationException(Msg.code(2738) + "Date range extraction is not supported for type");
	}

	@Override
	public List<TokenParam> getCodes(IBase theCodeElement) {
		String elementTypeName =
				myFhirContext.getElementDefinition(theCodeElement.getClass()).getName();
		switch (elementTypeName) {
			case "Coding" -> {
				var terser = myFhirContext.newTerser();
				TokenParam e = codingToTokenParam(terser, theCodeElement);
				return List.of(e);
			}
			case "code" -> {
				String codeValue = ((IPrimitiveType<?>) theCodeElement).getValueAsString();
				return List.of(new TokenParam(codeValue));
			}
			case "CodeableConcept" -> {
				var terser = myFhirContext.newTerser();
				return terser.getValues(theCodeElement, "codeing").stream()
						.map(coding -> codingToTokenParam(terser, coding))
						.toList();
			}
			default -> throw new UnsupportedOperationException(
					"Expected element of type Coding, CodeType, or CodeableConcept, found " + elementTypeName);
		}
	}

	@Nonnull
	private static TokenParam codingToTokenParam(FhirTerser theTerser, IBase theCodeElement) {
		String system = theTerser.getSinglePrimitiveValueOrNull(theCodeElement, "system");
		String code = theTerser.getSinglePrimitiveValueOrNull(theCodeElement, "code");

		return new TokenParam(system, code);
	}
}
