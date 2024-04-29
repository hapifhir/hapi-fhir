/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Collects our HSearch extended indexing data.
 *
 */
public class ExtendedHSearchIndexData {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedHSearchIndexData.class);

	final FhirContext myFhirContext;
	final StorageSettings myStorageSettings;

	final SetMultimap<String, String> mySearchParamStrings = HashMultimap.create();
	final SetMultimap<String, IBaseCoding> mySearchParamTokens = HashMultimap.create();
	final SetMultimap<String, BigDecimal> mySearchParamNumbers = HashMultimap.create();
	final SetMultimap<String, String> mySearchParamLinks = HashMultimap.create();
	final SetMultimap<String, String> mySearchParamUri = HashMultimap.create();
	final SetMultimap<String, DateSearchIndexData> mySearchParamDates = HashMultimap.create();
	final SetMultimap<String, QuantitySearchIndexData> mySearchParamQuantities = HashMultimap.create();
	final SetMultimap<String, CompositeSearchIndexData> mySearchParamComposites = HashMultimap.create();
	private String myForcedId;
	private String myResourceJSON;
	private IBaseResource myResource;

	public ExtendedHSearchIndexData(
			FhirContext theFhirContext, StorageSettings theStorageSettings, IBaseResource theResource) {
		this.myFhirContext = theFhirContext;
		this.myStorageSettings = theStorageSettings;
		myResource = theResource;
	}

	private <V> BiConsumer<String, V> ifNotContained(BiConsumer<String, V> theIndexWriter) {
		return (s, v) -> {
			// Ignore contained resources for now.
			if (!s.contains(".")) {
				theIndexWriter.accept(s, v);
			}
		};
	}

	/**
	 * Write the index document.
	 *
	 * Called by Hibernate Search after the ResourceTable entity has been flushed/committed.
	 * Keep this in sync with the schema defined in {@link SearchParamTextPropertyBinder}
	 *
	 * @param theDocument the Hibernate Search document for ResourceTable
	 */
	public void writeIndexElements(DocumentElement theDocument) {
		HSearchIndexWriter indexWriter = HSearchIndexWriter.forRoot(myStorageSettings, theDocument);

		ourLog.debug("Writing JPA index to Hibernate Search");

		// todo can this be moved back to ResourceTable as a computed field to merge with myId?
		theDocument.addValue("myForcedId", myForcedId);

		if (myResourceJSON != null) {
			theDocument.addValue("myRawResource", myResourceJSON);
		}

		mySearchParamStrings.forEach(ifNotContained(indexWriter::writeStringIndex));
		mySearchParamTokens.forEach(ifNotContained(indexWriter::writeTokenIndex));
		mySearchParamLinks.forEach(ifNotContained(indexWriter::writeReferenceIndex));
		// we want to receive the whole entry collection for each invocation
		mySearchParamQuantities.forEach(ifNotContained(indexWriter::writeQuantityIndex));
		Multimaps.asMap(mySearchParamNumbers).forEach(ifNotContained(indexWriter::writeNumberIndex));
		mySearchParamDates.forEach(ifNotContained(indexWriter::writeDateIndex));
		Multimaps.asMap(mySearchParamUri).forEach(ifNotContained(indexWriter::writeUriIndex));
		Multimaps.asMap(mySearchParamComposites).forEach(indexWriter::writeCompositeIndex);
	}

	public void addStringIndexData(String theSpName, String theText) {
		mySearchParamStrings.put(theSpName, theText);
	}

	/**
	 * Add if not already present.
	 */
	public void addTokenIndexDataIfNotPresent(String theSpName, String theSystem, String theValue) {
		boolean isPresent = mySearchParamTokens.get(theSpName).stream()
				.anyMatch(c -> Objects.equals(c.getSystem(), theSystem) && Objects.equals(c.getCode(), theValue));
		if (!isPresent) {
			addTokenIndexData(theSpName, new CodingDt(theSystem, theValue));
		}
	}

	public void addTokenIndexData(String theSpName, IBaseCoding theNextValue) {
		mySearchParamTokens.put(theSpName, theNextValue);
	}

	public void addUriIndexData(String theSpName, String theValue) {
		mySearchParamUri.put(theSpName, theValue);
	}

	public void addResourceLinkIndexData(String theSpName, String theTargetResourceId) {
		mySearchParamLinks.put(theSpName, theTargetResourceId);
	}

	public void addDateIndexData(
			String theSpName,
			Date theLowerBound,
			int theLowerBoundOrdinal,
			Date theUpperBound,
			int theUpperBoundOrdinal) {
		addDateIndexData(
				theSpName,
				new DateSearchIndexData(theLowerBound, theLowerBoundOrdinal, theUpperBound, theUpperBoundOrdinal));
	}

	public void addDateIndexData(String theSpName, DateSearchIndexData value) {
		mySearchParamDates.put(theSpName, value);
	}

	public SetMultimap<String, DateSearchIndexData> getDateIndexData() {
		return mySearchParamDates;
	}

	public void addNumberIndexDataIfNotPresent(String theParamName, BigDecimal theValue) {
		mySearchParamNumbers.put(theParamName, theValue);
	}

	public void addQuantityIndexData(String theSpName, QuantitySearchIndexData value) {
		mySearchParamQuantities.put(theSpName, value);
	}

	public SetMultimap<String, QuantitySearchIndexData> getQuantityIndexData() {
		return mySearchParamQuantities;
	}

	public void setForcedId(String theForcedId) {
		myForcedId = theForcedId;
	}

	public String getForcedId() {
		return myForcedId;
	}

	public void setRawResourceData(String theResourceJSON) {
		myResourceJSON = theResourceJSON;
	}

	public SetMultimap<String, CompositeSearchIndexData> getSearchParamComposites() {
		return mySearchParamComposites;
	}

	public void addCompositeIndexData(String theSearchParamName, CompositeSearchIndexData theBuildCompositeIndexData) {
		mySearchParamComposites.put(theSearchParamName, theBuildCompositeIndexData);
	}
}
