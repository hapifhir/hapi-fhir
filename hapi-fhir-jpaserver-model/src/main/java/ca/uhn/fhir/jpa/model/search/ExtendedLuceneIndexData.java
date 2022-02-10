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
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

/**
 * Collects our lucene extended indexing data.
 *
 */
public class ExtendedLuceneIndexData {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedLuceneIndexData.class);

	final FhirContext myFhirContext;
	final SetMultimap<String, String> mySearchParamStrings = HashMultimap.create();
	final SetMultimap<String, TokenParam> mySearchParamTokens = HashMultimap.create();
	final SetMultimap<String, String> mySearchParamLinks = HashMultimap.create();

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
	}

	public void addStringIndexData(String theSpName, String theText) {
		mySearchParamStrings.put(theSpName, theText);
	}

	public void addTokenIndexData(String theSpName, String theSystem,  String theValue) {
		mySearchParamTokens.put(theSpName, new TokenParam(theSystem, theValue));
	}

	public void addResourceLinkIndexData(String theSpName, String theTargetResourceId){
		mySearchParamLinks.put(theSpName, theTargetResourceId);
	}
}
