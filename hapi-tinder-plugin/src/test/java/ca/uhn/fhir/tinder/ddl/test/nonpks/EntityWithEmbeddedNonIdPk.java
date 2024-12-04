package ca.uhn.fhir.tinder.ddl.test.nonpks;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENTITY_WITH_EMBEDDED_NON_ID")
public class EntityWithEmbeddedNonIdPk {

	@Id
	@Column(name="MY_ID")
	private String myId;

	@Embedded
	@AttributeOverride(name = "myId", column = @Column(name = "LB_RES_ID", insertable = true, updatable = false))
	@AttributeOverride(
		name = "myPartitionId",
		column = @Column(name = "LB_RES_PARTITION_ID", insertable = true, updatable = false, nullable = true))
	private EmbeddedIdPk myEmbedded;

}
