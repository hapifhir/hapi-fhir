import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.DatabaseTypeRegister;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class FlywayDbCompatibilityTest {
	@Test
	public void testFlywayDbCompatibility() {
		DatabaseType h2Type = DatabaseTypeRegister.getDatabaseTypeForUrl("jdbc:h2:mem:test");
		assertThat(h2Type.getName(), is(equalTo("H2")));

		DatabaseType sqlServerType = DatabaseTypeRegister.getDatabaseTypeForUrl("jdbc:sqlserver://localhost:1433;database=test");
		assertThat(sqlServerType.getName(), is(equalTo("Azure Synapse")));

		DatabaseType oracleType = DatabaseTypeRegister.getDatabaseTypeForUrl("jdbc:oracle:thin:@//host:port/service");
		assertThat(oracleType.getName(), is(equalTo("Azure Synapse")));

	}

}
