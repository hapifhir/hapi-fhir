package ca.uhn.fhir.jpa.util;

import java.math.BigDecimal;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

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