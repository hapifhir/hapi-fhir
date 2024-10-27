package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for a bridge between the RI validation tools and HAPI
 */
abstract class BaseValidatorBridge implements IValidatorModule {

	public BaseValidatorBridge() {
		super();
	}

	private void doValidate(IValidationContext<?> theCtx) {
		List<ValidationMessage> messages = validate(theCtx);

		for (ValidationMessage riMessage : messages) {
			SingleValidationMessage hapiMessage = new SingleValidationMessage();
			if (riMessage.getCol() != -1) {
				hapiMessage.setLocationCol(riMessage.getCol());
			}
			if (riMessage.getLine() != -1) {
				hapiMessage.setLocationLine(riMessage.getLine());
			}
			hapiMessage.setLocationString(riMessage.getLocation());
			hapiMessage.setMessage(riMessage.getMessage());
			if (riMessage.getLevel() != null) {
				hapiMessage.setSeverity(
						ResultSeverityEnum.fromCode(riMessage.getLevel().toCode()));
			}
			if (riMessage.getMessageId() != null) {
				// In BaseValidator, the messageId gets populated with the raw message because
				// there is an assumption that it's a message key and not an actual message. But
				// messsages coming from our internal terminology service don't work that
				// way, so we strip them by checking if the ID is actually a sentence
				if (!riMessage.getMessageId().contains(" ")) {
					hapiMessage.setMessageId(riMessage.getMessageId());
				}
			}
			if (riMessage.sliceText != null && riMessage.sliceText.length > 0) {
				hapiMessage.setSliceMessages(Arrays.asList(riMessage.sliceText));
			}
			theCtx.addValidationMessage(hapiMessage);
		}
	}

	protected abstract List<ValidationMessage> validate(IValidationContext<?> theCtx);

	@Override
	public void validateResource(IValidationContext<IBaseResource> theCtx) {
		doValidate(theCtx);
	}
}
