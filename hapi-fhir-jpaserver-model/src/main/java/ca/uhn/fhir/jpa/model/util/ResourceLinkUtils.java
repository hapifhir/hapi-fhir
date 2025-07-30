package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.jpa.model.entity.ResourceLink;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ResourceLinkUtils {

	/**
	 * UNKNOWN resource type, used for flagging
	 * canonical urls in ResourceLinks.
	 */
	public static final String UNKNOWN = "(unknown)";

	/**
	 * Returns true if the target of the resourcelink is a canonical url;
	 * returns false otherwise.
	 */
	public static boolean isTargetCanonicalUrl(ResourceLink theLink) {
		return isBlank(theLink.getTargetResourceId()) && theLink.getTargetResourceType().equals(UNKNOWN);
	}
}

