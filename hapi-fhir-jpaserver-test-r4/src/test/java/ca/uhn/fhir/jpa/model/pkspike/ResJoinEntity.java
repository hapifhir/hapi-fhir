package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
public class ResJoinEntity {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PID")
	Long myId;
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Long myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@ManyToOne(
		optional = false)
	@JoinColumn(
		name = "RES_ID",
		referencedColumnName = "RES_ID",
		nullable = false,
		updatable = false)
	ResRootEntity myResource;
}
