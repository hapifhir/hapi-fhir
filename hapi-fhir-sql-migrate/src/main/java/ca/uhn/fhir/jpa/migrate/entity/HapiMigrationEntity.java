package ca.uhn.fhir.jpa.migrate.entity;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

// Note even though we are using javax.persistence annotations here, we are managing these records outside of jpa
// so these annotations are for informational purposes only
@Entity
public class HapiMigrationEntity {
	public static final int VERSION_MAX_SIZE = 50;
	public static final int DESCRIPTION_MAX_SIZE = 200;
	public static final int TYPE_MAX_SIZE = 20;
	public static final int SCRIPT_MAX_SIZE = 1000;
	public static final int INSTALLED_BY_MAX_SIZE = 100;
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

	public static HapiMigrationEntity fromBaseTask(BaseTask theTask) {
		HapiMigrationEntity retval = new HapiMigrationEntity();
		retval.setVersion(theTask.getMigrationVersion());
		retval.setDescription(theTask.getDescription());
		retval.setChecksum(theTask.hashCode());
		retval.setType("JDBC");
		return retval;
	}

	public static RowMapper<HapiMigrationEntity> newRowMapper() {
		return (rs, rowNum) -> {
			HapiMigrationEntity entity = new HapiMigrationEntity();
			entity.setPid(rs.getInt(1));
			entity.setVersion(rs.getString(2));
			entity.setDescription(rs.getString(3));
			entity.setType(rs.getString(4));
			entity.setScript(rs.getString(5));
			entity.setChecksum(rs.getInt(6));
			entity.setInstalledBy(rs.getString(7));
			entity.setInstalledOn(rs.getTimestamp(8));
			entity.setExecutionTime(rs.getInt(9));
			entity.setSuccess(rs.getBoolean(10));
			return entity;
		};
	}
}
