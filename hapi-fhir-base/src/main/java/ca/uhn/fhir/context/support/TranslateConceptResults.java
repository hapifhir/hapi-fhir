package ca.uhn.fhir.context.support;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class TranslateConceptResults {
	private List<TranslateConceptResult> myResults;
	private String myMessage;
	private boolean myResult;

	public TranslateConceptResults() {
		super();
		myResults = new ArrayList<>();
	}

	public List<TranslateConceptResult> getResults() {
		return myResults;
	}

	public void setResults(List<TranslateConceptResult> theResults) {
		myResults = theResults;
	}

	public String getMessage() {
		return myMessage;
	}

	public void setMessage(String theMessage) {
		myMessage = theMessage;
	}

	public boolean getResult() {
		return myResult;
	}

	public void setResult(boolean theMatched) {
		myResult = theMatched;
	}

	public int size() {
		return getResults().size();
	}

	public boolean isEmpty() {
		return getResults().isEmpty();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		TranslateConceptResults that = (TranslateConceptResults) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myResults, that.myResults);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(myResults);
		return b.toHashCode();
	}
}
