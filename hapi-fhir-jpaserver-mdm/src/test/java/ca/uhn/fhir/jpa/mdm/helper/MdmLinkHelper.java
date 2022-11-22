package ca.uhn.fhir.jpa.mdm.helper;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMLinkResults;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
public class MdmLinkHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkHelper.class);

	@Autowired
   private IMdmLinkJpaRepository myMdmLinkRepo;
	@Autowired
	private IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@SuppressWarnings("rawtypes")
	@Autowired
	private IMdmLinkDao myMdmLinkDao;

	@Transactional
	public void logMdmLinks() {
		List<MdmLink> links = myMdmLinkRepo.findAll();
		ourLog.info("All MDM Links:");
		for (MdmLink link : links) {
			IdDt goldenResourceId = link.getGoldenResource().getIdDt().toVersionless();
			IdDt targetId = link.getSource().getIdDt().toVersionless();
			ourLog.info("{}: {}, {}, {}, {}", link.getId(), goldenResourceId, targetId, link.getMatchResult(), link.getLinkSource());
		}
	}

	private MdmTransactionContext createContextForCreate(String theResourceType) {
		MdmTransactionContext ctx = new MdmTransactionContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.CREATE_RESOURCE);
		ctx.setResourceType(theResourceType);
		ctx.setTransactionLogMessages(null);
		return ctx;
	}

	/**
	 * Creates all the initial links specified in the state object.
	 *
	 * These links will be returned in an MDMLinkResults object, in case
	 * they are needed.
	 */
	public MDMLinkResults setup(MDMState<Patient> theState) {
		MDMLinkResults results = new MDMLinkResults();

		String[] inputs = theState.getParsedInputState();

		// create all patients if needed
		for (String inputState : inputs) {
			String[] params = MDMState.parseState(inputState);
			createIfNeeded(theState, params[0]);
			createIfNeeded(theState, params[3]);
		}

		// create all the links
		for (String inputState : theState.getParsedInputState()) {
			ourLog.info(inputState);
			String[] params = MDMState.parseState(inputState);

			Patient goldenResource = theState.getParameter(params[0]);
			Patient targetResource = theState.getParameter(params[3]);

			MdmLinkSourceEnum matchSourceType = MdmLinkSourceEnum.valueOf(params[1]);
			MdmMatchResultEnum matchResultType = MdmMatchResultEnum.valueOf(params[2]);

			MdmMatchOutcome matchOutcome = new MdmMatchOutcome(
				null,
				null
			);
			matchOutcome.setMatchResultEnum(matchResultType);

			MdmLink link = (MdmLink) myMdmLinkDaoSvc.createOrUpdateLinkEntity(
				goldenResource,
				targetResource,
				matchOutcome,
				matchSourceType,
				createContextForCreate("Patient")
			);

			results.addResult(link);
		}

		return results;
	}

	private Patient createIfNeeded(MDMState<Patient> theState, String thePatientId) {
		Patient patient = theState.getParameter(thePatientId);
		if (patient == null) {
			// if it doesn't exist, create it
			patient = createPatientAndTags(thePatientId);
			theState.addParameter(thePatientId, patient);
		}
		return patient;
	}

	private Patient createPatientAndTags(String theId) {
		Patient patient = new Patient();
		patient.setActive(true); // all mdm patients must be active

		// TODO - update the create to an update
		// currently, IdHelperService.resolveResourcePersistentIds
		// expects either a regular PID or have a partition id :(
		patient.addIdentifier(new Identifier().setValue(theId));

		// Golden patients will be "PG#"
		if (theId.length() >= 2 && theId.charAt(1) == 'G') {
			// golden resource
			MdmResourceUtil.setGoldenResource(patient);
		}
		MdmResourceUtil.setMdmManaged(patient);

		DaoMethodOutcome outcome = myPatientDao.create(patient);
		Patient outputPatient = (Patient) outcome.getResource();
		return outputPatient;
	}

	public void validateResults(MDMState<Patient> theState) {
		String[] outputStates = theState.getParsedOutputState();

		// for every parameter, we'll get all links
		for (Map.Entry<String, Patient> entrySet : theState.getParameterToValue().entrySet()) {
			Patient patient = entrySet.getValue();
			List<MdmLink> links = getAllMdmLinks(patient);
			theState.addLinksForResource(patient, links.toArray(new MdmLink[0]));
		}

		int totalExpectedLinks = outputStates.length;
		int totalActualLinks = 0;
		for (Patient p : theState.getActualOutcomeLinks().keys()) {
			totalActualLinks += theState.getActualOutcomeLinks().get(p).size();
		}
		assertEquals(totalExpectedLinks, totalActualLinks,
			String.format("Invalid number of links. Expected %d, Actual %d",
				totalExpectedLinks, totalActualLinks)
		);

		for (String state : outputStates) {
			ourLog.info(state);
			String[] params = MDMState.parseState(state);

			Patient leftSideResource = theState.getParameter(params[0]);
			Collection<MdmLink> links = theState.getActualOutcomeLinks().get(leftSideResource);
			assertFalse(links.isEmpty(), state);

			MdmLinkSourceEnum matchSourceType = MdmLinkSourceEnum.valueOf(params[1]);
			MdmMatchResultEnum matchResultType = MdmMatchResultEnum.valueOf(params[2]);

			Patient rightSideResource = theState.getParameter(params[3]);

			boolean foundLink = false;
			for (MdmLink link : links) {
				if (link.getGoldenResourcePid().longValue() == leftSideResource.getIdElement().getIdPartAsLong().longValue()
					&& link.getSourcePid().longValue() == rightSideResource.getIdElement().getIdPartAsLong().longValue()
					&& link.getMatchResult() == matchResultType
					&& link.getLinkSource() == matchSourceType
				) {
					foundLink = true;
					break;
				}
			}

			assertTrue(foundLink, String.format("State: %s - not found", state));
		}
	}

	public List<MdmLink> getAllMdmLinks(Patient theGoldenPatient) {
		return myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theGoldenPatient).stream()
			.map( link -> (MdmLink) link)
			.collect(Collectors.toList());
	}

}
