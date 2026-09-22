package ca.uhn.fhir.jpa.model.pkspike.embeddedid;

import ca.uhn.fhir.jpa.model.pkspike.IRootEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
public class ResRootEmbeddedIdEntity implements IRootEntity<ResJoinEmbeddedIdEntity> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResRootEmbeddedIdEntity.class);

	@EmbeddedId
	ResRootPK myId = new ResRootPK();

	@Column(name = "PARTITION_ID", nullable = true, insertable = false, updatable = false)
	Integer myPartitionId;

	@Column(name = "STRING_COL")
	String myString;

	@OneToMany(mappedBy = "myResource")
	Collection<ResJoinEmbeddedIdEntity> myJoinEntities = new ArrayList<>();

	public ResRootEmbeddedIdEntity() {
		ourLog.info("new ResRootCompositeEntity()");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Long getResId() {
		return myId==null?null:myId.myId;
	}

	@Override
	public void setPartitionId(Integer thePartitionId) {
		myPartitionId = thePartitionId;
		myId.myPartitionId = thePartitionId;
	}

	@Override
	public Integer getPartitionId() {
		return myPartitionId;
	}

	@Override
	public String getString() {
		return myString;
	}

	@Override
	public void setString(String theString) {
		myString = theString;
	}

	@Override
	public Collection<ResJoinEmbeddedIdEntity> getJoins() {
		return myJoinEntities;
	}

	@Embeddable
	static class ResRootPK {
		@GeneratedValue()
		@Column(name = "RES_ID")
		public Long myId;

		@Column(name = "PARTITION_ID", nullable = true, insertable = true, updatable = false)
		public Integer myPartitionId;

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

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

}
