package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class SchemaInitializationProviderTest {

	@Test
	public void testParseSqlFileIntoIndividualStatements() {
		SchemaInitializationProvider svc = new SchemaInitializationProvider(null, null, null, true);

		String input = """
				create sequence foo;
				
				alter table if exists CDR_XACT_LOG_STEP
			      add constraint FK_XACTLOGSTEP_XACTLOG
			      foreign key (LOG_PID)
			      -- comment in a weird spot
			      references CDR_XACT_LOG;
			  
			    -- we can't use convering index until the autovacuum runs for those rows, which kills index performance
			  ALTER TABLE hfj_resource SET (autovacuum_vacuum_scale_factor = 0.01);
			  ALTER TABLE hfj_forced_id SET (autovacuum_vacuum_scale_factor = 0.01);
			""";
		List<String> listToPopulate = new ArrayList<>();
		svc.parseSqlFileIntoIndividualStatements(DriverTypeEnum.POSTGRES_9_4, listToPopulate, input);

		assertThat(listToPopulate.toString(), listToPopulate, contains(
			"create sequence foo",
			"alter table if exists CDR_XACT_LOG_STEP add constraint FK_XACTLOGSTEP_XACTLOG foreign key (LOG_PID) references CDR_XACT_LOG",
			"ALTER TABLE hfj_resource SET (autovacuum_vacuum_scale_factor = 0.01)",
			"ALTER TABLE hfj_forced_id SET (autovacuum_vacuum_scale_factor = 0.01)"
		));

	}

}
