package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Wraps an Attachment datatype or Binary resource, since they both
 * hold binary content but don't look entirely similar
 */
interface IBinaryTarget {

	void setSize(Integer theSize);

	String getContentType();

	void setContentType(String theContentType);

	byte[] getData();

	void setData(byte[] theBytes);

	IBaseHasExtensions getTarget();

	@SuppressWarnings("unchecked")
	default Optional<String> getAttachmentId() {
		return getTarget()
			.getExtension()
			.stream()
			.filter(t -> JpaConstants.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
			.filter(t -> t.getValue() instanceof IPrimitiveType)
			.map(t -> (IPrimitiveType<String>) t.getValue())
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.findFirst();
	}
}
