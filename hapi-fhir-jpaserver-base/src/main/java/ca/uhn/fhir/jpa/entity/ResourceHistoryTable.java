package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;

@Entity
@Table(name = "HFJ_RES_VER", uniqueConstraints = {})
@org.hibernate.annotations.Table(appliesTo="HFJ_RES_VER", indexes= {@Index(name="IDX_RES_VER_DATE", columnNames= {"RES_UPDATED"})})
public class ResourceHistoryTable extends BaseHasResource implements Serializable {

	public static final String Q_GETALL = "SELECT h FROM ResourceHistoryTable h WHERE h.myPk.myId = :PID AND h.myPk.myResourceType = :RESTYPE ORDER BY h.myUpdated ASC";

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ResourceHistoryTablePk myPk;

	@Column(name="RES_TYPE",insertable=false, updatable=false)
	private String myResourceType;

	@Column(name="PID", insertable=false, updatable=false)
	private Long myId;

	@OneToMany(mappedBy = "myResourceHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Collection<ResourceHistoryTag> myTags;

	@Override
	public IdDt getIdDt() {
		return new IdDt(myPk.getResourceType() + '/' + myPk.getId() + '/' + Constants.PARAM_HISTORY + '/' + myPk.getVersion());
	}

	public ResourceHistoryTablePk getPk() {
		return myPk;
	}

	@SuppressWarnings("unchecked")
	public String getResourceType() {
		return myPk.getResourceType();
//		try {
//			return (Class<IResource>) Class.forName(Patient.class.getPackage().getName() + "." + myPk.getResourceType());
//		} catch (ClassNotFoundException e) {
//			throw new InternalErrorException(e);
//		}
	}

	public Collection<ResourceHistoryTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<ResourceHistoryTag>();
		}
		return myTags;
	}

	@Override
	public long getVersion() {
		return myPk.getVersion();
	}

	public void setPk(ResourceHistoryTablePk thePk) {
		myPk = thePk;
	}

	public void addTag(ResourceHistoryTag theTag) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTag().getTerm().equals(theTag)) {
				return;
			}
		}
		getTags().add(theTag);
	}

	public boolean hasTag(String theTerm, String theLabel, String theScheme) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTag().getScheme().equals(theScheme) && next.getTag().getTerm().equals(theTerm)) {
				return true;
			}
		}
		return false;
	}

	public void addTag(ResourceTag theTag) {
		getTags().add(new ResourceHistoryTag(this, theTag.getTag()));
	}

}
