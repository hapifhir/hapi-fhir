package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TerserUtil;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapt the autocomplete result into a ValueSet suitable for our $expand extension.
 */
public class ValueSetAutocompleteSearch {
	private final FhirContext myFhirContext;
	private final TokenAutocompleteSearch myAutocompleteSearch;
	static final int DEFAULT_SIZE = 30;

	public ValueSetAutocompleteSearch(FhirContext theFhirContext, SearchSession theSession) {
		myFhirContext = theFhirContext;
		myAutocompleteSearch = new TokenAutocompleteSearch(myFhirContext, theSession);
	}

	public IBaseResource search(ValueSetAutocompleteOptions theOptions) {
		List<TokenAutocompleteHit> aggEntries = myAutocompleteSearch.search(theOptions.getResourceType(), theOptions.getSearchParamCode(), theOptions.getFilter(), theOptions.getSearchParamModifier(), (int) theOptions.getCount().orElse(DEFAULT_SIZE));

		ValueSet result = new ValueSet();
		ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
		result.setExpansion(expansion);
		aggEntries.stream()
			.map(this::makeCoding)
			.forEach(expansion::addContains);

		return result;
	}

	ValueSet.ValueSetExpansionContainsComponent makeCoding(TokenAutocompleteHit theSearchHit) {
		TokenParam tokenParam = new TokenParam();
		tokenParam.setValueAsQueryToken(myFhirContext, null, null, theSearchHit.mySystemCode);

		// R4 only for now.
//		IBaseCoding coding = TerserUtil.newElement(myFhirContext, "Coding");
		ValueSet.ValueSetExpansionContainsComponent coding = new ValueSet.ValueSetExpansionContainsComponent();
		coding.setCode(tokenParam.getValue());
		coding.setSystem(tokenParam.getSystem());
		coding.setDisplay(theSearchHit.myDisplayText);

		return coding;
	}
}
