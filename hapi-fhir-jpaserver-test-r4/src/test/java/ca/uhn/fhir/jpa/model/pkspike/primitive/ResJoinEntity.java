package ca.uhn.fhir.jpa.model.pkspike.primitive;

import ca.uhn.fhir.jpa.model.pkspike.IJoinEntity;
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
public class ResJoinEntity implements IJoinEntity<ResRootEntity> {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "PID")
	Long myId;
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@Column(name = "RES_ID", nullable = false, insertable = false, updatable = false)
	Long myResId;

	@ManyToOne(
		optional = false)
	@JoinColumn(
		name = "RES_ID",
		referencedColumnName = "RES_ID",
		nullable = false,
		updatable = false)
	ResRootEntity myResource;

	@Override
	public Long getResId() {
		return myResId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public Long getPid() {
		return myId;
	}

	@Override
	public void setString(String theString) {
		myString = theString;
	}

	@Override
	public void setParent(ResRootEntity theRoot) {
		myResource = theRoot;
	}

	@Override
	public String getString() {
		return myString;
	}

	@Override
	public void setPartitionId(Integer thePartitionId) {
		myPartitionId = thePartitionId;
	}

	@Override
	public Integer getPartitionId() {
		return myPartitionId;
	}
}
