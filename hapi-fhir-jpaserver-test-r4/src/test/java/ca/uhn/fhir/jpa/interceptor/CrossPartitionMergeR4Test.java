package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-6
@Import(CrossPartitionMergeR4Test.TestConfig.class)
public class CrossPartitionMergeR4Test extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionMergeR4Test.class);
	private static final int ALTERNATE_DEFAULT_ID = -1;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private HapiTransactionService myTransactionService;

	private PatientIdPartitionInterceptor myPartitionInterceptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionInterceptor = new PatientIdPartitionInterceptor(
			getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);
		myPartitionSettings.setAllowReferencesAcrossPartitions(
			PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);

		PartitionSettings defaultSettings = new PartitionSettings();
		myPartitionSettings.setPartitioningEnabled(defaultSettings.isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(defaultSettings.isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(defaultSettings.getDefaultPartitionId());
		myPartitionSettings.setAllowReferencesAcrossPartitions(
			defaultSettings.getAllowReferencesAcrossPartitions());

		myTransactionService.setTransactionPropagationWhenChangingPartitions(
			HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
	}

	@Test
	void testCrossPartitionMerge_compartmentResourcesMovedAndReferencesUpdated() {
		SystemRequestDetails allPartsReq =
			SystemRequestDetails.forRequestPartitionId(RequestPartitionId.allPartitions());

		// Setup: Create patients in different partitions
		Patient patientA = new Patient();
		patientA.setId("Patient/A");
		patientA.setActive(true);
		myPatientDao.update(patientA, mySrd);

		Patient patientB = new Patient();
		patientB.setId("Patient/B");
		patientB.setActive(true);
		myPatientDao.update(patientB, mySrd);

		// Create Organization in default partition
		Organization org = new Organization();
		org.setName("Test Org");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		// Create Encounter in Patient/A's compartment
		Encounter encounter = new Encounter();
		encounter.getSubject().setReference("Patient/A");
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);
		IIdType encounterId = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

		// Create Observation referencing Patient/A, the Encounter, and Organization
		Observation obs1 = new Observation();
		obs1.getSubject().setReference("Patient/A");
		obs1.getEncounter().setReference(encounterId.getValue());
		obs1.addPerformer().setReference(orgId.getValue());
		obs1.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		// Create another Observation referencing Patient/A only
		Observation obs2 = new Observation();
		obs2.getSubject().setReference("Patient/A");
		obs2.getCode().addCoding().setSystem("http://loinc.org").setCode("67890");
		myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		// Create Group in default partition referencing Patient/A
		Group group = new Group();
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);
		group.addMember().getEntity().setReference("Patient/A");
		IIdType groupId = myGroupDao.create(group, mySrd).getId().toUnqualifiedVersionless();

		// Execute: merge Patient/A into Patient/B
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("source-patient").setValue(new Reference("Patient/A"));
		inParams.addParameter().setName("target-patient").setValue(new Reference("Patient/B"));
		inParams.addParameter().setName("delete-source").setValue(new BooleanType(false));

		myClient.operation()
			.onType("Patient")
			.named("merge")
			.withParameters(inParams)
			.returnResourceType(Parameters.class)
			.execute();

		// Verify: Compartment resources now reference Patient/B
		SearchParameterMap obsSearch = SearchParameterMap.newSynchronous();
		obsSearch.add(Observation.SP_SUBJECT, new ReferenceParam("Patient/B"));
		List<IBaseResource> newObservations = myObservationDao.search(obsSearch, allPartsReq).getAllResources();
		assertThat(newObservations).hasSize(2);

		SearchParameterMap encSearch = SearchParameterMap.newSynchronous();
		encSearch.add(Encounter.SP_SUBJECT, new ReferenceParam("Patient/B"));
		List<IBaseResource> newEncounters = myEncounterDao.search(encSearch, allPartsReq).getAllResources();
		assertThat(newEncounters).hasSize(1);

		// Verify: Intra-compartment cross-references updated (Observation→Encounter)
		Encounter newEncounter = (Encounter) newEncounters.get(0);
		IIdType newEncounterId = newEncounter.getIdElement().toUnqualifiedVersionless();

		Observation obsWithEncounter = newObservations.stream()
			.map(r -> (Observation) r)
			.filter(Observation::hasEncounter)
			.findFirst()
			.orElseThrow(() -> new AssertionError("Expected an Observation with encounter reference"));
		assertThat(obsWithEncounter.getEncounter().getReferenceElement().toUnqualifiedVersionless().getValue())
			.isEqualTo(newEncounterId.getValue());

		// Verify: Reference to Organization (default partition) preserved
		assertThat(obsWithEncounter.getPerformerFirstRep().getReferenceElement().toUnqualifiedVersionless().getValue())
			.isEqualTo(orgId.getValue());

		// Verify: Patient references replaced (source→target)
		assertThat(obsWithEncounter.getSubject().getReference()).isEqualTo("Patient/B");
		assertThat(newEncounter.getSubject().getReference()).isEqualTo("Patient/B");

		// Verify: Non-compartment resource (Group) updated in place with new patient reference
		Group updatedGroup = myGroupDao.read(groupId, allPartsReq);
		assertThat(updatedGroup.getMemberFirstRep().getEntity().getReference()).isEqualTo("Patient/B");

		// Verify: Organization unchanged
		Organization updatedOrg = myOrganizationDao.read(orgId, allPartsReq);
		assertThat(updatedOrg.getName()).isEqualTo("Test Org");

		// Verify: No compartment resources left referencing Patient/A
		SearchParameterMap oldObsSearch = SearchParameterMap.newSynchronous();
		oldObsSearch.add(Observation.SP_SUBJECT, new ReferenceParam("Patient/A"));
		List<IBaseResource> oldObservations = myObservationDao.search(oldObsSearch, allPartsReq).getAllResources();
		assertThat(oldObservations).isEmpty();

		SearchParameterMap oldEncSearch = SearchParameterMap.newSynchronous();
		oldEncSearch.add(Encounter.SP_SUBJECT, new ReferenceParam("Patient/A"));
		List<IBaseResource> oldEncounters = myEncounterDao.search(oldEncSearch, allPartsReq).getAllResources();
		assertThat(oldEncounters).isEmpty();
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public IMdmSettings mdmSettings() {
			MdmSettings settings = new MdmSettings(null);
			settings.setSearchAllPartitionForMatch(true);
			return settings;
		}
	}
}
