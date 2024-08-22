package ca.uhn.fhir.jpa.model.pkspike.composite;

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
@IdClass(ResRootCompositeEntity.ResRootPK.class)
public class ResRootCompositeEntity {
	private static final Logger ourLog = LoggerFactory.getLogger(ResRootCompositeEntity.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	Long myId;

	@Id
	@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
	Integer myPartitionId;

	ResRootPK getPK() {
		return new ResRootPK(myId, myPartitionId);
	}

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource")
	Collection<ResJoinCompositeEntity> myJoinEntities = new ArrayList<>();

	public ResRootCompositeEntity() {
		ourLog.info("new ResRootCompositeEntity()");
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

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
			return EqualsBuilder.reflectionEquals(this,theO);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}

}
