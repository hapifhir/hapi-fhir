package ca.uhn.fhir.jpa.migrate.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "FLY_HFJ_MIGRATION")
public class HapiMigrationEntity implements Persistable<Integer> {
	private static final int VERSION_MAX_SIZE = 50;
	private static final int DESCRIPTION_MAX_SIZE = 200;
	private static final int TYPE_MAX_SIZE = 20;
	private static final int SCRIPT_MAX_SIZE = 1000;
	private static final int INSTALLED_BY_MAX_SIZE = 100;
	@Id
	@SequenceGenerator(name = "SEQ_FLY_HFJ_MIGRATION", sequenceName = "SEQ_FLY_HFJ_MIGRATION")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FLY_HFJ_MIGRATION")
	@Column(name = "INSTALLED_RANK")
	private Integer myPid;

	@Column(name = "VERSION", length = VERSION_MAX_SIZE)
	private String myVersion;

	@Column(name = "DESCRIPTION", length = DESCRIPTION_MAX_SIZE)
	private String myDescription;

	@Column(name = "TYPE", length = TYPE_MAX_SIZE)
	private String myType;

	@Column(name = "SCRIPT", length = SCRIPT_MAX_SIZE)
	private String myScript;

	@Column(name = "CHECKSUM")
	private Integer myChecksum;

	@Column(name = "INSTALLED_BY", length = INSTALLED_BY_MAX_SIZE)
	private String myInstalledBy;

	@Column(name = "INSTALLED_ON")
	private Date myInstalledOn;

	@Column(name = "EXECUTION_TIME")
	private Integer myExecutionTime;

	@Column(name = "SUCCESS")
	private Boolean mySuccess;

	@Override
	public Integer getId() {
		return myPid;
	}

	@Override
	public boolean isNew() {
		return null == getId();
	}

	public Integer getPid() {
		return myPid;
	}

	public void setPid(Integer thePid) {
		myPid = thePid;
	}

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		myVersion = theVersion;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public String getType() {
		return myType;
	}

	public void setType(String theType) {
		myType = theType;
	}

	public String getScript() {
		return myScript;
	}

	public void setScript(String theScript) {
		myScript = theScript;
	}

	public Integer getChecksum() {
		return myChecksum;
	}

	public void setChecksum(Integer theChecksum) {
		myChecksum = theChecksum;
	}

	public String getInstalledBy() {
		return myInstalledBy;
	}

	public void setInstalledBy(String theInstalledBy) {
		myInstalledBy = theInstalledBy;
	}

	public Date getInstalledOn() {
		return myInstalledOn;
	}

	public void setInstalledOn(Date theInstalledOn) {
		myInstalledOn = theInstalledOn;
	}

	public Integer getExecutionTime() {
		return myExecutionTime;
	}

	public void setExecutionTime(Integer theExecutionTime) {
		myExecutionTime = theExecutionTime;
	}

	public Boolean getSuccess() {
		return mySuccess;
	}

	public void setSuccess(Boolean theSuccess) {
		mySuccess = theSuccess;
	}
}
