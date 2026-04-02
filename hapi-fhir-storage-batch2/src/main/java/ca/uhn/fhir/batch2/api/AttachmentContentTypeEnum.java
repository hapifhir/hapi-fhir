package ca.uhn.fhir.batch2.api;

public enum AttachmentContentTypeEnum {

	GZIP(false),
	PLAIN_TEXT(true),
	CSV(true);

	private final boolean mySupportsCompression;

	AttachmentContentTypeEnum(boolean theSupportsCompression) {
		mySupportsCompression = theSupportsCompression;
	}

	public boolean isSupportsCompression() {
		return mySupportsCompression;
	}
}
