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
import ca.uhn.fhir.mdm.api.MdmMatchEvaluation;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;

public enum MdmSimilarityEnum {
	JARO_WINKLER(new HapiStringSimilarity(new JaroWinkler())),
	COSINE(new HapiStringSimilarity(new Cosine())),
	JACCARD(new HapiStringSimilarity(new Jaccard())),
	LEVENSCHTEIN(new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE(new HapiStringSimilarity(new SorensenDice())),
	NUMERIC_JARO_WINKLER(new HapiNumericSimilarity(new JaroWinkler())),
	NUMERIC_COSINE(new HapiNumericSimilarity(new Cosine())),
	NUMERIC_JACCARD(new HapiNumericSimilarity(new Jaccard())),
	NUMERIC_LEVENSCHTEIN(new HapiNumericSimilarity(new NormalizedLevenshtein())),
	NUMERIC_SORENSEN_DICE(new HapiNumericSimilarity(new SorensenDice()));

	private final IMdmFieldSimilarity myMdmFieldSimilarity;

	MdmSimilarityEnum(IMdmFieldSimilarity theMdmFieldSimilarity) {
		myMdmFieldSimilarity = theMdmFieldSimilarity;
	}

	public MdmMatchEvaluation match(
			FhirContext theFhirContext,
			IBase theLeftBase,
			IBase theRightBase,
			boolean theExact,
			@Nullable Double theThreshold) {
		return matchBySimilarity(
				myMdmFieldSimilarity, theFhirContext, theLeftBase, theRightBase, theExact, theThreshold);
	}

	private MdmMatchEvaluation matchBySimilarity(
			IMdmFieldSimilarity theSimilarity,
			FhirContext theFhirContext,
			IBase theLeftBase,
			IBase theRightBase,
			boolean theExact,
			Double theThreshold) {
		double similarityResult = theSimilarity.similarity(theFhirContext, theLeftBase, theRightBase, theExact);
		return new MdmMatchEvaluation(similarityResult >= theThreshold, similarityResult);
	}
}
