package ca.uhn.fhir.mdm.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MdmLinkChangeEvent {

	private String myGoldenResourceId;
	private Set<String> myDuplicateGoldenResourceIds = new HashSet<>();

	public String getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public void setGoldenResourceId(IBaseResource theGoldenResourceId) {
		setGoldenResourceId(getIdAsString(theGoldenResourceId));
	}

	public void setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
	}

	private String getIdAsString(IBaseResource theResource) {
		if (theResource == null) {
			return null;
		}
		IIdType idElement = theResource.getIdElement();
		if (idElement == null) {
			return null;
		}
		return idElement.getValueAsString();
	}

	public Set<String> getDuplicateGoldenResourceIds() {
		return myDuplicateGoldenResourceIds;
	}

	public void setDuplicateGoldenResourceIds(Set<String> theDuplicateGoldenResourceIds) {
		myDuplicateGoldenResourceIds = theDuplicateGoldenResourceIds;
	}

	public MdmLinkChangeEvent addDuplicateGoldenResourceId(IBaseResource theDuplicateGoldenResourceId) {
		String id = getIdAsString(theDuplicateGoldenResourceId);
		if (id != null) {
			getDuplicateGoldenResourceIds().add(id);
		}
		return this;
	}

}
