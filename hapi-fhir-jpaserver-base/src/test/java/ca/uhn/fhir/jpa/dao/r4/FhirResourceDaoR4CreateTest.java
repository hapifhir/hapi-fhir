package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

public class FhirResourceDaoR4CreateTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CreateTest.class);

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());
	}

	@Test
	public void testCreateWithUuidResourceStrategy() throws Exception {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		assertThat(id.getIdPart(), matchesPattern("[a-z0-9]{8}-.*"));

		p = myPatientDao.read(id);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

	}

	@Test
	public void testTransactionCreateWithUuidResourceStrategy() throws Exception {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);

		Organization org = new Organization();
		org.setId(IdType.newRandomUuid());
		org.setName("ORG");

		Patient p = new Patient();
		p.setId(IdType.newRandomUuid());
		p.addName().setFamily("FAM");
		p.getManagingOrganization().setReference(org.getId());

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(org)
			.setFullUrl(org.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);
		input.addEntry()
			.setResource(p)
			.setFullUrl(p.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		Bundle output = mySystemDao.transaction(mySrd, input);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry().get(0).getResponse().getLocation(), matchesPattern("Organization/[a-z0-9]{8}-.*"));
		assertThat(output.getEntry().get(1).getResponse().getLocation(), matchesPattern("Patient/[a-z0-9]{8}-.*"));


	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
