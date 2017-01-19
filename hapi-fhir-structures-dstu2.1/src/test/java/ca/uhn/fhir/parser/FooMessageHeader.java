package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu2016may.model.Identifier;
import org.hl7.fhir.dstu2016may.model.MessageHeader;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "MessageHeader")
public class FooMessageHeader extends MessageHeader {

	private static final long serialVersionUID = 1L;

	@Block()
	public static class FooMessageSourceComponent extends MessageHeader.MessageSourceComponent {

		private static final long serialVersionUID = 1L;
		@Child(name = "ext-messageheader-application-id", type = Identifier.class, modifier = true)
		@Description(shortDefinition = "Message Header Application ID")
		@Extension(url = "http://foo", definedLocally = false, isModifier = false)
		private Identifier messageHeaderApplicationId;

		/*
		 * Get messageHeaderApplicationId
		 */
		public Identifier getMessageHeaderApplicationId() throws FHIRException {

			if (messageHeaderApplicationId == null) {
				messageHeaderApplicationId = new Identifier();
			}
			return messageHeaderApplicationId;
		}

		/*
		 * Set messageHeaderApplicationId
		 */
		public void setmessageHeaderApplicationId(Identifier messageHeaderApplicationId) {
			this.messageHeaderApplicationId = messageHeaderApplicationId;
		}

	}

}