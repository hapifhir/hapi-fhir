package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.test.BaseValueSetHSearchExpansionR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class runs all parent class tests using Lucene configuration
 * There is also a LuceneFilesystem configuration available, for debugging purposes
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.DefaultLuceneHeap.class})
public class ValueSetHSearchExpansionR4LuceneIT extends BaseValueSetHSearchExpansionR4Test {

}
