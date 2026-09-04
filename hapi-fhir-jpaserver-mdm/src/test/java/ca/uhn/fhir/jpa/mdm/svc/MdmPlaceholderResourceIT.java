package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmPlaceholderResourceIT extends BaseMdmR4Test {

	@Language("JSON")
	private static final String MDM_RULES_JSON = """
		{
			"version": "1",
			"mdmTypes": ["Patient", "Practitioner", "Medication"],
			"candidateSearchParams": [
				{
					"resourceType": "*",
					"searchParams": [
						"identifier"
					]
				}
			],
			"candidateFilterSearchParams": [
			],
			"matchFields": [
				{
		  			"name": "medicare-id",
		  			"resourceType": "Patient",
		  			"resourcePath": "identifier",
		  			"matcher": {
		  				"algorithm": "IDENTIFIER",
		  				"identifierSystem": "http://hl7.org/fhir/sid/us-medicare"
		  			}
		  		}
			],
			"matchResultMap": {
				"medicare-id": "MATCH"
			},
			"eidSystem": "http://company.io/fhir/NamingSystem/custom-eid-system"
		}
		""";

	private static final String IDENTIFIER_SYSTEM = "http://hl7.org/fhir/sid/us-medicare";

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Autowired
	private IMdmMatchFinderSvc mySvc;

	@Autowired
	private IFhirResourceDao<Patient> myPatientDao;

	private MdmRulesJson myExistingRules;

	@BeforeEach
	public void before() throws Exception {
		super.before();
		myExistingRules = myMdmSettings.getMdmRules();

		MdmRulesJson rules = JsonUtil.deserialize(MDM_RULES_JSON, MdmRulesJson.class);

		myMdmSettings.setMdmRules(rules);
		myMdmSettings.setIgnorePlaceholderResources(true);
	}

	@AfterEach
	public void after() throws IOException {
		super.after();

		myMdmSettings.setMdmRules(myExistingRules);
		myMdmSettings.setIgnorePlaceholderResources(false);
	}

	// placeholder as source
	@Test
	public void getMatchedTargets_suppliedPlaceholder_returnsNothing() throws InterruptedException {
		// setup
		// create a placeholder resource
		Patient source = createPlaceholderPatient();
		source.addIdentifier()
			.setSystem(IDENTIFIER_SYSTEM)
			.setValue("123");

		// add some resources to the db
		for (String name : new String[] { "john", "jane" }) {
			Patient patient = new Patient();
			patient.addName()
				.addGiven(name)
				.setFamily("doe");
			patient.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue("123");
			myMdmHelper.createWithLatch(patient);
		}

		// test
		List<MatchedTarget> results = mySvc.getMatchedTargets("Patient", source, RequestPartitionId.allPartitions());

		// validate
		assertTrue(results.isEmpty());
	}

	@Test
	public void getMatchedTargets_realResourceEidMatching_doesNotIgnorePlaceholders() throws InterruptedException {
		// setup
		Patient placeholder = createPlaceholderPatient();
		addExternalEID(placeholder, "abc");

		myMdmHelper.createWithLatch(placeholder);

		Patient source = new Patient();
		source.addIdentifier()
			.setSystem(IDENTIFIER_SYSTEM)
			.setValue("123");
		source.addName()
			.setFamily("simpson")
			.addGiven("homer");
		addExternalEID(source, "abc");

		// test
		List<MatchedTarget> results = mySvc.getMatchedTargets("Patient", source, RequestPartitionId.allPartitions());

		// validate
		assertEquals(2, results.size());
		assertTrue(results.stream()
			.anyMatch(target -> target.getTarget().getIdElement().getIdPartAsLong().equals(placeholder.getIdElement().getIdPartAsLong())));
	}

	// placeholder as candidate
	@Test
	public void getMatchedTargets_realResource_ignoresPlaceholdersInDb() throws InterruptedException {
		// setup
		IIdType placeholderId;
		{
			Patient placeholder = createPlaceholderPatient();

				placeholder.addIdentifier()
					.setSystem(IDENTIFIER_SYSTEM)
					.setValue("123");

			// shouldn't be matched, so we won't wait on a latch
			DaoMethodOutcome outcome = myPatientDao.create(placeholder, new SystemRequestDetails());
			placeholderId = outcome.getId();

			Patient nonplaceholder = new Patient();
			nonplaceholder.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue("123");
			myMdmHelper.createWithLatch(nonplaceholder);
		}

		Patient source = new Patient();
		source.addIdentifier()
			.setSystem(IDENTIFIER_SYSTEM)
			.setValue("123");
		source.addName()
			.setFamily("simpson")
			.addGiven("homer");

		// test
		List<MatchedTarget> results = mySvc.getMatchedTargets("Patient", source, RequestPartitionId.allPartitions());

		// validate
		assertEquals(1, results.size());
		assertFalse(results.stream()
			.anyMatch(target -> target.getTarget().getIdElement().getIdPartAsLong().equals(placeholderId.getIdPartAsLong())));
	}

	@Test
	public void getMatchedTargets_realEIDPlaceholder_matches() throws InterruptedException {
		// setup
		Patient real = new Patient();
		real.addName().setFamily("Simpson")
			.addGiven("Homer");
		addExternalEID(real, "abc");
		myMdmHelper.createWithLatch(real);
		assertLinkCount(1);

		// create the placeholder with eid
		Patient placeholder = createPlaceholderPatient();
		addExternalEID(placeholder, "abc");

		MdmHelperR4.OutcomeAndLogMessageWrapper outcome = myMdmHelper.createWithLatch(placeholder);
		IIdType placeholderId = outcome.getDaoMethodOutcome()
			.getId();


		Patient candidate = new Patient();
		candidate.addName()
			.setFamily("Simpson")
			.addGiven("jay");
			addExternalEID(candidate, "abc");


		// test
		myMdmHelper.createWithLatch(candidate);

		// verify
		assertLinkCount(3);

		runInTransaction(() -> {
			List<MdmLink> allLinks = myMdmLinkDao.findAll();
			assertEquals(3, allLinks.size());
			assertTrue(
				allLinks.stream()
					.anyMatch(link -> {
						return Objects.equals(placeholderId.getIdPartAsLong(), link.getSource().getId().getId());
					})
			);
		});
	}

	@Test
	public void getMatchedTargets_oneRealOnePlaceholder_matchesRealOnly() throws InterruptedException {
		// setup
		Patient real = new Patient();
		real.addName().setFamily("Simpson")
			.addGiven("Homer");

		real.addIdentifier()
			.setSystem(IDENTIFIER_SYSTEM)
			.setValue("123");
		myMdmHelper.createWithLatch(real);
		assertLinkCount(1);

		// create our placeholder
		IIdType placeholderId;
		{
			Patient placeholder = createPlaceholderPatient();

			placeholder.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue("123");

			// shouldn't fire the latch since it shouldn't match
			DaoMethodOutcome outcome = myPatientDao.create(placeholder, new SystemRequestDetails());
			placeholderId = outcome.getId();
		}

		Patient candidate = new Patient();
		candidate.addName()
			.setFamily("Simpson")
			.addGiven("jay");

		candidate.addIdentifier()
			.setSystem(IDENTIFIER_SYSTEM)
			.setValue("123");

		// test
		myMdmHelper.createWithLatch(candidate);

		// verify
		assertLinkCount(2);

		runInTransaction(() -> {
			List<MdmLink> allLinks = myMdmLinkDao.findAll();
			assertEquals(2, allLinks.size());
			for (MdmLink link : allLinks) {
				// none of the links should be to the placeholder
				assertNotEquals(placeholderId.getIdPartAsLong(), link.getSource().getId().getId());
			}
		});
	}

	@ParameterizedTest
	@ValueSource(booleans = {
		true,
		false
	})
	public void getMatchedTargets_withNoIgnorePlaceholder_matchesPlaceholderToo(boolean theIsEid) throws InterruptedException {
		// setup
		myMdmSettings.setIgnorePlaceholderResources(false);

		Patient real = new Patient();
		real.addName().setFamily("Simpson")
			.addGiven("Homer");
		if (theIsEid) {
			addExternalEID(real, "abc");
		} else {
			real.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue("123");
		}
		myMdmHelper.createWithLatch(real);
		assertLinkCount(1);

		// create our placeholder
		IIdType placeholderId;
		{
			Patient placeholder = createPlaceholderPatient();
			if (theIsEid) {
				addExternalEID(placeholder, "abc");
			} else {
				placeholder.addIdentifier()
					.setSystem(IDENTIFIER_SYSTEM)
					.setValue("123");
			}


			MdmHelperR4.OutcomeAndLogMessageWrapper outcome = myMdmHelper.createWithLatch(placeholder);
			placeholderId = outcome.getDaoMethodOutcome().getId();
		}

		Patient candidate = new Patient();
		candidate.addName()
			.setFamily("Simpson")
			.addGiven("jay");
		if (theIsEid) {
			addExternalEID(candidate, "abc");
		} else {
			candidate.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue("123");
		}

		// test
		myMdmHelper.createWithLatch(candidate);

		// verify
		assertLinkCount(3);

		runInTransaction(() -> {
			List<MdmLink> allLinks = myMdmLinkDao.findAll();
			// one of these links should be to the placeholder
			assertTrue(allLinks
				.stream()
				.anyMatch(link -> {
					return Objects.equals(link.getSource().getId().getId(), placeholderId.getIdPartAsLong());
				}));
		});
	}

	@Test
	public void updateResource_placeholderResource_doNotCreateGoldenResourcesUntilFilledIn() throws InterruptedException {
		// setup
		Patient placeholder = createPlaceholderPatient();
		placeholder.addIdentifier().setSystem(IDENTIFIER_SYSTEM).setValue("123");
		IIdType id = myPatientDao.create(placeholder, new SystemRequestDetails())
			.getId().toUnqualifiedVersionless();

		assertLinkCount(0);

		Patient filledIn = new Patient();
		filledIn.setId(id);
		filledIn.addIdentifier().setSystem(IDENTIFIER_SYSTEM).setValue("123");
		filledIn.addName().setFamily("simpson").addGiven("homer");

		myMdmHelper.updateWithLatch(filledIn);

		assertLinkCount(1);
		runInTransaction(() -> {
			List<MdmLink> allLinks = myMdmLinkDao.findAll();
			// one of these links should be to the placeholder
			assertTrue(allLinks
				.stream()
				.anyMatch(link -> {
					return Objects.equals(link.getSource().getId().getId(), id.getIdPartAsLong());
				}));
		});
	}

	private Patient createPlaceholderPatient() {
		Patient placeholder = new Patient();
		ExtensionUtil.addExtension(myFhirContext,
			placeholder, EXT_RESOURCE_PLACEHOLDER,
			"boolean", "true");
		return placeholder;
	}
}
