package ca.uhn.fhir.jpa.model.pkspike.partitionkey;

import ca.uhn.fhir.jpa.model.pkspike.IRootEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.PartitionKey;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "RES_ROOT")
public class ResRootPartitionEntity implements IRootEntity<ResJoinPartitionEntity> {
	@Id
	@GeneratedValue()
	@Column(name = "RES_ID")
	Long myId;

	@PartitionKey
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource", fetch = FetchType.EAGER)
	Collection<ResJoinPartitionEntity> myJoinEntities = new ArrayList<>();

	public Long getId() {
		return myId;
	}

	public String getString() {
		return myString;
	}

	public void setString(String theString) {
		myString = theString;
	}

	@Override
	public Collection<ResJoinPartitionEntity> getJoins() {
		return myJoinEntities;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
		return myId;
	}

}
