package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
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
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Collects our lucene extended indexing data.
 *
 */
public class ExtendedLuceneIndexData {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedLuceneIndexData.class);

	final FhirContext myFhirContext;
	final SetMultimap<String, String> mySearchParamStrings = HashMultimap.create();
	final SetMultimap<String, IBaseCoding> mySearchParamTokens = HashMultimap.create();
	final SetMultimap<String, String> mySearchParamLinks = HashMultimap.create();
	final SetMultimap<String, DateSearchIndexData> mySearchParamDates = HashMultimap.create();

	public ExtendedLuceneIndexData(FhirContext theFhirContext) {
		this.myFhirContext = theFhirContext;
	}

	private <V> BiConsumer<String, V> ifNotContained(BiConsumer<String, V> theIndexWriter) {
		return (s,v) -> {
			// Ignore contained resources for now.
			if (!s.contains(".")) {
				theIndexWriter.accept(s,v);
			}
		};
	}
	public void writeIndexElements(DocumentElement theDocument) {
		HibernateSearchIndexWriter indexWriter = HibernateSearchIndexWriter.forRoot(myFhirContext, theDocument);

		ourLog.debug("Writing JPA index to Hibernate Search");

		mySearchParamStrings.forEach(ifNotContained(indexWriter::writeStringIndex));
		mySearchParamTokens.forEach(ifNotContained(indexWriter::writeTokenIndex));
		mySearchParamLinks.forEach(ifNotContained(indexWriter::writeReferenceIndex));
		// TODO MB Use RestSearchParameterTypeEnum to define templates.
		mySearchParamDates.forEach(ifNotContained(indexWriter::writeDateIndex));
	}

	public void addStringIndexData(String theSpName, String theText) {
		mySearchParamStrings.put(theSpName, theText);
	}

	/**
	 * Add if not already present.
	 */
	public void addTokenIndexDataIfNotPresent(String theSpName, String theSystem,  String theValue) {
		boolean isPresent = mySearchParamTokens.get(theSpName).stream()
			.anyMatch(c -> Objects.equals(c.getSystem(), theSystem) && Objects.equals(c.getCode(), theValue));
		if (!isPresent) {
			addTokenIndexData(theSpName, new CodingDt(theSystem, theValue));
		}
	}

	public void addTokenIndexData(String theSpName, IBaseCoding theNextValue) {
		mySearchParamTokens.put(theSpName, theNextValue);
	}

	public void addResourceLinkIndexData(String theSpName, String theTargetResourceId) {
		mySearchParamLinks.put(theSpName, theTargetResourceId);
	}

	public void addDateIndexData(String theSpName, Date theLowerBound, int theLowerBoundOrdinal, Date theUpperBound, int theUpperBoundOrdinal) {
		mySearchParamDates.put(theSpName, new DateSearchIndexData(theLowerBound, theLowerBoundOrdinal, theUpperBound, theUpperBoundOrdinal));
	}

}
