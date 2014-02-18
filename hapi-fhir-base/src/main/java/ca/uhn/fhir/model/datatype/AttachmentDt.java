package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.Description;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="Attachment")
public class AttachmentDt extends BaseCompositeDatatype {

	@Child(name="contentType", order=0, min=1)
	private CodeDt myContentType;
	
	@Child(name="language", order=1)
	private CodeDt myLanguage;
	
	@Child(name="data", order=2)
	private Base64BinaryDt myData;

	@Child(name="url", order=3)
	private UriDt myUrl;

	@Child(name="size", order=4)
	private IntegerDt mySize;
	
	@Child(name="hash", order=5)
	@Description("Hash of the data (sha-1, base64ed )")
	private Base64BinaryDt myHash;

	@Child(name="title", order=5)
	@Description("Label to display in place of the data")
	private StringDt myLabel;

}
