package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import ca.uhn.fhir.mdm.api.MdmLinkEvent;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import org.hibernate.dialect.H2Dialect;
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
	@Autowired
	private IdHelperService myIdHelperService;

	@Bean
	@Primary
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "true");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());

		extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER), HapiLuceneAnalysisConfigurer.class.getName());
		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-heap");
		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
		extraProperties.put(HibernateOrmMapperSettings.ENABLED, "true");

		return extraProperties;
	}


	@Test
	public void testDuplicateLinkChangeEvent() throws InterruptedException {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-22");
		patient3 = createPatientAndUpdateLinks(patient3);

		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-11");
		addExternalEID(patient2, "eid-22");

		patient2 = (Patient) myPatientDao.update(patient2).getResource();
		MdmTransactionContext ctx = myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient2, createContextForUpdate(patient2.getIdElement().getResourceType()));

		MdmLinkEvent mdmLinkEvent = ctx.getMdmLinkEvent();
		assertFalse(mdmLinkEvent.getDuplicateGoldenResourceIds().isEmpty());
	}

	// @Test
	public void testCreateLinkChangeEvent() throws InterruptedException {
		Practitioner pr = buildPractitionerWithNameAndId("Young", "AC-DC");
		myMdmHelper.createWithLatch(pr);

		ResourceOperationMessage resourceOperationMessage = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(ResourceOperationMessage.class);
		assertNotNull(resourceOperationMessage);
		assertEquals(pr.getId(), resourceOperationMessage.getId());

		MdmLink link = getLinkByTargetId(pr);

		MdmLinkEvent linkChangeEvent = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(MdmLinkEvent.class);
		assertNotNull(linkChangeEvent);
		assertEquals(link.getGoldenResourcePid(), new IdDt(linkChangeEvent.getGoldenResourceId()).getIdPartAsLong());
		assertEquals(link.getSourcePid(), new IdDt(linkChangeEvent.getTargetResourceId()).getIdPartAsLong());
	}

	/*
	@Test
	public void testDuplicateLinkChangeEvent() throws InterruptedException {
		logAllLinks();
		for (IBaseResource r : myPatientDao.search(new SearchParameterMap()).getAllResources()) {
			ourLog.info("Found {}", r);
		}

		Patient myPatient = createPatientAndUpdateLinks(buildPaulPatient());
		StringType myPatientId = new StringType(myPatient.getIdElement().getValue());

		Patient mySourcePatient = getGoldenResourceFromTargetResource(myPatient);
		StringType mySourcePatientId = new StringType(mySourcePatient.getIdElement().getValue());
		StringType myVersionlessGodlenResourceId = new StringType(mySourcePatient.getIdElement().toVersionless().getValue());

		MdmLink myLink = myMdmLinkDaoSvc.findMdmLinkBySource(myPatient).get();
		// Tests require our initial link to be a POSSIBLE_MATCH
		myLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		saveLink(myLink);
		assertEquals(MdmLinkSourceEnum.AUTO, myLink.getLinkSource());
//		myDaoConfig.setExpungeEnabled(true);

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());

		// Add a possible duplicate
		StringType myLinkSource = new StringType(MdmLinkSourceEnum.AUTO.name());
		Patient sourcePatient1 = createGoldenPatient();
		StringType myGoldenResource1Id = new StringType(sourcePatient1.getIdElement().toVersionless().getValue());
		Long sourcePatient1Pid = myIdHelperService.getPidOrNull(sourcePatient1);
		Patient sourcePatient2 = createGoldenPatient();
		StringType myGoldenResource2Id = new StringType(sourcePatient2.getIdElement().toVersionless().getValue());
		Long sourcePatient2Pid = myIdHelperService.getPidOrNull(sourcePatient2);


		MdmLink possibleDuplicateMdmLink = myMdmLinkDaoSvc.newMdmLink().setGoldenResourcePid(sourcePatient1Pid).setSourcePid(sourcePatient2Pid).setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(MdmLinkSourceEnum.AUTO);
		saveLink(possibleDuplicateMdmLink);

		logAllLinks();

		ResourceOperationMessage resourceOperationMessage = myMdmHelper.getAfterMdmLatch().getLatchInvocationParameterOfType(ResourceOperationMessage.class);
		assertNotNull(resourceOperationMessage);
		assertEquals(sourcePatient2.getId(), resourceOperationMessage.getId());
	}
	 */

	private MdmLink getLinkByTargetId(IBaseResource theResource) {
		MdmLink example = new MdmLink();
		example.setSourcePid(theResource.getIdElement().getIdPartAsLong());
		return myMdmLinkDao.findAll(Example.of(example)).get(0);
	}

	@Test
	public void testUpdateLinkChangeEvent() throws InterruptedException {
		Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
		myMdmHelper.createWithLatch(patient1);

		MdmTransactionContext ctx = createContextForCreate("Patient");
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient1, ctx);
		ourLog.info(ctx.getMdmLinkEvent().toString());
		assertEquals(patient1.getIdElement().getValue(), ctx.getMdmLinkEvent().getTargetResourceId());
		assertEquals(getLinkByTargetId(patient1).getGoldenResourcePid(), new IdDt(ctx.getMdmLinkEvent().getGoldenResourceId()).getIdPartAsLong());

		Patient patient2 = addExternalEID(buildJanePatient(), "eid-2");
		myMdmHelper.createWithLatch(patient2);
		ctx = createContextForCreate("Patient");
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient2, ctx);
		ourLog.info(ctx.getMdmLinkEvent().toString());
		assertEquals(patient2.getIdElement().getValue(), ctx.getMdmLinkEvent().getTargetResourceId());
		assertEquals(getLinkByTargetId(patient2).getGoldenResourcePid(), new IdDt(ctx.getMdmLinkEvent().getGoldenResourceId()).getIdPartAsLong());
	}


}
