package ca.uhn.fhir.jpa.model.pkspike.primitive;

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

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "RES_ROOT")
public class ResRootEntity implements IRootEntity<ResJoinEntity> {
	@Id
	@GeneratedValue()
	@Column(name = "RES_ID")
	Long myId;

	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource", fetch = FetchType.EAGER)
	Collection<ResJoinEntity> myJoinEntities = new ArrayList<>();

	public Long getId() {
		return myId;
	}

	@Override
	public Long getResId() {
		return myId;
	}

	@Override
	public void setPartitionId(Integer thePartitionId) {
		myPartitionId = thePartitionId;
	}

	@Override
	public Integer getPartitionId() {
		return myPartitionId;
	}

	public String getString() {
		return myString;
	}

	public void setString(String theString) {
		myString = theString;
	}

	@Override
	public Collection<ResJoinEntity> getJoins() {
		return myJoinEntities;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
