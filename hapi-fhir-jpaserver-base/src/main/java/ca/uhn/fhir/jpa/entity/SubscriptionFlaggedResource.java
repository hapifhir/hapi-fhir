package ca.uhn.fhir.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HFJ_SUBSCRIPTION_FLAG_RES")
public class SubscriptionFlaggedResource {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_SUBSCRIPTION_FLAG_ID", sequenceName = "SEQ_SUBSCRIPTION_FLAG_ID")
	@Column(name = "PID", insertable = false, updatable = false)
	private Long myId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false)
	private Date myCreated;

}
