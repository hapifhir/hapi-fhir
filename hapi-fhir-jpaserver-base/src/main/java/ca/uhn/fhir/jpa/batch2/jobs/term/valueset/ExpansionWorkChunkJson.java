package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.ValueSet;

public class ExpansionWorkChunkJson implements IModelJson {
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@JsonProperty("stagingUrl")
	private String myStagingUrl;
	@JsonProperty("include")
	private boolean myInclude;
	@JsonProperty("compose")
	private String myCompose;

	public String getStagingUrl() {
		return myStagingUrl;
	}

	public void setStagingUrl(String theStagingUrl) {
		myStagingUrl = theStagingUrl;
	}

	public boolean isInclude() {
		return myInclude;
	}

	public void setInclude(boolean theInclude) {
		myInclude = theInclude;
	}

	public ValueSet.ValueSetComposeComponent getCompose() {
		ValueSet.ValueSetComposeComponent retVal = null;
		if (myCompose != null) {
			retVal = new ValueSet.ValueSetComposeComponent();
			myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).parseInto(myCompose, retVal);
		}
		return retVal;
	}

	public void setCompose(ValueSet.ValueSetComposeComponent theValueSetCompose) {
		String serialized = null;
		if (theValueSetCompose != null) {
			serialized = myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).encodeToString(theValueSetCompose);
		}
		myCompose = serialized;
	}

}
