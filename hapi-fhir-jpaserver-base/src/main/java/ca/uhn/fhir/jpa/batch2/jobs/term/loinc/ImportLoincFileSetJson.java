package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.ArrayList;
import java.util.List;

public class ImportLoincFileSetJson extends TerminologyFileSetJson {

	@JsonProperty("loincCodeSystemXml")
	private String myLoincCodeSystemXml;

	@JsonProperty("codeSystemStagingVersionId")
	private String myCodeSystemStagingVersionId;

	@JsonProperty("linguisticVariants")
	private List<LinguisticVariantJson> myLinguisticVariants;

	@JsonIgnore
	private CodeSystem myLoincCodeSystemXmlParsed;

	public List<LinguisticVariantJson> getLinguisticVariants() {
		if (myLinguisticVariants == null) {
			myLinguisticVariants = new ArrayList<>();
		}
		return myLinguisticVariants;
	}

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

	public static class LinguisticVariantJson implements IModelJson {

		@JsonProperty("id")
		private String myId;
		@JsonProperty("lang")
		private String myIsoLanguage;
		@JsonProperty("country")
		private String myIsoCountry;
		@JsonProperty("name")
		private String myLanguageName;

		/**
		 * Constructor
		 */
		public LinguisticVariantJson() {
			super();
		}

		/**
		 * Constructor
		 */
		public LinguisticVariantJson(
			@Nonnull String theId,
			@Nonnull String theIsoLanguage,
			@Nonnull String theIsoCountry,
			@Nonnull String theLanguageName) {
			this.myId = theId;
			this.myIsoLanguage = theIsoLanguage;
			this.myIsoCountry = theIsoCountry;
			this.myLanguageName = theLanguageName;
		}

		public String getLinguisticVariantFileName() {
			return myIsoLanguage + myIsoCountry + myId + "LinguisticVariant.csv";
		}

		public String getLanguageName() {
			return myLanguageName;
		}

		public String getLanguageCode() {
			return myIsoLanguage + "-" + myIsoCountry;
		}
	}
}
