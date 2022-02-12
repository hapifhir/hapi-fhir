package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.jpa.bulk.imprt2.BulkImport2AppCtx;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//When you define a new batch job, add it here.
@Configuration
@Import({
	BulkImport2AppCtx.class
})
public class Batch2JobsConfig {
	// nothing
}
