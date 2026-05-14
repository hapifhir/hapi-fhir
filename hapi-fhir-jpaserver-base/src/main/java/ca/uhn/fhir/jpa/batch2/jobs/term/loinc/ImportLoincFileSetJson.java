package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

public class ImportLoincFileSetJson extends TerminologyFileSetJson {

	@JsonProperty("loincCodeSystemXml")
	private String myLoincCodeSystemXml;

	@JsonProperty("codeSystemStagingVersionId")
	private String myCodeSystemStagingVersionId;

	@JsonIgnore
	private CodeSystem myLoincCodeSystemXmlParsed;

	public CodeSystem getLoincCodeSystem() {
		if (myLoincCodeSystemXmlParsed == null) {
			myLoincCodeSystemXmlParsed =
					FhirContext.forR4Cached().newXmlParser().parseResource(CodeSystem.class, getLoincCodeSystemXml());
		}
		return myLoincCodeSystemXmlParsed;
	}

	public void setLoincCodeSystem(@Nonnull CodeSystem theCodeSystem) {
		setLoincCodeSystemXml(FhirContext.forR4Cached().newXmlParser().encodeResourceToString(theCodeSystem));
		myLoincCodeSystemXmlParsed = theCodeSystem;
	}

	public String getLoincCodeSystemXml() {
		return myLoincCodeSystemXml;
	}

	public void setLoincCodeSystemXml(String theLoincCodeSystemXml) {
		myLoincCodeSystemXml = theLoincCodeSystemXml;
		myLoincCodeSystemXmlParsed = null;
	}

	public String getCodeSystemStagingVersionId() {
		return myCodeSystemStagingVersionId;
	}

	public void setCodeSystemStagingVersionId(String theCodeSystemStagingVersionId) {
		myCodeSystemStagingVersionId = theCodeSystemStagingVersionId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <OT extends TerminologyFileSetJson> OT cloneWithOnlyFutureChunks() {
		ImportLoincFileSetJson retVal = new ImportLoincFileSetJson();
		populateFutureChunksInClone(retVal);

		retVal.setLoincCodeSystemXml(getLoincCodeSystemXml());
		retVal.setCodeSystemStagingVersionId(getCodeSystemStagingVersionId());
		retVal.setResourcesToActivate(getResourcesToActivate());

		return (OT) retVal;
	}

}
