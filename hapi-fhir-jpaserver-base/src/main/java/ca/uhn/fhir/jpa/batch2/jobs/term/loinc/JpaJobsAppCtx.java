package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	ImportLoincJobAppCtx.class
})
public class JpaJobsAppCtx {
}
