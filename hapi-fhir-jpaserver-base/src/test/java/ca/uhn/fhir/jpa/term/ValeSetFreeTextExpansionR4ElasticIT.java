package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class runs all parent class tests using Elasticsearch configuration
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
public class ValeSetFreeTextExpansionR4ElasticIT extends AbstractValeSetFreeTextExpansionR4Test {

}
