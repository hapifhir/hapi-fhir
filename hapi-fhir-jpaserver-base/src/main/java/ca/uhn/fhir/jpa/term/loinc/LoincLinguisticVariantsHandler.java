package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import javax.annotation.Nonnull;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

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

public class LoincLinguisticVariantsHandler implements IZipContentsHandlerCsv {

	private final List<LinguisticVariant> myLinguisticVariants;

	public LoincLinguisticVariantsHandler(List<LinguisticVariant> thelinguisticVariants) {
		myLinguisticVariants = thelinguisticVariants;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		
		String id = trim(theRecord.get("ID"));
		if (isBlank(id)) {
			return;
		}

		String isoLanguage = trim(theRecord.get("ISO_LANGUAGE"));
		if (isBlank(isoLanguage)) {
			return;
		}
		
		String isoCountry = trim(theRecord.get("ISO_COUNTRY"));
		if (isBlank(isoCountry)) {
			return;
		}
		
		String languageName = trim(theRecord.get("LANGUAGE_NAME"));
		if (isBlank(languageName)) {
			return;
		}
		
		LinguisticVariant linguisticVariant = new LinguisticVariant(id, isoLanguage, isoCountry, languageName);
		myLinguisticVariants.add(linguisticVariant);
	}

	public static class LinguisticVariant {

		private String myId;
		private String myIsoLanguage;
		private String myIsoCountry;
		private String myLanguageName;

		public LinguisticVariant(@Nonnull String theId, @Nonnull String theIsoLanguage, @Nonnull String theIsoCountry, @Nonnull String theLanguageName) {
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

}
