package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import org.hibernate.annotations.Index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;

@Entity
@Table(name = "HFJ_RESOURCE", uniqueConstraints = {})
@Inheritance(strategy = InheritanceType.JOINED)
@org.hibernate.annotations.Table(appliesTo = "HFJ_RESOURCE", indexes = { @Index(name = "IDX_RES_DATE", columnNames = { "RES_UPDATED" }) })
public class ResourceTable extends BaseHasResource implements Serializable {
	static final int RESTYPE_LEN = 30;

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

	@Column(name = "RES_TYPE", length = RESTYPE_LEN)
	private String myResourceType;

	@OneToMany(mappedBy = "myResource", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Collection<ResourceTag> myTags;

	@Column(name = "RES_VER")
	private long myVersion;

	public ResourceTag addTag(TagDefinition theTag) {
		ResourceTag tag = new ResourceTag(this, theTag);
		tag.setResourceType(getResourceType());
		getTags().add(tag);
		return tag;
	}

	public Long getId() {
		return myId;
	}

	public IdDt getIdDt() {
		return new IdDt(myResourceType + '/' + myId + '/' + Constants.PARAM_HISTORY + '/' + myVersion);
	}

	public Collection<ResourceIndexedSearchParamDate> getParamsDate() {
		if (myParamsDate == null) {
			myParamsDate = new ArrayList<ResourceIndexedSearchParamDate>();
		}
		return myParamsDate;
	}

	public Collection<ResourceIndexedSearchParamNumber> getParamsNumber() {
		if (myParamsNumber == null) {
			myParamsNumber = new ArrayList<ResourceIndexedSearchParamNumber>();
		}
		return myParamsNumber;
	}

	public Collection<ResourceIndexedSearchParamString> getParamsString() {
		if (myParamsString == null) {
			myParamsString = new ArrayList<ResourceIndexedSearchParamString>();
		}
		return myParamsString;
	}

	public Collection<ResourceIndexedSearchParamToken> getParamsToken() {
		if (myParamsToken == null) {
			myParamsToken = new ArrayList<ResourceIndexedSearchParamToken>();
		}
		return myParamsToken;
	}

	public Collection<ResourceLink> getResourceLinks() {
		if (myResourceLinks == null) {
			myResourceLinks = new ArrayList<ResourceLink>();
		}
		return myResourceLinks;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Collection<ResourceTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<ResourceTag>();
		}
		return myTags;
	}

	public long getVersion() {
		return myVersion;
	}

	public boolean hasTag(String theTerm, String theLabel, String theScheme) {
		for (ResourceTag next : getTags()) {
			if (next.getTag().getTerm().equals(theTerm)) {
				return true;
			}
		}
		return false;
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
		if (!isParamsDatePopulated() && theParamsDate.isEmpty()) {
			return;
		}
		getParamsDate().clear();
		getParamsDate().addAll(theParamsDate);
	}

	public void setParamsDatePopulated(boolean theParamsDatePopulated) {
		myParamsDatePopulated = theParamsDatePopulated;
	}

	public void setParamsNumber(List<ResourceIndexedSearchParamNumber> theNumberParams) {
		if (!isParamsNumberPopulated() && theNumberParams.isEmpty()) {
			return;
		}
		getParamsNumber().clear();
		getParamsNumber().addAll(theNumberParams);
	}

	public void setParamsNumberPopulated(boolean theParamsNumberPopulated) {
		myParamsNumberPopulated = theParamsNumberPopulated;
	}

	public void setParamsString(Collection<ResourceIndexedSearchParamString> theParamsString) {
		if (!isParamsStringPopulated() && theParamsString.isEmpty()) {
			return;
		}
		getParamsString().clear();
		getParamsString().addAll(theParamsString);
	}

	public void setParamsStringPopulated(boolean theParamsStringPopulated) {
		myParamsStringPopulated = theParamsStringPopulated;
	}

	public void setParamsToken(Collection<ResourceIndexedSearchParamToken> theParamsToken) {
		if (!isParamsTokenPopulated() && theParamsToken.isEmpty()) {
			return;
		}
		getParamsToken().clear();
		getParamsToken().addAll(theParamsToken);
	}

	public void setParamsTokenPopulated(boolean theParamsTokenPopulated) {
		myParamsTokenPopulated = theParamsTokenPopulated;
	}

	public void setResourceLinks(List<ResourceLink> theLinks) {
		if (!isHasLinks() && theLinks.isEmpty()) {
			return;
		}
		getResourceLinks().clear();
		getResourceLinks().addAll(theLinks);
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public void setVersion(long theVersion) {
		myVersion = theVersion;
	}

	public ResourceHistoryTable toHistory(FhirContext theCtx) {
		ResourceHistoryTable retVal = new ResourceHistoryTable();

		retVal.setResourceId(myId);
		retVal.setResourceType(myResourceType);
		retVal.setVersion(myVersion);

		retVal.setPublished(getPublished());
		retVal.setUpdated(getUpdated());
		retVal.setEncoding(getEncoding());
		retVal.setResource(getResource());

		for (ResourceTag next : getTags()) {
			retVal.addTag(next);
		}

		return retVal;
	}
}
