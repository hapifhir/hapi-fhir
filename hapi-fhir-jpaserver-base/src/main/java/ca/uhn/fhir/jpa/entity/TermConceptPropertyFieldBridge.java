package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

import java.util.Collection;

/**
 * Allows hibernate search to index individual concepts' properties
 */
public class TermConceptPropertyFieldBridge implements FieldBridge, StringBridge {

	public static final String PROP_PREFIX = "PROP__";

	/**
	 * Constructor
	 */
	public TermConceptPropertyFieldBridge() {
		super();
	}

	@Override
	public String objectToString(Object theObject) {
		return theObject.toString();
	}

	@Override
	public void set(String theName, Object theValue, Document theDocument, LuceneOptions theLuceneOptions) {
		Collection<TermConceptProperty> properties = (Collection<TermConceptProperty>) theValue;
		if (properties != null) {
			for (TermConceptProperty next : properties) {
				String propValue = next.getKey() + "=" + next.getValue();
				theLuceneOptions.addFieldToDocument(theName, propValue, theDocument);
			}
		}
	}
}
