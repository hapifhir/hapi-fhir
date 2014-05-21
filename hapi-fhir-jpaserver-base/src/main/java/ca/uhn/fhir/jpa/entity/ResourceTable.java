package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;

@Entity
@Table(name = "HFJ_RESOURCE", uniqueConstraints = {})
@Inheritance(strategy = InheritanceType.JOINED)
public class ResourceTable extends BaseHasResource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "SP_HAS_LINKS")
	private boolean myHasLinks;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	private Long myId;

	@OneToMany(mappedBy = "myTargetResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceLink> myIncomingResourceLinks;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamDate> myParamsDate;

	@Column(name = "SP_DATE_PRESENT")
	private boolean myParamsDatePopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamNumber> myParamsNumber;

	@Column(name = "SP_NUMBER_PRESENT")
	private boolean myParamsNumberPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamString> myParamsString;

	@Column(name = "SP_STRING_PRESENT")
	private boolean myParamsStringPopulated;

	@OneToMany(mappedBy = "myResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceIndexedSearchParamToken> myParamsToken;

	@Column(name = "SP_TOKEN_PRESENT")
	private boolean myParamsTokenPopulated;

	@OneToMany(mappedBy = "mySourceResource", cascade = {}, fetch = FetchType.LAZY, orphanRemoval = false)
	private Collection<ResourceLink> myResourceLinks;

	@Column(name = "RES_TYPE", length = 30)
	private String myResourceType;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Collection<ResourceTag> myTags;

	@Version()
	@Column(name = "RES_VER")
	private Long myVersion;

	public void addTag(String theTerm, String theLabel, String theScheme) {
		for (ResourceTag next : getTags()) {
			if (next.getTerm().equals(theTerm)) {
				return;
			}
		}
		getTags().add(new ResourceTag(this, theTerm, theLabel, theScheme));
	}

	public IdDt getIdDt() {
		return new IdDt(myId);
	}

	public Long getId() {
		return myId;
	}

	public Collection<ResourceIndexedSearchParamDate> getParamsDate() {
		if (myParamsDate == null) {
			myParamsDate = new ArrayList<>();
		}
		return myParamsDate;
	}

	public Collection<ResourceIndexedSearchParamNumber> getParamsNumber() {
		if (myParamsNumber == null) {
			myParamsNumber = new ArrayList<>();
		}
		return myParamsNumber;
	}

	public Collection<ResourceIndexedSearchParamString> getParamsString() {
		if (myParamsString == null) {
			myParamsString = new ArrayList<>();
		}
		return myParamsString;
	}

	public Collection<ResourceIndexedSearchParamToken> getParamsToken() {
		if (myParamsToken == null) {
			myParamsToken = new ArrayList<>();
		}
		return myParamsToken;
	}

	public Collection<ResourceLink> getResourceLinks() {
		if (myResourceLinks == null) {
			myResourceLinks = new ArrayList<>();
		}
		return myResourceLinks;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Collection<ResourceTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<>();
		}
		return myTags;
	}

	public IdDt getVersion() {
		return new IdDt(myVersion);
	}

	public boolean isHasLinks() {
		return myHasLinks;
	}

	public boolean isParamsDatePopulated() {
		return myParamsDatePopulated;
	}

	public boolean isParamsNumberPopulated() {
		return myParamsNumberPopulated;
	}

	public boolean isParamsStringPopulated() {
		return myParamsStringPopulated;
	}

	public boolean isParamsTokenPopulated() {
		return myParamsTokenPopulated;
	}

	public void setHasLinks(boolean theHasLinks) {
		myHasLinks = theHasLinks;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setParamsDate(Collection<ResourceIndexedSearchParamDate> theParamsDate) {
		myParamsDate = theParamsDate;
	}

	public void setParamsDatePopulated(boolean theParamsDatePopulated) {
		myParamsDatePopulated = theParamsDatePopulated;
	}

	public void setParamsNumberPopulated(boolean theParamsNumberPopulated) {
		myParamsNumberPopulated = theParamsNumberPopulated;
	}

	public void setParamsString(Collection<ResourceIndexedSearchParamString> theParamsString) {
		myParamsString = theParamsString;
	}

	public void setParamsStringPopulated(boolean theParamsStringPopulated) {
		myParamsStringPopulated = theParamsStringPopulated;
	}

	public void setParamsToken(Collection<ResourceIndexedSearchParamToken> theParamsToken) {
		myParamsToken = theParamsToken;
	}

	public void setParamsTokenPopulated(boolean theParamsTokenPopulated) {
		myParamsTokenPopulated = theParamsTokenPopulated;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public void setVersion(IdDt theVersion) {
		myVersion = theVersion.asLong();
	}

	public ResourceHistoryTable toHistory(FhirContext theCtx) {
		ResourceHistoryTable retVal = new ResourceHistoryTable();

		ResourceHistoryTablePk pk = new ResourceHistoryTablePk();
		pk.setId(myId);
		pk.setResourceType(myResourceType);
		pk.setVersion(myVersion);
		retVal.setPk(pk);

		retVal.setPublished(getPublished());
		retVal.setUpdated(getUpdated());
		retVal.setEncoding(getEncoding());
		retVal.setResource(getResource());

		for (ResourceTag next : getTags()) {
			retVal.addTag(next.getTerm(), next.getLabel(), next.getScheme());
		}

		return retVal;
	}
}
