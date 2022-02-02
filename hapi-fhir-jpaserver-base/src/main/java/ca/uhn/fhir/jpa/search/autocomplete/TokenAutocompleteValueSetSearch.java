package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.ValueSetAutocompleteOptions;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

/**
 * Adapt the autocomplete result into a ValueSet suitable for our $expand extension.
 */
public class TokenAutocompleteValueSetSearch {
	private final FhirContext myFhirContext;
	private final TokenAutocompleteSearch myAutocompleteSearch;
	static final int DEFAULT_SIZE = 30;

	public TokenAutocompleteValueSetSearch(FhirContext theFhirContext, SearchSession theSession) {
		myFhirContext = theFhirContext;
		myAutocompleteSearch = new TokenAutocompleteSearch(myFhirContext, theSession);
	}

	public IBaseResource search(ValueSetAutocompleteOptions theOptions) {
		if (!Constants.PARAMQUALIFIER_TOKEN_TEXT.equals(theOptions.getSearchParamModifier())) {
			// wipmb where to validate this?  Top level?
			throw new IllegalArgumentException("Only support :text searches");
		}
		List<IBaseCoding> codings = myAutocompleteSearch
			.search(theOptions.getResourceType(), theOptions.getSearchParamCode(), theOptions.getFilter(), theOptions.getCount().orElse(DEFAULT_SIZE));

		ValueSet result = new ValueSet();
		ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
		result.setExpansion(expansion);
		for (IBaseCoding coding : codings) {
			ValueSet.ValueSetExpansionContainsComponent component = expansion.addContains();
			component.setCode(coding.getCode());
			component.setSystem(coding.getSystem());
			component.setDisplay(coding.getDisplay());
		}
		return result;
	}
}
