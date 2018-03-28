package ca.uhn.fhir.tests.integration.karaf.dstu3;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.MessageHeader;
import org.hl7.fhir.exceptions.FHIRException;

@ResourceDef(name = "MessageHeader")
public class FooMessageHeader extends MessageHeader {

	private static final long serialVersionUID = 1L;

	@Block()
	public static class FooMessageSourceComponent extends MessageSourceComponent {

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
