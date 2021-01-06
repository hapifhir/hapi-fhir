package ca.uhn.fhir.jpa.entity;

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

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Allows hibernate search to index individual concepts' properties
 */
public class TermConceptPropertyBinder implements PropertyBinder {


	public static final String CONCEPT_FIELD_PROPERTY_PREFIX = "PROP";

	@Override
	public void bind(PropertyBindingContext thePropertyBindingContext) {
		thePropertyBindingContext.dependencies().use("myKey").use("myValue");
		IndexSchemaElement indexSchemaElement = thePropertyBindingContext.indexSchemaElement();

		//In order to support dynamic fields, we have to use field templates. We _must_ define the template at bootstrap time and cannot
		//create them adhoc. https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#mapper-orm-bridge-index-field-dsl-dynamic
		//I _think_ im doing the right thing here by indicating that everything matching this template uses this analyzer.
		indexSchemaElement.fieldTemplate("propTemplate", f -> f.asString().analyzer("termConceptPropertyAnalyzer"))
			.matchingPathGlob(CONCEPT_FIELD_PROPERTY_PREFIX + "*")
			.multiValued();


		thePropertyBindingContext.bridge(new TermConceptPropertyBridge());
	}

	private class TermConceptPropertyBridge implements PropertyBridge {

		@Override
		public void write(DocumentElement theDocument, Object theObject, PropertyBridgeWriteContext thePropertyBridgeWriteContext) {

			Collection<TermConceptProperty> properties = (Collection<TermConceptProperty>) theObject;

			if (properties != null) {
				for (TermConceptProperty next : properties) {
					theDocument.addValue(CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey(), next.getValue());
					System.out.println("Adding Prop: " + CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey() + " -- " + next.getValue());
					if (next.getType() == TermConceptPropertyTypeEnum.CODING && isNotBlank(next.getDisplay())) {
							theDocument.addValue(CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey(), next.getDisplay());
							System.out.println("Adding multivalue Prop: " + CONCEPT_FIELD_PROPERTY_PREFIX + next.getKey() + " -- " + next.getDisplay());
					}
				}
			}
		}
	}
}
