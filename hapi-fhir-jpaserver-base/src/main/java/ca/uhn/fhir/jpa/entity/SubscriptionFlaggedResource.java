package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_SUBSCRIPTION_FLAG_RES")
public class SubscriptionFlaggedResource {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_SUBSCRIPTION_FLAG_ID", sequenceName = "SEQ_SUBSCRIPTION_FLAG_ID")
	@Column(name = "PID", insertable = false, updatable = false)
	private Long myId;

	@ManyToOne
	@JoinColumn(name="RES_ID")
	private ResourceTable myResource;
	
	@ManyToOne()
	@JoinColumn(name="SUBSCRIPTION_ID")
	private SubscriptionTable mySubscription;

	public ResourceTable getResource() {
		return myResource;
	}

	public SubscriptionTable getSubscription() {
		return mySubscription;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setSubscription(SubscriptionTable theSubscription) {
		mySubscription = theSubscription;
	}
	
}
