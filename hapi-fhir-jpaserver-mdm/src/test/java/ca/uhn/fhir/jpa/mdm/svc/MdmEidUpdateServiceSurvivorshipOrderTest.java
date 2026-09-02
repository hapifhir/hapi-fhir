package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A survivorship implementation is handed the Golden Resource so it can decide which values survive.
 * It must be handed the same state on update as on create, and
 * {@link MdmMatchLinkSvc#handleMdmCreate} merges the incoming EIDs into the Golden Resource before
 * applying survivorship rules. CDR's survivorship implementation runs a customer-authored script that
 * may read or write identifiers, so the difference is observable there even though HAPI's own
 * implementation excludes identifiers.
 */
// Created by claude-opus-5
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdmEidUpdateServiceSurvivorshipOrderTest {

	@Mock
	private IMdmResourceDaoSvc myMdmResourceDaoSvc;

	@Mock
	private IMdmLinkSvc myMdmLinkSvc;

	@Mock
	private MdmGoldenResourceFindingSvc myMdmGoldenResourceFindingSvc;

	@Mock
	private GoldenResourceHelper myGoldenResourceHelper;

	@Mock
	private EIDHelper myEIDHelper;

	@Mock
	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Mock
	private IMdmSettings myMdmSettings;

	@Mock
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	@InjectMocks
	private MdmEidUpdateService myEidUpdateService;

	private final Patient myTargetResource = new Patient();
	private final Patient myGoldenResource = new Patient();
	private MatchedGoldenResourceCandidate myCandidate;

	@BeforeEach
	public void before() {
		IResourcePersistentId<?> goldenPid = mock(IResourcePersistentId.class);
		myCandidate = new MatchedGoldenResourceCandidate(goldenPid, MdmMatchOutcome.EID_MATCH);

		when(myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(
						eq(myCandidate), any()))
				.thenReturn(myGoldenResource);

		// The incoming resource carries an EID that the Golden Resource already has, which is the branch
		// that merges EIDs into the Golden Resource.
		when(myEIDHelper.hasEidOverlap(myGoldenResource, myTargetResource)).thenReturn(true);
		when(myEIDHelper.getExternalEid(myTargetResource)).thenReturn(Collections.emptyList());

		// The resource stays matched to the same Golden Resource.
		IMdmLink existingLink = mock(IMdmLink.class);
		when(existingLink.getGoldenResourcePersistenceId()).thenReturn(goldenPid);
		when(myMdmLinkDaoSvc.getMatchedOrPossibleMatchedLinkForSource(myTargetResource))
				.thenReturn(Optional.of(existingLink));
		when(myMdmResourceDaoSvc.readGoldenResourceByPid(eq(goldenPid), eq("Patient")))
				.thenReturn(myGoldenResource);
	}

	@Test
	public void handleMdmUpdate_matchedToTheSameGoldenResource_mergesEidsBeforeApplyingSurvivorshipRules() {
		myEidUpdateService.handleMdmUpdate(myTargetResource, myCandidate, newContext());

		InOrder inOrder = inOrder(myGoldenResourceHelper, myMdmSurvivorshipService);
		inOrder.verify(myGoldenResourceHelper).handleExternalEidAddition(eq(myGoldenResource), eq(myTargetResource), any());
		inOrder.verify(myMdmSurvivorshipService)
				.applySurvivorshipRulesToGoldenResource(eq(myTargetResource), eq(myGoldenResource), any());
	}

	private MdmTransactionContext newContext() {
		MdmTransactionContext context = new MdmTransactionContext(
				TransactionLogMessages.createNew(), MdmTransactionContext.OperationType.UPDATE_RESOURCE);
		context.setResourceType("Patient");
		return context;
	}
}
