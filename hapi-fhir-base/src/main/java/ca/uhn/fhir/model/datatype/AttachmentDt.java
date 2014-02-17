package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.Description;
import ca.uhn.fhir.model.api.ResourceElement;

@DatatypeDefinition(name="Attachment")
public class AttachmentDt extends BaseCompositeDatatype {

	@ResourceElement(name="contentType", order=0, min=1)
	private CodeDt myContentType;
	
	@ResourceElement(name="language", order=1)
	private CodeDt myLanguage;
	
	@ResourceElement(name="data", order=2)
	private Base64BinaryDt myData;

	@ResourceElement(name="url", order=3)
	private UriDt myUrl;

	@ResourceElement(name="size", order=4)
	private IntegerDt mySize;
	
	@ResourceElement(name="hash", order=5)
	@Description("Hash of the data (sha-1, base64ed )")
	private Base64BinaryDt myHash;

	@ResourceElement(name="title", order=5)
	@Description("Label to display in place of the data")
	private StringDt myLabel;

}
