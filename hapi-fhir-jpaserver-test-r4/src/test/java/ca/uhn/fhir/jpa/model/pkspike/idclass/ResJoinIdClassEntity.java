package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.model.pkspike.IJoinEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.PartitionKey;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
	name = "RES_JOIN"
)
@IdClass(ResJoinIdClassEntity.ResJoinPK.class)
public class ResJoinIdClassEntity implements IJoinEntity<ResRootIdClassEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "PID")
	Long myId;

	@PartitionKey
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	// fixme mb which side controls vs reads?
	@ManyToOne(
		optional = false)
	@JoinColumns({
		@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID"),
//		@JoinColumn(name = "PARTITION_ID", referencedColumnName = "PARTITION_ID")
	})
	ResRootIdClassEntity myResource;

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
	public void setParent(ResRootIdClassEntity theRoot) {
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
		return myResource == null? null: myResource.myId;
	}


	static class ResJoinPK {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		@Column(name = "PID")
		Long myId;

		/** for Hibernate */
		public ResJoinPK() {}


		public ResJoinPK(Long theId) {
			myId = theId;
		}

		@Override
		public boolean equals(Object theO) {
			return EqualsBuilder.reflectionEquals(this,theO);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

	static class ResJoinCompositePK extends ResJoinPK {
		@Id
		@Column(name = "PARTITION_ID", nullable = false, insertable = false, updatable = false)
		Integer myPartitionId;

		/** for Hibernate */
		public ResJoinCompositePK() {}

		public ResJoinCompositePK(Long theId, Integer thePartitionId) {
			super(theId);
			myPartitionId = thePartitionId;
		}



	}
}
