package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

public class ImportTerminologyMetadataAttachmentJson implements IModelJson {

	public static final String ATTACHMENT_FILENAME = "metadata.json";

	@JsonProperty("codeSystemXml")
	private String myCodeSystemXml;
	@JsonProperty("codeSystemStagingVersionId")
	private String myCodeSystemStagingVersionId;
	@JsonIgnore
	private CodeSystem myCodeSystemParsed;


	public CodeSystem getCodeSystem() {
		if (myCodeSystemParsed == null) {
			myCodeSystemParsed =
				FhirContext.forR4Cached().newXmlParser().parseResource(CodeSystem.class, getCodeSystemXml());
		}
		return myCodeSystemParsed;
	}

	public void setCodeSystem(@Nonnull CodeSystem theCodeSystem) {
		setCodeSystemXml(FhirContext.forR4Cached().newXmlParser().encodeResourceToString(theCodeSystem));
		myCodeSystemParsed = theCodeSystem;
	}

	public String getCodeSystemXml() {
		return myCodeSystemXml;
	}

	public void setCodeSystemXml(String theCodeSystemXml) {
		myCodeSystemXml = theCodeSystemXml;
		myCodeSystemParsed = null;
	}

	public String getCodeSystemStagingVersionId() {
		return myCodeSystemStagingVersionId;
	}

	public void setCodeSystemStagingVersionId(String theCodeSystemStagingVersionId) {
		myCodeSystemStagingVersionId = theCodeSystemStagingVersionId;
	}

}
