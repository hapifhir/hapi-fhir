/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.migrate.entity;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.util.VersionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

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
	public static final int CREATE_TABLE_PID = -1;
	public static final String INITIAL_RECORD_DESCRIPTION = "<< HAPI FHIR Schema History table created >>";
	public static final String INITIAL_RECORD_SCRIPT = "HAPI FHIR";

	@Id
	@GenericGenerator(
			name = "SEQ_FLY_HFJ_MIGRATION",
			strategy = "ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator")
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

	public static HapiMigrationEntity tableCreatedRecord() {
		HapiMigrationEntity retVal = new HapiMigrationEntity();
		retVal.setPid(CREATE_TABLE_PID);
		retVal.setDescription(INITIAL_RECORD_DESCRIPTION);
		retVal.setType("TABLE");
		retVal.setScript(INITIAL_RECORD_SCRIPT);
		retVal.setInstalledBy(VersionEnum.latestVersion().name());
		retVal.setInstalledOn(new Date());
		retVal.setExecutionTime(0);
		retVal.setSuccess(true);
		return retVal;
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

	public static HapiMigrationEntity fromBaseTask(BaseTask theTask) {
		HapiMigrationEntity retval = new HapiMigrationEntity();
		retval.setVersion(theTask.getMigrationVersion());
		retval.setDescription(theTask.getDescription());
		retval.setChecksum(theTask.hashCode());
		retval.setType("JDBC");
		return retval;
	}

	public static RowMapper<HapiMigrationEntity> rowMapper() {
		return (rs, rowNum) -> {
			HapiMigrationEntity entity = new HapiMigrationEntity();
			entity.setPid(rs.getInt(1));
			entity.setVersion(rs.getString(2));
			entity.setDescription(rs.getString(3));
			entity.setType(rs.getString(4));
			entity.setScript(rs.getString(5));
			entity.setChecksum(rs.getInt(6));
			entity.setInstalledBy(rs.getString(7));
			entity.setInstalledOn(rs.getDate(8));
			entity.setExecutionTime(rs.getInt(9));
			entity.setSuccess(rs.getBoolean(10));
			return entity;
		};
	}

	public PreparedStatementSetter asPreparedStatementSetter() {
		return ps -> {
			ps.setInt(1, getPid());
			ps.setString(2, getVersion());
			ps.setString(3, getDescription());
			ps.setString(4, getType());
			ps.setString(5, getScript());
			if (getChecksum() == null) {
				ps.setNull(6, java.sql.Types.INTEGER);
			} else {
				ps.setInt(6, getChecksum());
			}
			ps.setString(7, getInstalledBy());
			ps.setDate(
					8,
					getInstalledOn() != null
							? new java.sql.Date(getInstalledOn().getTime())
							: null);
			ps.setInt(9, getExecutionTime());
			ps.setBoolean(10, getSuccess());
		};
	}
}
