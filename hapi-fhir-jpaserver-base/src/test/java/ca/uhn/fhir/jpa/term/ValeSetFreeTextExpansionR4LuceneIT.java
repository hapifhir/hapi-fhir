package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class runs all parent class tests using Lucene configuration
 * There is also a LuceneFilesystem configuration available, for debugging purposes
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.DefaultLuceneHeap.class})
public class ValeSetFreeTextExpansionR4LuceneIT extends AbstractValeSetFreeTextExpansionR4Test {

}
