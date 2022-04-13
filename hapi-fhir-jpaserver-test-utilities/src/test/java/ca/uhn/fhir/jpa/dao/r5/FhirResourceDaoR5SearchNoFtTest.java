package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = TestHibernateSearchAddInConfig.NoFT.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchNoFtTest extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5SearchNoFtTest.class);

	@Test
	public void testHasWithTargetReference() {
		Organization org = new Organization();
		org.setId("ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("PRACT");
		practitioner.addName().setFamily("PRACT");
		myPractitionerDao.update(practitioner);

		PractitionerRole role = new PractitionerRole();
		role.setId("ROLE");
		role.getPractitioner().setReference("Practitioner/PRACT");
		role.getOrganization().setReference("Organization/ORG");
		myPractitionerRoleDao.update(role);

		SearchParameterMap params = new SearchParameterMap();
		HasAndListParam value = new HasAndListParam();
		value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "organization", "ORG")));
		params.add("_has", value);
		IBundleProvider outcome = myPractitionerDao.search(params);
		assertEquals(1, outcome.getResources(0, 1).size());
	}

	@Test
	public void testHasWithTargetReferenceQualified() {
		Organization org = new Organization();
		org.setId("ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("PRACT");
		practitioner.addName().setFamily("PRACT");
		myPractitionerDao.update(practitioner);

		PractitionerRole role = new PractitionerRole();
		role.setId("ROLE");
		role.getPractitioner().setReference("Practitioner/PRACT");
		role.getOrganization().setReference("Organization/ORG");
		myPractitionerRoleDao.update(role);

		SearchParameterMap params = new SearchParameterMap();
		HasAndListParam value = new HasAndListParam();
		value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "organization", "Organization/ORG")));
		params.add("_has", value);
		IBundleProvider outcome = myPractitionerDao.search(params);
		assertEquals(1, outcome.getResources(0, 1).size());
	}

	@Test
	public void testHasWithTargetId() {
		Organization org = new Organization();
		org.setId("ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("PRACT");
		practitioner.addName().setFamily("PRACT");
		myPractitionerDao.update(practitioner);

		PractitionerRole role = new PractitionerRole();
		role.setId("ROLE");
		role.getPractitioner().setReference("Practitioner/PRACT");
		role.getOrganization().setReference("Organization/ORG");
		myPractitionerRoleDao.update(role);

		runInTransaction(() -> {
			ourLog.info("Links:\n * {}", myResourceLinkDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		HasAndListParam value = new HasAndListParam();
		value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "_id", "ROLE")));
		params.add("_has", value);
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPractitionerDao.search(params);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(1);
		assertEquals(1, outcome.getResources(0, 1).size());
	}

	@Test
	public void testSearchDoesntFailIfResourcesAreDeleted() {

		Patient p = new Patient();
		p.addIdentifier().setValue("1");
		myPatientDao.create(p);

		p = new Patient();
		p.addIdentifier().setValue("2");
		myPatientDao.create(p);

		p = new Patient();
		p.addIdentifier().setValue("3");
		Long id = myPatientDao.create(p).getId().getIdPartAsLong();

		IBundleProvider outcome = myPatientDao.search(new SearchParameterMap());
		assertEquals(3, outcome.size().intValue());

		runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			table.setDeleted(new Date());
			myResourceTableDao.save(table);
		});

		assertEquals(2, outcome.getResources(0, 3).size());

		runInTransaction(() -> {
			myResourceHistoryTableDao.deleteAll();
		});

		assertEquals(0, outcome.getResources(0, 3).size());
	}

}
