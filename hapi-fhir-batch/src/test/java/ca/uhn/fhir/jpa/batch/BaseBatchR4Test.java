package ca.uhn.fhir.jpa.batch;

import ca.uhn.fhir.jpa.batch.config.BatchJobConfig;
import ca.uhn.fhir.jpa.batch.config.TestBatchConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BatchJobConfig.class, TestBatchConfig.class})
abstract public class BaseBatchR4Test {
}
