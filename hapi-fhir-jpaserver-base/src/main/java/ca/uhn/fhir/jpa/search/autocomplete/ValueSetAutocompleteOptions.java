package ca.uhn.fhir.jpa.search.autocomplete;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValueSetAutocompleteOptions {

	private final String myResourceType;
	private final String mySearchParamCode;
	private final String mySearchParamModifier;
	private final String myFilter;
	private final Integer myCount;

	static final List<String> ourSupportedModifiers = Arrays.asList("", TokenParamModifier.TEXT.getBareModifier());

	public ValueSetAutocompleteOptions(String theContext, String theFilter, Integer theCount) {
		myFilter = theFilter;
		myCount = theCount;
		int separatorIdx = theContext.indexOf('.');
		String codeWithPossibleModifier;
		if (separatorIdx >= 0) {
			myResourceType = theContext.substring(0, separatorIdx);
			codeWithPossibleModifier = theContext.substring(separatorIdx + 1);
		} else {
			myResourceType = null;
			codeWithPossibleModifier = theContext;
		}
		int modifierIdx = codeWithPossibleModifier.indexOf(':');
		if (modifierIdx >= 0) {
			mySearchParamCode = codeWithPossibleModifier.substring(0, modifierIdx);
			mySearchParamModifier = codeWithPossibleModifier.substring(modifierIdx + 1);
		} else {
			mySearchParamCode = codeWithPossibleModifier;
			mySearchParamModifier = null;
		}
	}

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
		if (theContext == null || theContext.isEmpty()) {
			throw new InvalidRequestException(Msg.code(2021) + "$expand with contexDirection='existing' requires a context");
		}
		String filter = theFilter == null ? null : theFilter.getValue();
		ValueSetAutocompleteOptions result = new ValueSetAutocompleteOptions(theContext.getValue(), filter, IPrimitiveType.toValueOrNull(theCount));

		if (!ourSupportedModifiers.contains(defaultString(result.getSearchParamModifier()))) {
			throw new InvalidRequestException(Msg.code(2069) + "$expand with contexDirection='existing' only supports plain token search, or the :text modifier.  Received " + result.getSearchParamModifier());
		}

		return result;
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
