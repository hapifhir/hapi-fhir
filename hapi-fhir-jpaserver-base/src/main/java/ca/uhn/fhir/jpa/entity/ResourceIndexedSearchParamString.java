package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "SPIDX_STRING", indexes= {@Index(name="IDX_SP_STRING", columnList="SP_VALUE_NORMALIZED")})
public class ResourceIndexedSearchParamString extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE_NORMALIZED", length = 100, nullable = true)
	public String myValueNormalized;

	@Column(name="SP_VALUE_EXACT",length=100,nullable=true)
	public String myValueExact;
	
	public ResourceIndexedSearchParamString() {
	}

	public ResourceIndexedSearchParamString(String theName, String theValueNormalized, String theValueExact) {
		setName(theName);
		setValueNormalized(theValueNormalized);
		setValueExact(theValueExact);
	}

	public String getValueNormalized() {
		return myValueNormalized;
	}

	public void setValueNormalized(String theValueNormalized) {
		myValueNormalized = theValueNormalized;
	}

	public String getValueExact() {
		return myValueExact;
	}

	public void setValueExact(String theValueExact) {
		myValueExact = theValueExact;
	}



}
