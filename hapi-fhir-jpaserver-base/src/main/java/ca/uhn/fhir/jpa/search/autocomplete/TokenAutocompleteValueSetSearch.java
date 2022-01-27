package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
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

	public TokenAutocompleteValueSetSearch(FhirContext theFhirContext, SearchSession theSession) {
		myFhirContext = theFhirContext;
		myAutocompleteSearch = new TokenAutocompleteSearch(myFhirContext, theSession);
	}

	public static class Options {
		final String myResourceType;
		final String mySearchParameterCode;
		final String mySearchText;

		public Options(String theResourceType, String theSearchParameterCode, String theSearchText) {
			myResourceType = theResourceType;
			mySearchParameterCode = theSearchParameterCode;
			mySearchText = theSearchText;
		}
	}

	public IBaseResource search(Options theOptions) {
		List<IBaseCoding> codings = myAutocompleteSearch
			.search(theOptions.myResourceType, theOptions.mySearchParameterCode, theOptions.mySearchText);

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
