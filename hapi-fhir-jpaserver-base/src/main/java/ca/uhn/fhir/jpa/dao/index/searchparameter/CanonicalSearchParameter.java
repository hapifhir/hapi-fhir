package ca.uhn.fhir.jpa.dao.index.searchparameter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class CanonicalSearchParameter {
	private static final Logger ourLog = getLogger(CanonicalSearchParameter.class);

	private String myId;
	private String myName;
	private String myUrl;
	private String myStatus;
	private String myDescription;
	private String myCode;
	private List<String> myBase = new ArrayList<>();
	private String myType;
	private String myExpression;

	public CanonicalSearchParameter(String theId, String theUrl, String theName, String theStatus, String theDescription, String theCode, List<String> theBase, String theType, String theExpression) {
		myId = theId;
		myName = theName;
		myUrl = theUrl;
		myStatus = theStatus;
		myDescription = theDescription;
		myCode = theCode;
		myBase = theBase;
		myType = theType;
		myExpression = theExpression;
	}

	public CanonicalSearchParameter() {

	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getName() {
		return myName;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public String getStatus() {
		return myStatus;
	}

	public void setStatus(String theStatus) {
		myStatus = theStatus;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

	public List<String> getBase() {
		return myBase;
	}

	public void setBase(List<String> theBase) {
		myBase = theBase;
	}

	public String getType() {
		return myType;
	}

	public void setType(String theType) {
		myType = theType;
	}

	public String getExpression() {
		return myExpression;
	}

	public void setExpression(String theExpression) {
		myExpression = theExpression;
	}

	/**
	 * Given a FHIR Context, return an STU-appropriate Search Parameter object.
	 *
	 * @param theFhirContext The FHIRContext of the running application.
	 * @return an {@link IBaseResource} Search Parameter.
	 */
	public IBaseResource decanonicalize(FhirContext theFhirContext) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		switch (version) {
			case DSTU3:
				SearchParameter spDstu3 = new SearchParameter();
				spDstu3.setId(myId);
				spDstu3.setUrl(myUrl);
				spDstu3.setName(myName);
				spDstu3.setStatus(Enumerations.PublicationStatus.fromCode(myStatus));
				spDstu3.setDescription(myDescription);
				spDstu3.setCode(myCode);
				spDstu3.setBase(myBase.stream().map(CodeType::new).collect(Collectors.toList()));
				spDstu3.setType(Enumerations.SearchParamType.fromCode(myType));
				spDstu3.setExpression(myExpression);
				return spDstu3;
			case R4:
				org.hl7.fhir.r4.model.SearchParameter spR4 = new org.hl7.fhir.r4.model.SearchParameter();
				spR4.setId(myId);
				spR4.setUrl(myUrl);
				spR4.setName(myName);
				spR4.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.fromCode(myStatus));
				spR4.setDescription(myDescription);
				spR4.setCode(myCode);
				spR4.setBase(myBase.stream().map(org.hl7.fhir.r4.model.CodeType::new).collect(Collectors.toList()));
				spR4.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.fromCode(myType));
				spR4.setExpression(myExpression);
				return spR4;
			case R5:
				org.hl7.fhir.r5.model.SearchParameter spR5 = new org.hl7.fhir.r5.model.SearchParameter();
				spR5.setId(myId);
				spR5.setUrl(myUrl);
				spR5.setName(myName);
				spR5.setStatus(org.hl7.fhir.r5.model.Enumerations.PublicationStatus.fromCode(myStatus));
				spR5.setDescription(myDescription);
				spR5.setCode(myCode);
				spR5.setBase(myBase.stream().map(org.hl7.fhir.r5.model.CodeType::new).collect(Collectors.toList()));
				spR5.setType(org.hl7.fhir.r5.model.Enumerations.SearchParamType.fromCode(myType));
				spR5.setExpression(myExpression);
				return spR5;
			default:
				ourLog.error("Decanonicalizing is only supported in DSTU3/R4/R5. Not " + version);
				return null;
		}
	}
}
