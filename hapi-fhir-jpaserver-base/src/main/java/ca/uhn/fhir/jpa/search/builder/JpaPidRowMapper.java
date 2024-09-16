package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JpaPidRowMapper implements RowMapper<JpaPid> {
	@Override
	public JpaPid mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer partitionId = rs.getInt(1);
		Long resourceId = rs.getLong(2);
		return JpaPid.fromId(resourceId, partitionId);
	}
}
