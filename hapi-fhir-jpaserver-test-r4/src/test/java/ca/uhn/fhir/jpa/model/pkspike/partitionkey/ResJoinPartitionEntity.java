package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

import ca.uhn.fhir.jpa.model.pkspike.EntityFixture;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.PartitionKey;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
public class ResJoinPartitionEntity implements EntityFixture.IJoinEntity<ResRootPartitionEntity> {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "PID")
	Long myId;
	@PartitionKey
	@Column(name = "PARTITION_ID", nullable = true, insertable = false, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@Column(name = "RES_ID", nullable = false, insertable = false, updatable = false)
	Long myResId;

	@ManyToOne(
		optional = false)
	@JoinColumns({
		@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, insertable = true, updatable = false),
		@JoinColumn(name = "PARTITION_ID", referencedColumnName = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	})
	ResRootPartitionEntity myResource;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

	@Override
	public void setString(String theString) {
		myString = theString;
	}

	@Override
	public void setParent(ResRootPartitionEntity theRoot) {
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

	@Override
	public Long getResId() {
		return myResId;
	}
}
