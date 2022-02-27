package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkEvent;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.backend.lucene.cfg.LuceneIndexSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

@TestPropertySource(properties = {
	"mdm.prevent_multiple_eids=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmEventIT extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmEventIT.class);

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;

	@Test
	public void testDuplicateLinkChangeEvent() throws InterruptedException {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		myMdmHelper.createWithLatch(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		myMdmHelper.createWithLatch(patient2);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-22");
		myMdmHelper.createWithLatch(patient3);

		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-11");
		addExternalEID(patient2, "eid-22");

		myMdmHelper.updateWithLatch(patient2);

		MdmLinkEvent linkChangeEvent = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(MdmLinkEvent.class);
		assertNotNull(linkChangeEvent);

		ourLog.info("Got event: {}", linkChangeEvent);

		long expectTwoPossibleMatchesForPatientTwo = linkChangeEvent.getMdmLinks()
			.stream()
			.filter(l -> l.getSourceId().equals(patient2.getIdElement().toVersionless().getValueAsString()) && l.getMatchResult() == MdmMatchResultEnum.POSSIBLE_MATCH)
			.count();
		assertEquals(2, expectTwoPossibleMatchesForPatientTwo);

		long expectOnePossibleDuplicate = linkChangeEvent.getMdmLinks()
			.stream()
			.filter(l -> l.getMatchResult() == MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.count();
		assertEquals(1, expectOnePossibleDuplicate);

		List<MdmLinkJson> mdmLinkEvent = linkChangeEvent.getMdmLinks();
		assertEquals(3, mdmLinkEvent.size());
	}

	@Test
	public void testCreateLinkChangeEvent() throws InterruptedException {
		Practitioner pr = buildPractitionerWithNameAndId("Young", "AC-DC");
		myMdmHelper.createWithLatch(pr);

		ResourceOperationMessage resourceOperationMessage = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(ResourceOperationMessage.class);
		assertNotNull(resourceOperationMessage);
		assertEquals(pr.getId(), resourceOperationMessage.getId());

		MdmLink link = getLinkByTargetId(pr);

		MdmLinkEvent linkChangeEvent = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(MdmLinkEvent.class);
		assertNotNull(linkChangeEvent);

		assertEquals(1, linkChangeEvent.getMdmLinks().size());
		MdmLinkJson l = linkChangeEvent.getMdmLinks().get(0);
		assertEquals(link.getGoldenResourcePid(), new IdDt(l.getGoldenResourceId()).getIdPartAsLong());
		assertEquals(link.getSourcePid(), new IdDt(l.getSourceId()).getIdPartAsLong());
	}

	private MdmLink getLinkByTargetId(IBaseResource theResource) {
		MdmLink example = new MdmLink();
		example.setSourcePid(theResource.getIdElement().getIdPartAsLong());
		return myMdmLinkDao.findAll(Example.of(example)).get(0);
	}

	@Test
	public void testUpdateLinkChangeEvent() throws InterruptedException {
		Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
		myMdmHelper.createWithLatch(patient1);

		MdmLinkEvent linkChangeEvent = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(MdmLinkEvent.class);
		assertNotNull(linkChangeEvent);
		assertEquals(1, linkChangeEvent.getMdmLinks().size());

		MdmLinkJson link = linkChangeEvent.getMdmLinks().get(0);
		assertEquals(patient1.getIdElement().toVersionless().getValueAsString(), link.getSourceId());
		assertEquals(getLinkByTargetId(patient1).getGoldenResourcePid(), new IdDt(link.getGoldenResourceId()).getIdPartAsLong());
		assertEquals(MdmMatchResultEnum.MATCH, link.getMatchResult());
	}

}
