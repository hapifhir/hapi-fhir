package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_SUBSCRIPTION_CAND_RES")
public class SubscriptionCandidateResource {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_SUBSCRIPTION_CAND_ID", sequenceName = "SEQ_SUBSCRIPTION_CAND_ID")
	@Column(name = "PID", insertable = false, updatable = false)
	private Long myId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID")
	private ResourceTable myResource;

	@Column(name = "RES_VERSION", nullable = false)
	private long myResourceVersion;

	public ResourceTable getResource() {
		return myResource;
	}

	public long getResourceVersion() {
		return myResourceVersion;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourceVersion(long theResourceVersion) {
		myResourceVersion = theResourceVersion;
	}

}
