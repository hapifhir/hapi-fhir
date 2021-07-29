package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Allows hibernate search to index
 *
 * CodeableConcept.text
 * Coding.display
 * Identifier.type.text
 *
 */
public class SearchParamTextPropertyBinder implements PropertyBinder {


	public static final String SEARCH_PARAM_TEXT_PREFIX = "text-";
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamTextPropertyBinder.class);

	@Override
	public void bind(PropertyBindingContext thePropertyBindingContext) {
		// FIXME Is it safe to use object identity of the Map to track dirty?
		thePropertyBindingContext.dependencies().use("mySearchParamTexts");
		IndexSchemaElement indexSchemaElement = thePropertyBindingContext.indexSchemaElement();

		//In order to support dynamic fields, we have to use field templates. We _must_ define the template at bootstrap time and cannot
		//create them adhoc. https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#mapper-orm-bridge-index-field-dsl-dynamic
		//I _think_ im doing the right thing here by indicating that everything matching this template uses this analyzer.
		indexSchemaElement.fieldTemplate("propTemplate", f -> f.asString().analyzer("standard"))
			.matchingPathGlob(SEARCH_PARAM_TEXT_PREFIX +  "*");


		thePropertyBindingContext.bridge(SearchParamTextWrapper.class, new SearchParamTextPropertyBridge());
	}

	private class SearchParamTextPropertyBridge implements PropertyBridge<SearchParamTextWrapper> {

		@Override
		public void write(DocumentElement theDocument, SearchParamTextWrapper searchParamTexts, PropertyBridgeWriteContext thePropertyBridgeWriteContext) {

			if (searchParamTexts != null) {
				searchParamTexts.entrySet()
					.forEach(entry -> {
						theDocument.addValue(SEARCH_PARAM_TEXT_PREFIX + entry.getKey(), entry.getValue());
						ourLog.trace("Adding Search Param Text: {}{} -- {}", SEARCH_PARAM_TEXT_PREFIX, entry.getKey(), entry.getValue());
					});
				}
			}
		}
}
