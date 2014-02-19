package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;

@DatatypeDef(name="Attachment")
public class AttachmentDt extends BaseCompositeDatatype {

	@Child(name="contentType", order=0, min=1)
	private CodeDt<?> myContentType;
	
	@Child(name="language", order=1)
	private CodeDt<?> myLanguage;
	
	@Child(name="data", order=2)
	private Base64BinaryDt myData;

	@Child(name="url", order=3)
	private UriDt myUrl;

	@Child(name="size", order=4)
	private IntegerDt mySize;
	
	@Child(name="hash", order=5)
	@Description("Hash of the data (sha-1, base64ed )")
	private Base64BinaryDt myHash;

	@Child(name="title", order=6)
	@Description("Label to display in place of the data")
	private StringDt myTitle;

	public CodeDt<?> getContentType() {
		return myContentType;
	}

	public void setContentType(CodeDt<?> theContentType) {
		myContentType = theContentType;
	}

	public CodeDt<?> getLanguage() {
		return myLanguage;
	}

	public void setLanguage(CodeDt<?> theLanguage) {
		myLanguage = theLanguage;
	}

	public Base64BinaryDt getData() {
		return myData;
	}

	public void setData(Base64BinaryDt theData) {
		myData = theData;
	}

	public UriDt getUrl() {
		return myUrl;
	}

	public void setUrl(UriDt theUrl) {
		myUrl = theUrl;
	}

	public Base64BinaryDt getHash() {
		return myHash;
	}

	public void setHash(Base64BinaryDt theHash) {
		myHash = theHash;
	}

	public StringDt getTitle() {
		return myTitle;
	}

	public void setTitle(StringDt theTitle) {
		myTitle = theTitle;
	}

	public IntegerDt getSize() {
		return mySize;
	}

	public void setSize(IntegerDt theSize) {
		mySize = theSize;
	}

	
	
}
