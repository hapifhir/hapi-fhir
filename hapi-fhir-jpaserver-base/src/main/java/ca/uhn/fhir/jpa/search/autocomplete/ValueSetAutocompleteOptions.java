package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValueSetAutocompleteOptions {

	private String myResourceType;
	private String mySearchParamCode;
	private String mySearchParamModifier;
	private String myFilter;
	private Integer myCount;

	public static ValueSetAutocompleteOptions validateAndParseOptions(
		DaoConfig theDaoConfig,
		IPrimitiveType<String> theContext,
		IPrimitiveType<String> theFilter,
		IPrimitiveType<Integer> theCount,
		IIdType theId,
		IPrimitiveType<String> theUrl,
		IBaseResource theValueSet)
	{
		boolean haveId = theId != null && theId.hasIdPart();
		boolean haveIdentifier = theUrl != null && isNotBlank(theUrl.getValue());
		boolean haveValueSet = theValueSet != null && !theValueSet.isEmpty();
		if (haveId || haveIdentifier || haveValueSet) {
			throw new InvalidRequestException(Msg.code(2020) + "$expand with contexDirection='existing' is only supported at the type leve. It is not supported at instance level, with a url specified, or with a ValueSet .");
		}
		if (!theDaoConfig.isAdvancedLuceneIndexing()) {
			throw new InvalidRequestException(Msg.code(2022) + "$expand with contexDirection='existing' requires Extended Lucene Indexing.");
		}
		ValueSetAutocompleteOptions result = new ValueSetAutocompleteOptions();

		result.parseContext(theContext);
		result.myFilter =
			theFilter == null ? null : theFilter.getValue();
		result.myCount = IPrimitiveType.toValueOrNull(theCount);

		return result;
	}

	private void parseContext(IPrimitiveType<String> theContextWrapper) {
		if (theContextWrapper == null || theContextWrapper.isEmpty()) {
			throw new InvalidRequestException(Msg.code(2021) + "$expand with contexDirection='existing' requires a context");
		}
		String theContext = theContextWrapper.getValue();
		int separatorIdx = theContext.indexOf('.');
		String codeWithPossibleModifier;
		if (separatorIdx >= 0) {
			myResourceType = theContext.substring(0, separatorIdx);
			codeWithPossibleModifier = theContext.substring(separatorIdx + 1);
		} else {
			codeWithPossibleModifier = theContext;
		}
		int modifierIdx = codeWithPossibleModifier.indexOf(':');
		if (modifierIdx >= 0) {
			mySearchParamCode = codeWithPossibleModifier.substring(0, modifierIdx);
			mySearchParamModifier = codeWithPossibleModifier.substring(modifierIdx + 1);
		} else {
			mySearchParamCode = codeWithPossibleModifier;
		}

	}

	public String getResourceType() {
		return myResourceType;
	}

	public String getSearchParamCode() {
		return mySearchParamCode;
	}

	public String getSearchParamModifier() {
		return mySearchParamModifier;
	}

	public String getFilter() {
		return myFilter;
	}

	public Optional<Integer> getCount() {
		return Optional.ofNullable(myCount);
	}
}
