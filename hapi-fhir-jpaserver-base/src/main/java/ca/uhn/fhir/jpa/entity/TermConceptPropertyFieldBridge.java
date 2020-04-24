package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Allows hibernate search to index individual concepts' properties
 */
public class TermConceptPropertyFieldBridge implements FieldBridge, StringBridge {

	public static final String CONCEPT_FIELD_PROPERTY_PREFIX = "PROP";

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
		@SuppressWarnings("unchecked")
		Collection<TermConceptProperty> properties = (Collection<TermConceptProperty>) theValue;

		if (properties != null) {
			for (TermConceptProperty next : properties) {
				theDocument.add(new StringField(CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey(), next.getValue(), Field.Store.YES));

				if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
					if (isNotBlank(next.getDisplay())) {
						theDocument.add(new StringField(CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey(), next.getDisplay(), Field.Store.YES));
					}
				}
			}
		}
	}
}
