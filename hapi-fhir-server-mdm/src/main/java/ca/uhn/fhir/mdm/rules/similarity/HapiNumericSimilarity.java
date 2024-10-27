/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.similarity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.phonetic.NumericEncoder;
import ca.uhn.fhir.mdm.rules.matcher.util.StringMatcherUtils;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class HapiNumericSimilarity extends HapiStringSimilarity {
	private final NumericEncoder encoder = new NumericEncoder();

	public HapiNumericSimilarity(NormalizedStringSimilarity theStringSimilarity) {
		super(theStringSimilarity);
	}

	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {

		if (theLeftBase instanceof IPrimitiveType && theRightBase instanceof IPrimitiveType) {
			String leftString =
					encoder.encode(StringMatcherUtils.extractString((IPrimitiveType<?>) theLeftBase, theExact));
			String rightString =
					encoder.encode(StringMatcherUtils.extractString((IPrimitiveType<?>) theRightBase, theExact));

			return myStringSimilarity.similarity(leftString, rightString);
		}
		return 0.0;
	}
}
