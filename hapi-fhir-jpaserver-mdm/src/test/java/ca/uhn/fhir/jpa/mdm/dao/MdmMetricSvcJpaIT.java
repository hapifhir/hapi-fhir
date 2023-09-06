package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMetricSvcJpaImpl;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.parameters.MdmGenerateMetricParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {
	MdmMetricSvcJpaIT.TestConfig.class
})
public class MdmMetricSvcJpaIT extends BaseMdmR4Test {

	@Configuration
	public static class TestConfig {

		@Autowired
		private IMdmLinkJpaRepository myJpaRepository;

		@Bean
		IMdmMetricSvc mdmMetricSvc() {
			return new MdmMetricSvcJpaImpl(myJpaRepository);
		}
	}

	@Autowired
	private MdmLinkHelper myLinkHelper;

	@Autowired
	private IMdmMetricSvc mySvc;

	@BeforeEach
	public void before() throws Exception {
		super.before();
	}

	@Test
	public void test() {
		// we actually don't care about changing the links
		// but this is an easy way to create a bunch of links
		// for testing metrics
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState("""
			G1, AUTO, MATCH, P1
			G2, AUTO, MATCH, P2,
			G3, AUTO, POSSIBLE_MATCH, P3,
			G4, MANUAL, MATCH, P4
			G2, AUTO, NO_MATCH, P1
			G1, MANUAL, NO_MATCH, P2
			G1, MANUAL, POSSIBLE_MATCH, P3
		""");
		myLinkHelper.setup(state);

		// test
		MdmGenerateMetricParameters parameters = new MdmGenerateMetricParameters("Patient");
		MdmMetrics metrics = mySvc.generateMetrics(parameters);

		// verify
		assertNotNull(metrics);
	}
}
