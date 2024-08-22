package ca.uhn.fhir.jpa.model.pkspike.composite;

import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "RES_ROOT")
@IdClass(ResRootCompositeEntity.ResRootPK.class)
public class ResRootCompositeEntity {
	static class ResRootPK {
		@GeneratedValue()
		@Column(name = "RES_ID")
		Long myId;

		@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
		Integer myPartitionId;

		/** For Hibernate */
		protected ResRootPK() {}

		public ResRootPK(Long theId, Integer thePartitionId) {
			myId = theId;
			myPartitionId = thePartitionId;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (theO == null || getClass() != theO.getClass()) return false;
			ResRootPK resRootPK = (ResRootPK) theO;
			return Objects.equal(myId, resRootPK.myId) && Objects.equal(myPartitionId, resRootPK.myPartitionId);
		}

		@Override
		public int hashCode() {
			return java.util.Objects.hash(myId, myPartitionId);
		}
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	Long myId;

	@Id
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

//	ResRootPK getPK() {
//		return new ResRootPK(myId, myPartitionId);
//	}

	@Column(name = "STRING_COL")
	String myString;

//	@OneToMany(mappedBy = "myResource", fetch = FetchType.EAGER)
//	Collection<ResJoinCompositeEntity> myJoinEntities = new ArrayList<>();

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
