package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.test.BaseValueSetHSearchExpansionR4Test;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class runs all parent class tests using Elasticsearch configuration
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestR4ConfigWithElasticHSearch.class)
@RequiresDocker
public class ValueSetHSearchExpansionR4ElasticIT extends BaseValueSetHSearchExpansionR4Test {

}
