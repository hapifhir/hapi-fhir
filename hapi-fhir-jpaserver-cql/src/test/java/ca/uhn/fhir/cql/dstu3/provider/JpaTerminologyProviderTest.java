package ca.uhn.fhir.cql.dstu3.provider;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpaTerminologyProviderTest extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaTerminologyProviderTest.class);

	@Autowired
	protected TerminologyProvider jpaTerminologyProvider;

	@Autowired
	protected DaoConfig daoConfig;

	@BeforeEach
	public void before() throws IOException {
		// Load executable (i.e. "pre-expanded") value set
		loadResource("dstu3/provider/test-executable-value-set.json", myRequestDetails);
	}

	@Test
	public void testTerminologyProviderExpand() {
		ValueSetInfo valueSetInfo = new ValueSetInfo().withId("http://test.com/fhir/ValueSet/test-executable-value-set");
		Iterable<Code> codeIterable = this.jpaTerminologyProvider.expand(valueSetInfo);

		assertNotNull(codeIterable);

		List<Code> codes = new ArrayList<>();

		codeIterable.forEach(codes::add);

		assertThat(codes, hasSize(2));
	}
}
