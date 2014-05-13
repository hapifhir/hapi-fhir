package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	private Long myId;
	
	@Column(name="SP_NAME", length=100)
	private String myName;

	public String getName() {
		return myName;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public abstract BaseResourceTable<?> getResource();

	public abstract void setResource(BaseResourceTable<?> theResource);

	public abstract T getValue();

	public abstract void setValue(T theValue);

	
}
