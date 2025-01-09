package ca.uhn.fhir.tinder.ddl.test.pks;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENTITY_WITH_EMBEDDED_ID_CHILD")
public class EntityWithEmbeddedIdChild {

	@EmbeddedId
	private EntityWithEmbeddedIdPk myId;

	@ManyToOne
	@JoinColumns(value = {
		@JoinColumn(name = "PARENT_PID", referencedColumnName = "PID"),
		@JoinColumn(name = "PARENT_PARTITION_ID", referencedColumnName = "PARTITION_ID")
	},
	foreignKey = @ForeignKey(name="FK_EMBEDDED_ID_PARENT_CHILD"))
	private EntityWithComplexIdParent myParent;

	@Column(name = "PARENT_PARTITION_ID", insertable = false, updatable = false)
	private Long myParentPartitionId;

	@Column(name = "NAME")
	private String myName;

}
