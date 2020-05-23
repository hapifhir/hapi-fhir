package ca.uhn.fhir.parser.testprofile;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Communication;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Communication", profile = "http://hl7.org/fhir/profiles/CommunicationProfile", id = "communicationProfile")
public class CommunicationProfile extends Communication {

	@Child(name = "payload", min = 1, max = 1, order = Child.REPLACE_PARENT, type = {_Payload.class})
	@Description(shortDefinition = "", formalDefinition = "Text, attachment(s), or resource(s) that was communicated to the recipient.")
	public _Payload myPayload;

	@Block
	public static class _Payload extends Payload {

		@Child(name = "content", min = 0, max = 1, order = Child.REPLACE_PARENT, type = {StringDt.class})
		@Description(shortDefinition = "", formalDefinition = "A communicated content")
		public StringDt myContent;

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(myContent);
		}

	}

}
