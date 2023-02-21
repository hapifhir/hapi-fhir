package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
@SuppressWarnings({"Duplicates"})
public class UpliftedRefchainsR5Test extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpliftedRefchainsR5Test.class);

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();
		myStorageSettings.setIndexOnUpliftedRefchains(new StorageSettings().isIndexOnUpliftedRefchains());
	}

	@Test
	public void testCreate() {
		// Setup

		myStorageSettings.setIndexOnUpliftedRefchains(true);

		RuntimeSearchParam subjectSp = mySearchParamRegistry.getRuntimeSearchParam("Encounter", "subject");
		SearchParameter sp = new SearchParameter();
		Extension upliftRefChain = sp.addExtension().setUrl(JpaConstants.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(JpaConstants.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType("name"));

		sp.setId(subjectSp.getId());
		sp.setCode(subjectSp.getName());
		sp.setName(subjectSp.getName());
		sp.setUrl(subjectSp.getUri());
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression(subjectSp.getPath());
		subjectSp.getBase().forEach(sp::addBase);
		subjectSp.getTargets().forEach(sp::addTarget);
		mySearchParameterDao.update(sp, mySrd);

		Practitioner practitioner = new Practitioner();
		practitioner.addName().setFamily("Gumble").addGiven("Barney");
		IIdType practitionerId = myPractitionerDao.create(practitioner, mySrd).getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addName().setFamily("Simpson").addGiven("Homer");
		IIdType p1Id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		Encounter e1 = new Encounter();
		e1.setSubject(new Reference(p1Id));
		e1.addParticipant().setActor(new Reference(practitionerId));
		myEncounterDao.update(e1, mySrd);

		Patient p2 = new Patient();
		p2.addName().setFamily("Simpson").addGiven("Marge");
		IIdType p2Id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		// Test

		Encounter e2 = new Encounter();
		e2.setId("Encounter/E2");
		e2.setSubject(new Reference(p2Id));
		e2.addParticipant().setActor(new Reference(practitionerId));
		myEncounterDao.update(e2, mySrd);

		// Verify
	}


}
