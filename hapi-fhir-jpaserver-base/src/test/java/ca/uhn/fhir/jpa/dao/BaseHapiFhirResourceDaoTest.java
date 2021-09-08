package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BaseHapiFhirResourceDaoTest {
	TestResourceDao mySvc = new TestResourceDao();

	@Test
	public void validateResourceIdCreation_asSystem() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new SystemRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	@Test
	public void validateResourceIdCreation_asUser() {
		Patient patient = new Patient();
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("failedToCreateWithClientAssignedIdNotAllowed", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withNumber() {
		Patient patient = new Patient();
		patient.setId("2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ALPHANUMERIC);
		try {
			mySvc.validateResourceIdCreation(patient, sysRequest);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("failedToCreateWithClientAssignedNumericId", e.getMessage());
		}
	}

	@Test
	public void validateResourceIdCreationAlpha_withAlpha() {
		Patient patient = new Patient();
		patient.setId("P2401");
		RequestDetails sysRequest = new ServletRequestDetails();
		mySvc.getConfig().setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ALPHANUMERIC);
		mySvc.validateResourceIdCreation(patient, sysRequest);
		// no exception is thrown
	}

	static class TestResourceDao extends BaseHapiFhirResourceDao<Patient> {
		private final DaoConfig myDaoConfig = new DaoConfig();

		@Override
		public DaoConfig getConfig() {
			return myDaoConfig;
		}

		@Override
		protected String getMessageSanitized(String theKey, String theIdPart) {
			return theKey;
		}
	}
}
