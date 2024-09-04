package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4ReferentialIntegrityTest extends BaseJpaR4Test {

	@AfterEach
	public void afterResetConfig() {
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(new JpaStorageSettings().isEnforceReferentialIntegrityOnWrite());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(new JpaStorageSettings().isEnforceReferentialIntegrityOnDelete());
		myStorageSettings.setEnforceReferenceTargetTypes(new JpaStorageSettings().isEnforceReferenceTargetTypes());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider_withResourceType")
	public void referentialIntegrityOnWriteSetting_unknownIds_fullScopeTest(boolean theIsEnforceRefIntegrityEnabled,
					 JpaStorageSettings.ClientIdStrategyEnum theClientIdStrategy,
					 String theReferenceId) {
		// Given
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setEnforceReferenceTargetTypes(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setResourceClientIdStrategy(theClientIdStrategy);

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(theReferenceId));

		if (theIsEnforceRefIntegrityEnabled) {
			try {
				// When
				myPatientDao.create(p);
				fail();
			} catch (InvalidRequestException e) {
				// Then
				assertEquals(Msg.code(1094) + "Resource " + theReferenceId + " not found, specified in path: Patient.managingOrganization", e.getMessage());
			}
		} else {
			// Disabled ref integrity on write case: all POSTs should succeed
			// When
			IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
			// Then
			p = myPatientDao.read(id);
			assertEquals(theReferenceId, p.getManagingOrganization().getReference());
		}
	}

	private static Stream<Arguments> paramsProvider_withResourceType() {
		// theIsEnforceRefIntegrityEnabled, theClientIdStrategy, theReferenceId
		// note: client ID is tested since resolving the resource reference is different depending on the strategy
		return Stream.of(
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "Organization/123"),
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "Organization/A123"),
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ANY, "Organization/123"),
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ANY, "Organization/A123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "Organization/123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "Organization/A123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ANY, "Organization/123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ANY, "Organization/A123")
		);
	}

	@Test
	public void testRefIntegrityOnWrite_withReferenceIdOfAnotherResourceType() {
		// Given
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);

		// Create Observation with some ID...
		Observation obs = new Observation();
		IIdType obsId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		// And reference a non-existing Organization resource with the same ID as the Observation
		Patient p = new Patient();
		p.setManagingOrganization(new Reference(new IdType("Organization", obsId.getIdPart())));

		try {
			// When
			myPatientDao.create(p);
			fail();
		} catch (UnprocessableEntityException e) {
			// Then: identify that it is the wrong resource type, since ref integrity is enabled
			assertEquals(Msg.code(1095) + "Resource contains reference to unknown resource ID Organization/" + obsId.getIdPart(), e.getMessage());
		}

		// Given: now disable referential integrity on write
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(false);
		// When
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		// Then: this should now succeed
		p = myPatientDao.read(id);
		assertEquals("Organization/" + obsId.getIdPart(), p.getManagingOrganization().getReference());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider_noResourceType")
	public void testRefIntegrityOnWrite_withValidReferenceId_shouldAlwaysSucceed(boolean theIsEnforceRefIntegrityEnabled,
					  JpaStorageSettings.ClientIdStrategyEnum theClientIdStrategy,
					  String theReferenceId) {
		// Given
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setEnforceReferenceTargetTypes(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setResourceClientIdStrategy(theClientIdStrategy);

		Organization obs = new Organization();
		obs.setId(theReferenceId);
		myOrganizationDao.update(obs, mySrd);
		String qualifiedReferenceId = "Organization/" + theReferenceId;

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(qualifiedReferenceId));

		// When
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		// Then
		p = myPatientDao.read(id);
		assertEquals(qualifiedReferenceId, p.getManagingOrganization().getReference());
	}

	private static Stream<Arguments> paramsProvider_noResourceType() {
		// theIsEnforceRefIntegrityEnabled, theClientIdStrategy, theReferenceId
		// note: client ID is tested since resolving the resource reference is different depending on the strategy
		return Stream.of(
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "A123"),
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ANY, "123"),
			Arguments.of(true, JpaStorageSettings.ClientIdStrategyEnum.ANY, "A123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC, "A123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ANY, "123"),
			Arguments.of(false, JpaStorageSettings.ClientIdStrategyEnum.ANY, "A123")
		);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider_noResourceType")
	public void testRefIntegrityOnWrite_withReferenceIdOfDeletedResource(boolean theIsEnforceRefIntegrityEnabled,
					  JpaStorageSettings.ClientIdStrategyEnum theClientIdStrategy,
					  String theReferenceId) {
		// Given
		myStorageSettings.setEnforceReferentialIntegrityOnWrite(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setEnforceReferenceTargetTypes(theIsEnforceRefIntegrityEnabled);
		myStorageSettings.setResourceClientIdStrategy(theClientIdStrategy);

		Organization obs = new Organization();
		obs.setId(theReferenceId);
		IIdType obsId = myOrganizationDao.update(obs, mySrd).getId();
		String qualifiedReferenceId = "Organization/" + theReferenceId;

		myOrganizationDao.delete(obsId, mySrd);

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(qualifiedReferenceId));

		if (theIsEnforceRefIntegrityEnabled) {
			try {
				// When
				myPatientDao.create(p);
				fail();
			} catch (InvalidRequestException e) {
				// Then
				assertEquals(Msg.code(1096) + "Resource " + qualifiedReferenceId + " is deleted, specified in path: Patient.managingOrganization", e.getMessage());
			}
		} else {
			// Disabled ref integrity on write case: all POSTs should succeed
			// When
			IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

			// Then
			p = myPatientDao.read(id);
			assertEquals(qualifiedReferenceId, p.getManagingOrganization().getReference());
		}
	}

	@Test
	public void testDeleteFail() {
		Organization o = new Organization();
		o.setName("FOO");
		IIdType oid = myOrganizationDao.create(o).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(oid));
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		try {
			myOrganizationDao.delete(oid);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(515) + "Unable to delete Organization/" + oid.getIdPart() + " because at least one resource has a reference to this resource. First reference found was resource Patient/" + pid.getIdPart() + " in path Patient.managingOrganization", e.getMessage());
		}

		myPatientDao.delete(pid);
		myOrganizationDao.delete(oid);

	}

	@Test
	public void testDeleteAllow() {
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);

		Organization o = new Organization();
		o.setName("FOO");
		IIdType oid = myOrganizationDao.create(o).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(oid));
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myOrganizationDao.delete(oid);
		myPatientDao.delete(pid);

	}


}
