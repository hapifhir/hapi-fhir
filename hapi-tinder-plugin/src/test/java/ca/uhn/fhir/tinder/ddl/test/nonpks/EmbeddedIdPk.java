package ca.uhn.fhir.tinder.ddl.test.nonpks;

import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;


@Embeddable
public class EmbeddedIdPk {

	@Column(name = "PID")
	private Long myId;

	@Column(name = "PARTITION_ID")
	@PartitionedIdProperty
	private Integer myPartitionId;

}
