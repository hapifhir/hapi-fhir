package ca.uhn.fhir.jpa.subscription.module.subscriber;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public abstract class BaseResourceMessage implements IResourceMessage {
	@JsonProperty("additionalProperties")
	private Map<String, String> myAdditionalProperties;

	/**
	 * Returns an additional property stored in this message.
	 * <p>
	 * Additional properties are just a spot for user data of any kind to be
	 * added to the message for pasing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 */
	public Optional<String> getAdditionalProperty(String theKey) {
		Validate.notBlank(theKey);
		if (myAdditionalProperties == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(myAdditionalProperties.get(theKey));
	}

	/**
	 * Sets an additional property stored in this message.
	 * <p>
	 * Additional properties are just a spot for user data of any kind to be
	 * added to the message for pasing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 *
	 * @param theKey   The key (must not be null or blank)
	 * @param theValue The value (must not be null)
	 */
	public void setAdditionalProperty(String theKey, String theValue) {
		Validate.notBlank(theKey);
		Validate.notNull(theValue);
		if (myAdditionalProperties == null) {
			myAdditionalProperties = new HashMap<>();
		}
		myAdditionalProperties.put(theKey, theValue);
	}

	/**
	 * Copies any additional properties forward.
	 * <p>
	 * Additional properties are just a spot for user data of any kind to be
	 * added to the message for pasing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 */
	public void copyAdditionalPropertiesFrom(BaseResourceMessage theMsg) {
		if (theMsg.myAdditionalProperties != null) {
			if (myAdditionalProperties == null) {
				myAdditionalProperties = new HashMap<>();
			}
			myAdditionalProperties.putAll(theMsg.myAdditionalProperties);
		}
	}
}
