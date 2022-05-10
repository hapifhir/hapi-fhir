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

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaObjectField;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.engine.backend.types.dsl.IndexFieldTypeFactory;
import org.hibernate.search.engine.backend.types.dsl.StandardIndexFieldTypeOptionsStep;
import org.hibernate.search.engine.backend.types.dsl.StringIndexFieldTypeOptionsStep;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_TEXT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE_NORM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;

/**
 * Allows hibernate search to index
 * <p>
 * CodeableConcept.text
 * Coding.display
 * Identifier.type.text
 */
public class SearchParamTextPropertyBinder implements PropertyBinder, PropertyBridge<ExtendedLuceneIndexData> {

	public static final String SEARCH_PARAM_TEXT_PREFIX = "text-";
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamTextPropertyBinder.class);

	@Override
	public void bind(PropertyBindingContext thePropertyBindingContext) {
		// TODO Is it safe to use object identity of the Map to track dirty?
		// N.B. GGG I would hazard that it is not, we could potentially use Version of the resource.
		thePropertyBindingContext.dependencies()
			.use("mySearchParamStrings")
			.use("mySearchParamQuantities");

		defineIndexingTemplate(thePropertyBindingContext);

		thePropertyBindingContext.bridge(ExtendedLuceneIndexData.class, this);
	}

	private void defineIndexingTemplate(PropertyBindingContext thePropertyBindingContext) {
		IndexSchemaElement indexSchemaElement = thePropertyBindingContext.indexSchemaElement();

		//In order to support dynamic fields, we have to use field templates. We _must_ define the template at bootstrap time and cannot
		//create them adhoc. https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#mapper-orm-bridge-index-field-dsl-dynamic
		//I _think_ im doing the right thing here by indicating that everything matching this template uses this analyzer.
		IndexFieldTypeFactory indexFieldTypeFactory = thePropertyBindingContext.typeFactory();
		// TODO mb Once Ken finishes extracting a common base, we can share these constants with HapiElasticsearchAnalysisConfigurer and HapiLuceneAnalysisConfigurer
		StringIndexFieldTypeOptionsStep<?> standardAnalyzer = indexFieldTypeFactory.asString()
			.analyzer("standardAnalyzer")
			.projectable(Projectable.NO);

		StringIndexFieldTypeOptionsStep<?> exactAnalyzer =
			indexFieldTypeFactory.asString()
				.analyzer("exactAnalyzer") // default max-length is 256.  Is that enough for code system uris?
				.projectable(Projectable.NO);

		StringIndexFieldTypeOptionsStep<?> normStringAnalyzer = indexFieldTypeFactory.asString()
			.analyzer("normStringAnalyzer")
			.projectable(Projectable.NO);

		StringIndexFieldTypeOptionsStep<?> keywordFieldType = indexFieldTypeFactory.asString()
		// TODO JB: may have to add normalizer to support case insensitive searches depending on token flags
			.projectable(Projectable.NO)
			.aggregable(Aggregable.YES);

		StandardIndexFieldTypeOptionsStep<?, Instant> dateTimeFieldType = indexFieldTypeFactory.asInstant()
			.projectable(Projectable.NO)
			.sortable(Sortable.YES);

		StandardIndexFieldTypeOptionsStep<?, Integer> dateTimeOrdinalFieldType = indexFieldTypeFactory.asInteger()
			.projectable(Projectable.NO)
			.sortable(Sortable.YES);

		StandardIndexFieldTypeOptionsStep<?, Double> bigDecimalFieldType = indexFieldTypeFactory.asDouble()
			.projectable(Projectable.NO)
			.sortable(Sortable.YES);

		StringIndexFieldTypeOptionsStep<?> forcedIdType = indexFieldTypeFactory.asString()
			.projectable(Projectable.YES)
			.aggregable(Aggregable.NO);

		// type to store payload fields that do not participate in search, only results
		StringIndexFieldTypeOptionsStep<?> stringStorageType = indexFieldTypeFactory.asString()
			.searchable(Searchable.NO)
			.projectable(Projectable.YES)
			.aggregable(Aggregable.NO);

		// the old style for _text and _contains
		indexSchemaElement
			.fieldTemplate("SearchParamText", standardAnalyzer)
			.matchingPathGlob(SEARCH_PARAM_TEXT_PREFIX + "*");


		indexSchemaElement.field("myForcedId", forcedIdType).toReference();

		indexSchemaElement.field("myRawResource", stringStorageType).toReference();

		// The following section is a bit ugly.  We need to enforce order and dependency or the object matches will be too big.
		{
			IndexSchemaObjectField spfield = indexSchemaElement.objectField(HibernateSearchIndexWriter.SEARCH_PARAM_ROOT, ObjectStructure.FLATTENED);
			spfield.toReference();
			IndexSchemaObjectField nestedSpField = indexSchemaElement.objectField(HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT, ObjectStructure.FLATTENED);
			nestedSpField.toReference();

			// TODO MB: the lucene/elastic independent api is hurting a bit here.
			// For lucene, we need a separate field for each analyzer.  So we'll add string (for :exact), and text (for :text).
			// They aren't marked stored, so there's no space cost beyond the index for each.
			// But for elastic, I'd rather have a single field defined, with multi-field sub-fields.  The index cost is the same,
			// but elastic will actually store all fields in the source document.

			// So triplicate the storage for now. :-(
			String stringPathGlob = "*.string";
			spfield.objectFieldTemplate("stringIndex", ObjectStructure.FLATTENED).matchingPathGlob(stringPathGlob);
			spfield.fieldTemplate("string-norm", normStringAnalyzer).matchingPathGlob(stringPathGlob + "." + IDX_STRING_NORMALIZED).multiValued();
			spfield.fieldTemplate("string-exact", exactAnalyzer).matchingPathGlob(stringPathGlob + "." + IDX_STRING_EXACT).multiValued();
			spfield.fieldTemplate("string-text", standardAnalyzer).matchingPathGlob(stringPathGlob + "." + IDX_STRING_TEXT).multiValued();

			nestedSpField.objectFieldTemplate("nestedStringIndex", ObjectStructure.FLATTENED).matchingPathGlob(stringPathGlob);
			nestedSpField.fieldTemplate("string-text", standardAnalyzer).matchingPathGlob(stringPathGlob + "." + IDX_STRING_TEXT).multiValued();

			// token
			// Ideally, we'd store a single code-system string and use a custom tokenizer to
			// generate "system|" "|code" and "system|code" tokens to support all three.
			// But the standard tokenizers aren't that flexible.  As second best, it would be nice to use elastic multi-fields
			// to apply three different tokenizers to a single value.
			// Instead, just be simple and expand into three full fields for now
			String tokenPathGlob = "*.token";
			spfield.objectFieldTemplate("tokenIndex", ObjectStructure.FLATTENED).matchingPathGlob(tokenPathGlob);
			spfield.fieldTemplate("token-code", keywordFieldType).matchingPathGlob(tokenPathGlob + ".code").multiValued();
			spfield.fieldTemplate("token-code-system", keywordFieldType).matchingPathGlob(tokenPathGlob + ".code-system").multiValued();
			spfield.fieldTemplate("token-system", keywordFieldType).matchingPathGlob(tokenPathGlob + ".system").multiValued();

			nestedSpField.objectFieldTemplate("nestedTokenIndex", ObjectStructure.FLATTENED).matchingPathGlob(tokenPathGlob);
			nestedSpField.fieldTemplate("token-code", keywordFieldType).matchingPathGlob(tokenPathGlob + ".code").multiValued();
			nestedSpField.fieldTemplate("token-code-system", keywordFieldType).matchingPathGlob(tokenPathGlob + ".code-system").multiValued();
			nestedSpField.fieldTemplate("token-system", keywordFieldType).matchingPathGlob(tokenPathGlob + ".system").multiValued();

			// reference
			spfield.fieldTemplate("reference-value", keywordFieldType).matchingPathGlob("*.reference.value").multiValued();

			//quantity
			String quantityPathGlob = "*.quantity";
			nestedSpField.objectFieldTemplate("quantityTemplate", ObjectStructure.FLATTENED).matchingPathGlob(quantityPathGlob);
			nestedSpField.fieldTemplate(QTY_SYSTEM, keywordFieldType).matchingPathGlob(quantityPathGlob + "." + QTY_SYSTEM);
			nestedSpField.fieldTemplate(QTY_CODE, keywordFieldType).matchingPathGlob(quantityPathGlob + "." + QTY_CODE);
			nestedSpField.fieldTemplate(QTY_VALUE, bigDecimalFieldType).matchingPathGlob(quantityPathGlob + "." + QTY_VALUE);
			nestedSpField.fieldTemplate(QTY_CODE_NORM, keywordFieldType).matchingPathGlob(quantityPathGlob + "." + QTY_CODE_NORM);
			nestedSpField.fieldTemplate(QTY_VALUE_NORM, bigDecimalFieldType).matchingPathGlob(quantityPathGlob + "." + QTY_VALUE_NORM);

			// date
			String dateTimePathGlob = "*.dt";
			spfield.objectFieldTemplate("datetimeIndex", ObjectStructure.FLATTENED).matchingPathGlob(dateTimePathGlob);
			spfield.fieldTemplate("datetime-lower-ordinal", dateTimeOrdinalFieldType).matchingPathGlob(dateTimePathGlob + ".lower-ord");
			spfield.fieldTemplate("datetime-lower-value", dateTimeFieldType).matchingPathGlob(dateTimePathGlob + ".lower");
			spfield.fieldTemplate("datetime-upper-ordinal", dateTimeOrdinalFieldType).matchingPathGlob(dateTimePathGlob + ".upper-ord");
			spfield.fieldTemplate("datetime-upper-value", dateTimeFieldType).matchingPathGlob(dateTimePathGlob + ".upper");

			// last, since the globs are matched in declaration order, and * matches even nested nodes.
			spfield.objectFieldTemplate("spObject", ObjectStructure.FLATTENED).matchingPathGlob("*");

			// we use nested search params for the autocomplete search.
			nestedSpField.objectFieldTemplate("nestedSpObject", ObjectStructure.NESTED).matchingPathGlob("*").multiValued();
		}
	}

	@Override
	public void write(DocumentElement theDocument, ExtendedLuceneIndexData theIndexData, PropertyBridgeWriteContext thePropertyBridgeWriteContext) {
		if (theIndexData != null) {
			ourLog.trace("Writing index data for {}", theIndexData);
			theIndexData.writeIndexElements(theDocument);
		}
	}

}
