package ca.uhn.fhir.tinder.ddl.test;

import ca.uhn.hapi.fhir.sql.hibernatesvc.ConditionalIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Embeddable
public class EntityWithEmbeddedIdPk {

	@SequenceGenerator(name = "SEQ_COMPLEX_PARENT_ID", sequenceName = "SEQ_COMPLEX_PARENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_COMPLEX_PARENT_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "PARTITION_ID")
	@ConditionalIdProperty
	private Integer myPartitionId;

}
