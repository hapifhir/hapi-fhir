package ca.uhn.fhir.jpa.model.pkspike.embeddedid;

import ca.uhn.fhir.jpa.model.pkspike.IJoinEntity;
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

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
public class ResJoinEmbeddedIdEntity implements IJoinEntity<ResRootEmbeddedIdEntity> {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "PID")
	Long myId;
	@Column(name = "PARTITION_ID", nullable = true, insertable = false, updatable = false)
	Integer myPartitionId;
	@Column(name = "RES_ID", nullable = false, updatable = false, insertable = false)
	Long myResourceId;
	@Column(name = "STRING_COL")
	String myString;

	// fixme mb which side controls vs reads?
	@ManyToOne(
		optional = false)
	@JoinColumns({
		@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID"),
		@JoinColumn(name = "PARTITION_ID", referencedColumnName = "PARTITION_ID")
	})
	ResRootEmbeddedIdEntity myResource;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
	public void setParent(ResRootEmbeddedIdEntity theRoot) {
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
		// fixme keep copy
		return myResource==null?null:myResource.getResId();
	}
}
