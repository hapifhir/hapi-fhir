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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

/**
 * Adapt the autocomplete result into a ValueSet suitable for our $expand extension.
 */
public class ValueSetAutocompleteSearch {
	private final FhirContext myFhirContext;
	private final ModelConfig myModelConfig;
	private final TokenAutocompleteSearch myAutocompleteSearch;
	static final int DEFAULT_SIZE = 30;

	public ValueSetAutocompleteSearch(FhirContext theFhirContext, ModelConfig theModelConfig, SearchSession theSession) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myAutocompleteSearch = new TokenAutocompleteSearch(myFhirContext, myModelConfig, theSession);
	}

	public IBaseResource search(ValueSetAutocompleteOptions theOptions) {
		List<TokenAutocompleteHit> aggEntries = myAutocompleteSearch.search(theOptions.getResourceType(), theOptions.getSearchParamCode(), theOptions.getFilter(), theOptions.getSearchParamModifier(), (int) theOptions.getCount().orElse(DEFAULT_SIZE));

		ValueSet result = new ValueSet();
		ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
		result.setExpansion(expansion);
		result.setStatus(Enumerations.PublicationStatus.ACTIVE);
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
