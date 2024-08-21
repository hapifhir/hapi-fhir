package ca.uhn.fhir.jpa.model.pkspike;

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
@Table(
	name = "RES_ROOT"
//	uniqueConstraints = {
//		@UniqueConstraint(
//			name = IDX_RES_TYPE_FHIR_ID,
//			columnNames = {"RES_TYPE", "FHIR_ID"})
//	},
//	indexes = {
//		// Do not reuse previously used index name: IDX_INDEXSTATUS, IDX_RES_TYPE
//		@Index(name = "IDX_RES_DATE", columnList = BaseHasResource.RES_UPDATED),
//		@Index(name = "IDX_RES_FHIR_ID", columnList = "FHIR_ID"),
//		@Index(
//			name = "IDX_RES_TYPE_DEL_UPDATED",
//			columnList = "RES_TYPE,RES_DELETED_AT,RES_UPDATED,PARTITION_ID,RES_ID"),
//		@Index(name = "IDX_RES_RESID_UPDATED", columnList = "RES_ID, RES_UPDATED, PARTITION_ID")
//	}
	)
public class ResRootEntity {
	@Id
//	@GenericGenerator(name = "SEQ_RESOURCE_ID", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
