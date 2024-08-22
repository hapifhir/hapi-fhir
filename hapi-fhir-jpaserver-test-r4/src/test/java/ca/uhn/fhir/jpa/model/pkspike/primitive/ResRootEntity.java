package ca.uhn.fhir.jpa.model.pkspike.primitive;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "RES_ROOT")
public class ResRootEntity {
	@Id
	@GeneratedValue()
	@Column(name = "RES_ID")
	Long myId;

	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Long myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource", fetch = FetchType.EAGER)
	Collection<ResJoinEntity> myJoinEntities = new ArrayList<>();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getId() {
		return myId;
	}

	public String getString() {
		return myString;
	}

	public void setString(String theString) {
		myString = theString;
	}
}
