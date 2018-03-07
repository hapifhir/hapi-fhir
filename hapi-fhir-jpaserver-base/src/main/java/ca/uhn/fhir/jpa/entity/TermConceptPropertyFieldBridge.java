package ca.uhn.fhir.jpa.entity;

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
