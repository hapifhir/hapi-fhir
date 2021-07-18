package ca.uhn.fhir.jpa.term.loinc;

import javax.validation.constraints.NotNull;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

public class LinguisticVariant {

	private String myId;
	private String myIsoLanguage;
	private String myIsoCountry;
	private String myLanguageName;

	public LinguisticVariant(@NotNull String theId, @NotNull String theIsoLanguage, @NotNull String theIsoCountry, @NotNull String theLanguageName) {
		this.myId = theId;
		this.myIsoLanguage = theIsoLanguage;
		this.myIsoCountry = theIsoCountry;
		this.myLanguageName = theLanguageName;
	}

	public String getLinguisticVariantFileName() {
		return myIsoLanguage + myIsoCountry + myId + "LinguisticVariant.csv";
	}

	public String getLanguageName() {
		return myLanguageName;
	}
	
	public String getLanguageCode() {
		return myIsoLanguage + "-" + myIsoCountry;
	}
}
