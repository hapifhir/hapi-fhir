package ca.uhn.fhir.rest.server.messaging;



import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public abstract class BaseResourceMessage implements IResourceMessage, IModelJson {

	@JsonProperty("attributes")
	private Map<String, String> myAttributes;

	/**
	 * Returns an attribute stored in this message.
	 * <p>
	 * Attributes are just a spot for user data of any kind to be
	 * added to the message for pasing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 * <p>
	 * Note that messages are designed to be passed into queueing systems
	 * and serialized as JSON. As a result, only strings are currently allowed
	 * as values.
	 * </p>
	 */
	public Optional<String> getAttribute(String theKey) {
		Validate.notBlank(theKey);
		if (myAttributes == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(myAttributes.get(theKey));
	}

	/**
	 * Sets an attribute stored in this message.
	 * <p>
	 * Attributes are just a spot for user data of any kind to be
	 * added to the message for passing along the subscription processing
	 * pipeline (typically by interceptors). Values will be carried from the beginning to the end.
	 * </p>
	 * <p>
	 * Note that messages are designed to be passed into queueing systems
	 * and serialized as JSON. As a result, only strings are currently allowed
	 * as values.
	 * </p>
	 *
	 * @param theKey   The key (must not be null or blank)
	 * @param theValue The value (must not be null)
	 */
	public void setAttribute(String theKey, String theValue) {
		Validate.notBlank(theKey);
		Validate.notNull(theValue);
		if (myAttributes == null) {
			myAttributes = new HashMap<>();
		}
		myAttributes.put(theKey, theValue);
	}

	/**
	 * Copies any attributes from the given message into this messsage.
	 *
	 * @see #setAttribute(String, String)
	 * @see #getAttribute(String)
	 */
	public void copyAdditionalPropertiesFrom(BaseResourceMessage theMsg) {
		if (theMsg.myAttributes != null) {
			if (myAttributes == null) {
				myAttributes = new HashMap<>();
			}
			myAttributes.putAll(theMsg.myAttributes);
		}
	}
}
