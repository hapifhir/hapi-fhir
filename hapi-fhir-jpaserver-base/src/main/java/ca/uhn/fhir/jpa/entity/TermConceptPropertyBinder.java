/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.entity;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.engine.backend.types.dsl.IndexFieldTypeFactory;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Allows hibernate search to index individual concepts' properties
 */
public class TermConceptPropertyBinder implements PropertyBinder {

	public static final String CONCEPT_PROPERTY_PREFIX_NAME = "P:";

	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptPropertyBinder.class);

	@Override
	public void bind(PropertyBindingContext thePropertyBindingContext) {
		thePropertyBindingContext.dependencies().use("myKey").use("myValue");
		IndexSchemaElement indexSchemaElement = thePropertyBindingContext.indexSchemaElement();

		// In order to support dynamic fields, we have to use field templates. We _must_ define the template at
		// bootstrap time and cannot
		// create them adhoc.
		// https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#mapper-orm-bridge-index-field-dsl-dynamic
		indexSchemaElement
				.fieldTemplate("propTemplate", IndexFieldTypeFactory::asString)
				.matchingPathGlob(CONCEPT_PROPERTY_PREFIX_NAME + "*")
				.multiValued();

		thePropertyBindingContext.bridge(Collection.class, new TermConceptPropertyBridge());
	}

	@SuppressWarnings("rawtypes")
	private static class TermConceptPropertyBridge implements PropertyBridge<Collection> {

		@Override
		public void write(
				DocumentElement theDocument,
				Collection theObject,
				PropertyBridgeWriteContext thePropertyBridgeWriteContext) {

			@SuppressWarnings("unchecked")
			Collection<TermConceptProperty> properties = (Collection<TermConceptProperty>) theObject;

			if (properties != null) {
				for (TermConceptProperty next : properties) {
					theDocument.addValue(CONCEPT_PROPERTY_PREFIX_NAME + next.getKey(), next.getValue());
					ourLog.trace(
							"Adding Prop: {}{} -- {}", CONCEPT_PROPERTY_PREFIX_NAME, next.getKey(), next.getValue());
					if (next.getType() == TermConceptPropertyTypeEnum.CODING && isNotBlank(next.getDisplay())) {
						theDocument.addValue(CONCEPT_PROPERTY_PREFIX_NAME + next.getKey(), next.getDisplay());
						ourLog.trace(
								"Adding multivalue Prop: {}{} -- {}",
								CONCEPT_PROPERTY_PREFIX_NAME,
								next.getKey(),
								next.getDisplay());
					}
				}
			}
		}
	}
}
