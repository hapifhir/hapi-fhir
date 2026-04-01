package ca.uhn.fhir.batch2.api;

import java.io.InputStream;

public class StoreAttachmemtRequest {

	private InputStream myInputStream;
	private AttachmentContentTypeEnum myContentType;
	private String myFilename;
	private String myInstanceId;

	public String getInstanceId() {
		return myInstanceId;
	}

	public String getFilename() {
		return myFilename;
	}
}
