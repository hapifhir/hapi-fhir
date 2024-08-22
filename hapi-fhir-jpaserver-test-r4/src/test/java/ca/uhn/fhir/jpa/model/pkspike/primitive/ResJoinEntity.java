package ca.uhn.fhir.jpa.model.pkspike.primitive;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
public class ResJoinEntity {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
