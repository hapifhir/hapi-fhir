package ca.uhn.fhir.jpa.model.pkspike;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

public class SchemaCleanerExtension implements AfterEachCallback {

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		ApplicationContext springContext = SpringExtension.getApplicationContext(theExtensionContext);
		var dataSource = springContext.getBean(DataSource.class);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("delete from res_join");
		jdbcTemplate.execute("delete from res_root");
	}
}
