package ca.uhn.fhir.jpa.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.regex.Pattern;

@MappedSuperclass
public abstract class BaseResourceIndexedCombo extends BaseResourceIndex implements IResourceIndexComboSearchParameter {

	@Transient
	private IIdType mySearchParameterId;

	@Override
	public IIdType getSearchParameterId() {
		return mySearchParameterId;
	}

	@Override
	public void setSearchParameterId(@Nonnull IIdType theSearchParameterId) {
		assert theSearchParameterId.hasResourceType();
		assert theSearchParameterId.hasIdPart();
		mySearchParameterId = theSearchParameterId.toUnqualifiedVersionless();
	}
}
