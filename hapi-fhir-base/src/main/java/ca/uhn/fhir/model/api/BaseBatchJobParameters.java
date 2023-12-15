package ca.uhn.fhir.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseBatchJobParameters implements IModelJson {
	/**
	 * A serializable map of key-value pairs that can be
	 * added to any extending job.
	 */
	@JsonProperty("userData")
	private Map<String, Object> myUserData;

	public Map<String, Object> getUserData() {
		if (myUserData == null) {
			myUserData = new HashMap<>();
		}
		return myUserData;
	}

	public void setUserData(String theKey, Object theValue) {
		Validate.isTrue(isNotBlank(theKey), "Invalid key; key must be non-empty, non-null.");
		if (theValue == null) {
			getUserData().remove(theKey);
		} else {
			Validate.isTrue(
					validateValue(theValue),
					String.format(
							"Invalid data type provided %s", theValue.getClass().getName()));
			getUserData().put(theKey, theValue);
		}
	}

	private boolean validateValue(Object theValue) {
		if (theValue instanceof Boolean) {
			return true;
		}
		if (theValue instanceof Number) {
			return true;
		}
		if (theValue instanceof String) {
			return true;
		}
		return false;
	}
}
