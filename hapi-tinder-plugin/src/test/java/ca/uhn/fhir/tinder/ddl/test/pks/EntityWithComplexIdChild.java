package ca.uhn.fhir.tinder.ddl.test.pks;

import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ENTITY_WITH_COMPLEX_ID_CHILD")
@IdClass(ComplexId.class)
public class EntityWithComplexIdChild {

	@Id
	@SequenceGenerator(name = "SEQ_COMPLEX_CHILD_ID", sequenceName = "SEQ_COMPLEX_CHILD_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_COMPLEX_CHILD_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "PARTITION_ID")
	@Id
	@PartitionedIdProperty
	private Integer myPartitionId;

	@ManyToOne
	@JoinColumns(value = {
		@JoinColumn(name = "PARENT_PID", referencedColumnName = "PID"),
		@JoinColumn(name = "PARENT_PARTITION_ID", referencedColumnName = "PARTITION_ID")
	},
	foreignKey = @ForeignKey(name="FK_COMPLEX_ID_PARENT_CHILD"))
	private EntityWithComplexIdParent myParent;

	@Column(name = "PARENT_PARTITION_ID", insertable = false, updatable = false)
	private Long myParentPartitionId;

	@Column(name = "NAME")
	private String myName;

}
