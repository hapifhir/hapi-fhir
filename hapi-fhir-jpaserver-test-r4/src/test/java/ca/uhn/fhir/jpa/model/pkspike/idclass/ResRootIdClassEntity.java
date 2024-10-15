package ca.uhn.fhir.jpa.model.pkspike.idclass;

import ca.uhn.fhir.jpa.model.pkspike.IRootEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.PartitionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * fixme MB IdClass vs embeddable?
 *
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "RES_ROOT")
@IdClass(ResRootIdClassEntity.ResRootPK.class)
public class ResRootIdClassEntity implements IRootEntity<ResJoinIdClassEntity> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResRootIdClassEntity.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	Long myId;

//	@Id
	//@PartitionKey
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

//	ResRootPK getPK() {
//		return new ResRootPK(myId, myPartitionId);
//	}

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource")
	Collection<ResJoinIdClassEntity> myJoinEntities = new ArrayList<>();

	public ResRootIdClassEntity() {
		ourLog.info("new ResRootCompositeEntity()");
	}

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
	public Collection<ResJoinIdClassEntity> getJoins() {
		return myJoinEntities;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	static class ResRootPK {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "RES_ID")
		Long myId;

		/** for Hibernate */
		public ResRootPK() {}


		public ResRootPK(Long theId) {
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

	static class ResRootCompositePK extends ResRootPK {
		@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
		Integer myPartitionId;

		/** for Hibernate */
		public ResRootCompositePK() {}

		public ResRootCompositePK(Long theId, Integer thePartitionId) {
			super(theId);
			myPartitionId = thePartitionId;
		}



	}

}
