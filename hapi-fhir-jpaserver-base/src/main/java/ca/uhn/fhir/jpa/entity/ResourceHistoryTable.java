package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

@Entity
@Table(name = "RES_VER", uniqueConstraints = {})
public class ResourceHistoryTable extends BaseHasResource implements Serializable {

	public static final String Q_GETALL = "SELECT h FROM ResourceHistoryTable h WHERE h.myPk.myId = :PID AND h.myPk.myResourceType = :RESTYPE ORDER BY h.myUpdated ASC";

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ResourceHistoryTablePk myPk;

	@OneToMany(mappedBy = "myResourceHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Collection<ResourceHistoryTag> myTags;

	@Override
	public IdDt getIdDt() {
		return new IdDt(myPk.getId());
	}

	public ResourceHistoryTablePk getPk() {
		return myPk;
	}

	@SuppressWarnings("unchecked")
	public Class<IResource> getResourceType() {
		try {
			return (Class<IResource>) Class.forName(Patient.class.getPackage().getName() + "." + myPk.getResourceType());
		} catch (ClassNotFoundException e) {
			throw new InternalErrorException(e);
		}
	}

	public Collection<ResourceHistoryTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<>();
		}
		return myTags;
	}

	@Override
	public IdDt getVersion() {
		return new IdDt(myPk.getVersion());
	}

	public void setPk(ResourceHistoryTablePk thePk) {
		myPk = thePk;
	}

	public void addTag(String theTerm, String theLabel, String theScheme) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTerm().equals(theTerm)) {
				return;
			}
		}
		getTags().add(new ResourceHistoryTag(this, theTerm, theLabel, theScheme));
	}

}
