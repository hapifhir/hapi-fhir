package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "HFJ_PARTITION", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_PART_NAME", columnNames = {"PART_NAME"})
})
public class PartitionEntity {

	public static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_DESC_LENGTH = 200;

	/**
	 * Note that unlike most PID columns in HAPI FHIR JPA, this one is an Integer, and isn't
	 * auto assigned.
	 */
	@Id
	@Column(name = "PART_ID", nullable = false)
	private Integer myId;
	@Column(name = "PART_NAME", length = MAX_NAME_LENGTH, nullable = false)
	private String myName;
	@Column(name = "PART_DESC", length = MAX_DESC_LENGTH, nullable = true)
	private String myDescription;

	public Integer getId() {
		return myId;
	}

	public PartitionEntity setId(Integer theId) {
		myId = theId;
		return this;
	}

	public String getName() {
		return myName;
	}

	public PartitionEntity setName(String theName) {
		myName = theName;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

}
