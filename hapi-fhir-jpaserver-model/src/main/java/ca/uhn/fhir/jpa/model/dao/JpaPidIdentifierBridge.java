package ca.uhn.fhir.jpa.model.dao;

import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

// FIXME: should this be more nuanced
public class JpaPidIdentifierBridge implements IdentifierBridge<JpaPid> {
	@Override
	public String toDocumentIdentifier(JpaPid propertyValue, IdentifierBridgeToDocumentIdentifierContext context) {
		return propertyValue.getId().toString();
	}

	@Override
	public JpaPid fromDocumentIdentifier(
			String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
		long pid = Long.parseLong(documentIdentifier);
		return JpaPid.fromId(pid);
	}
}
