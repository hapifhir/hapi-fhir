package ca.uhn.fhir.jpa.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.regex.Pattern;

@MappedSuperclass
public abstract class BaseResourceIndexedCombo extends BaseResourceIndex implements IResourceIndexComboSearchParameter {

	public static final Pattern SEARCH_PARAM_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]+/[^/]+$");

	@Transient
	private String mySearchParameterId;

	@Nonnull
	@Override
	public String getSearchParameterId() {
		return mySearchParameterId;
	}

	@Override
	public void setSearchParameterId(@Nonnull String theSearchParameterId) {
		assert SEARCH_PARAM_ID_PATTERN.matcher(theSearchParameterId).matches();
		mySearchParameterId = theSearchParameterId;
	}

	@Override
	public void setSearchParameterId(@Nonnull IIdType theSearchParameterId) {
		setSearchParameterId(theSearchParameterId.toUnqualifiedVersionless().getValue());
	}
}
