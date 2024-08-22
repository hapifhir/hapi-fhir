package ca.uhn.fhir.jpa.model.pkspike.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
public class ResJoinCompositeEntity {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PID")
	Long myId;
	@Column(name = "PARTITION_ID", nullable = true, insertable = false, updatable = false)
	Long myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	// fixme mb which side controls vs reads?
	@ManyToOne(
		optional = false)
	@JoinColumns({
		@JoinColumn(
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false,
			insertable = true,
			updatable = false),
		@JoinColumn(name = "PARTITION_ID", referencedColumnName = "PARTITION_ID")
	})
	ResRootCompositeEntity myResource;
}
