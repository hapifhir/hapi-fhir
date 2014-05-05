package ca.uhn.fhir.jpa.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;

@Entity
@Table(name = "BASE_RES", uniqueConstraints = {})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "SVCVER_TYPE", length = 20, discriminatorType = DiscriminatorType.STRING)
public abstract class BaseResourceTable<T extends IResource> extends BaseHasResource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	private Long myId;

	@Version()
	@Column(name = "RES_VER")
	private Long myVersion;

	@OneToMany(mappedBy="myResource",cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Collection<ResourceTag> myTags;
	
	public IdDt getId() {
		return new IdDt(myId);
	}
	
	public void addTag(String theTerm, String theLabel, String theScheme) {
		for (ResourceTag next : getTags()) {
			if (next.getTerm().equals(theTerm)) {
				return;
			}
		}
		getTags().add(new ResourceTag(this, theTerm, theLabel, theScheme));
	}

	public Collection<ResourceTag> getTags() {
		if (myTags==null) {
			myTags = new ArrayList<>();
		}
		return myTags;
	}

	public abstract Class<T> getResourceType();

	public IdDt getVersion() {
		return new IdDt(myVersion);
	}

	public void setId(IdDt theId) {
		myId = theId.asLong();
	}

	public void setVersion(IdDt theVersion) {
		myVersion = theVersion.asLong();
	}

	public ResourceHistoryTable toHistory(FhirContext theCtx) {
		ResourceHistoryTable retVal = new ResourceHistoryTable();

		ResourceHistoryTablePk pk = new ResourceHistoryTablePk();
		pk.setId(myId);
		pk.setResourceType(theCtx.getResourceDefinition(getResourceType()).getName());
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
