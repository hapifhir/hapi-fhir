package ca.uhn.fhir.model;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

/**
 * A test resource used in place of 'real' fhir resources,
 * so as to avoid issues with mocking (eg, casting exceptions).
 */
public class TestFhirResource implements IAnyResource {
	@Override
	public String getId() {
		return null;
	}

	@Override
	public IBaseMetaType getMeta() {
		return null;
	}

	@Override
	public IIdType getIdElement() {
		return null;
	}

	@Override
	public IPrimitiveType<String> getLanguageElement() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean hasFormatComment() {
		return false;
	}

	@Override
	public List<String> getFormatCommentsPre() {
		return null;
	}

	@Override
	public List<String> getFormatCommentsPost() {
		return null;
	}

	@Override
	public Object getUserData(String name) {
		return null;
	}

	@Override
	public IAnyResource setId(String theId) {
		return this;
	}

	@Override
	public IBaseResource setId(IIdType theId) {
		return this;
	}

	@Override
	public FhirVersionEnum getStructureFhirVersionEnum() {
		return null;
	}

	@Override
	public void setUserData(String name, Object value) {

	}
}
