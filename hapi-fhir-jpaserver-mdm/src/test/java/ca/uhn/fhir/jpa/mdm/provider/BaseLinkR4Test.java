package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseLinkR4Test extends BaseProviderR4Test {
	protected static final StringType NO_MATCH_RESULT = new StringType(MdmMatchResultEnum.NO_MATCH.name());
	protected static final StringType MATCH_RESULT = new StringType(MdmMatchResultEnum.MATCH.name());
	protected static final StringType POSSIBLE_MATCH_RESULT = new StringType(MdmMatchResultEnum.POSSIBLE_MATCH.name());
	protected static final StringType POSSIBLE_DUPLICATE_RESULT = new StringType(MdmMatchResultEnum.POSSIBLE_DUPLICATE.name());

	@Autowired
	DaoConfig myDaoConfig;

	protected Patient myPatient;
	protected IAnyResource mySourcePatient;
	protected MdmLink myLink;
	protected StringType myPatientId;
	protected StringType mySourcePatientId;
	protected StringType myVersionlessGodlenResourceId;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myPatient = createPatientAndUpdateLinks(buildPaulPatient());
		myPatientId = new StringType(myPatient.getIdElement().getValue());

		mySourcePatient = getGoldenResourceFromTargetResource(myPatient);
		mySourcePatientId = new StringType(mySourcePatient.getIdElement().getValue());
		myVersionlessGodlenResourceId = new StringType(mySourcePatient.getIdElement().toVersionless().getValue());

		myLink = getOnlyPatientLink();
		// Tests require our initial link to be a POSSIBLE_MATCH
		myLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		saveLink(myLink);
		assertEquals(MdmLinkSourceEnum.AUTO, myLink.getLinkSource());
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		super.after();
	}

	@Nonnull
	protected MdmLink getOnlyPatientLink() {
		return myMdmLinkDaoSvc.findMdmLinkBySource(myPatient).get();
	}

	@Nonnull
	protected List<MdmLink> getPatientLinks() {
		return myMdmLinkDaoSvc.findMdmLinksBySourceResource(myPatient);
	}
}
