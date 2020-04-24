package ca.uhn.fhir.jpa.model.util;

/*
 * #%L
 * HAPI FHIR Model
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
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.math.BigDecimal;

public class BigDecimalNumericFieldBridge implements TwoWayFieldBridge {
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (value == null) {
			if (luceneOptions.indexNullAs() != null) {
				luceneOptions.addFieldToDocument(name, luceneOptions.indexNullAs(), document);
			}
		} else {
			BigDecimal bdValue = (BigDecimal)value;
			applyToLuceneOptions(luceneOptions, name, bdValue.doubleValue(), document);
		}
	}

	@Override
	public final String objectToString(final Object object) {
		return object == null ? null : object.toString();
	}

	@Override
	public Object get(final String name, final Document document) {
		final IndexableField field = document.getField(name);
		if (field != null) {
			Double doubleVal = (Double)field.numericValue();
			return new BigDecimal(doubleVal);
		} else {
			return null;
		}
	}

	protected void applyToLuceneOptions(LuceneOptions luceneOptions, String name, Number value, Document document) {
		luceneOptions.addNumericFieldToDocument(name, value, document);
	}
}
