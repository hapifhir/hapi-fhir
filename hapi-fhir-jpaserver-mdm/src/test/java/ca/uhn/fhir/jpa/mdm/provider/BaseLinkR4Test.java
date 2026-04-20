package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseLinkR4Test extends BaseMdmProviderR4Test {
	protected static final StringType NO_MATCH_RESULT = new StringType(MdmMatchResultEnum.NO_MATCH.name());
	protected static final StringType MATCH_RESULT = new StringType(MdmMatchResultEnum.MATCH.name());
	protected static final StringType POSSIBLE_MATCH_RESULT = new StringType(MdmMatchResultEnum.POSSIBLE_MATCH.name());
	protected static final StringType POSSIBLE_DUPLICATE_RESULT = new StringType(MdmMatchResultEnum.POSSIBLE_DUPLICATE.name());

	protected Patient myPatient;
	protected IAnyResource mySourcePatient;
	protected MdmLink myLink;
	protected StringType myPatientId;
	protected StringType mySourcePatientId;
	protected StringType myVersionlessGodlenResourceId;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPatient = createPatientAndUpdateLinks(buildPaulPatient());
		myPatientId = new StringType(myPatient.getIdElement().getValue());

		mySourcePatient = getGoldenResourceFromTargetResource(myPatient);
		mySourcePatientId = new StringType(mySourcePatient.getIdElement().getValue());
		myVersionlessGodlenResourceId = new StringType(mySourcePatient.getIdElement().toVersionless().getValue());

		myLink = (MdmLink) getOnlyPatientLink();
		// Tests require our initial link to be a POSSIBLE_MATCH
		myLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		saveLink(myLink);
		assertEquals(MdmLinkSourceEnum.AUTO, myLink.getLinkSource());
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setDeleteExpungeEnabled(true);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		super.after();
	}

	@Nonnull
	protected IMdmLink getOnlyPatientLink() {
		return myMdmLinkDaoSvc.findMdmLinkBySource(myPatient).get();
	}

	@Nonnull
	protected List<? extends IMdmLink> getPatientLinks() {
		return myMdmLinkDaoSvc.findMdmLinksBySourceResource(myPatient);
	}

	protected List<MdmLinkWithRevision<MdmLink>> getHistoricalLinks(List<String> theGoldenResourceIds, List<String> theResourceIds) {
		MdmHistorySearchParameters historySearchParameters = new MdmHistorySearchParameters()
			.setGoldenResourceIds(theGoldenResourceIds)
			.setSourceIds(theResourceIds);

		return myMdmLinkDaoSvc.findMdmLinkHistory(historySearchParameters);
	}
}
