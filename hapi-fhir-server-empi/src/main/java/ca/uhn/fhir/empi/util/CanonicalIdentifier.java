package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Version independent identifier
 */
public class CanonicalIdentifier extends BaseIdentifierDt {
	UriDt mySystem;
	StringDt myValue;

	@Override
	public UriDt getSystemElement() {
		return mySystem;
	}

	@Override
	public StringDt getValueElement() {
		return myValue;
	}

	@Override
	public CanonicalIdentifier setSystem(String theUri) {
		mySystem = new UriDt((theUri));
		return this;
	}

	@Override
	public CanonicalIdentifier setValue(String theString) {
		myValue = new StringDt(theString);
		return this;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		if (mySystem != null && !mySystem.isEmpty()) {
			return false;
		}
		if (myValue != null && !myValue.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		CanonicalIdentifier that = (CanonicalIdentifier) theO;

		return new EqualsBuilder()
			.append(mySystem, that.mySystem)
			.append(myValue, that.myValue)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(mySystem)
			.append(myValue)
			.toHashCode();
	}
}
