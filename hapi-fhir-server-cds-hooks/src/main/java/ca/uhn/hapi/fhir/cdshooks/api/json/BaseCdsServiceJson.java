package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see <a href=" https://cds-hooks.hl7.org/1.0/#extensions">For reading more about Extension support in CDS hooks</a>
 */

public class BaseCdsServiceJson implements IModelJson {

	@JsonProperty(value = "extension", required = true)
	String myExtension;

	public String getExtension() {
		return myExtension;
	}

	public BaseCdsServiceJson setExtension(String theExtension) {
		this.myExtension = theExtension;
		return this;
	}

}
