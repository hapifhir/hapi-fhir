package ca.uhn.fhir.jpa.subscription.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AfterPOJO extends BaseJsonMessage<String> {
	@JsonProperty("PID")
	private int pid;
	@JsonProperty("RES_TEXT")
	private String resText;
	@JsonProperty("RES_VER")
	private int resVer;
	@JsonProperty("RES_ID")
	private int resId;
	@JsonProperty("RES_DELETED_AT")
	private long resDeletedAt;
	@JsonProperty("RES_TYPE")
	private String resourceType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public AfterPOJO() {
	}

	public int getResVer() {
		return resVer;
	}

	public String getResourceType() {
		return resourceType;
	}

	public int getResId() {
		return resId;
	}

	public long getResDeletedAt() {
		return resDeletedAt;
	}

	@Override
	public String getPayload() {
		return resText;
	}

	void setPid(int thePid) {
		pid = thePid;
	}

	public void setResVer(int resVer) {
		this.resVer = resVer;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public void setResDeletedAt(long resDeletedAt) {
		this.resDeletedAt = resDeletedAt;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	int getPid() {
		return pid;
	}

	void setResText(String theResText) {
		resText = theResText;
	}

	String getResText() {
		return resText;
	}
}
