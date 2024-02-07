package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.Nonnull;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.NoFT.class})
@SuppressWarnings({"Duplicates"})
public class ResourceProviderR4ValueSetHSearchDisabledTest extends BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4ValueSetHSearchDisabledTest.class);


	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	private IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Autowired
	@Qualifier("myResourceProvidersR4")
	private ResourceProviderFactory myResourceProviders;
	@Autowired
	private ApplicationContext myAppCtx;


	private IIdType myExtensionalCsId;
	private IIdType myExtensionalVsId;
	@SuppressWarnings("JUnitMalformedDeclaration")
	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.withServer(t -> t.registerProviders(myResourceProviders.createProviders()))
		.withServer(t -> t.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class)))
		.withServer(t -> t.setDefaultResponseEncoding(EncodingEnum.XML))
		.withServer(t -> t.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class)));

	private void loadAndPersistCodeSystemAndValueSet() throws IOException {
		loadAndPersistCodeSystem();
		loadAndPersistValueSet();
	}

	private void loadAndPersistCodeSystem() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem);
	}

	private void persistCodeSystem(CodeSystem theCodeSystem) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myExtensionalCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
			}
		});
		myCodeSystemDao.readEntity(myExtensionalCsId, null);
	}

	private void loadAndPersistValueSet() throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet);
	}

	private void persistValueSet(ValueSet theValueSet) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
			}
		});
		myValueSetDao.readEntity(myExtensionalVsId, null);
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Test
	public void testExpandById() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myServer
			.getFhirClient()
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp).contains("<ValueSet xmlns=\"http://hl7.org/fhir\">");
		assertThat(resp).contains("<expansion>");
		assertThat(resp).contains("<contains>");
		assertThat(resp).contains("<system value=\"http://acme.org\"/>");
		assertThat(resp).contains("<code value=\"8450-9\"/>");
		assertThat(resp).contains("<display value=\"Systolic blood pressure--expiration\"/>");
		assertThat(resp).contains("</contains>");
		assertThat(resp).contains("<contains>");
		assertThat(resp).contains("<system value=\"http://acme.org\"/>");
		assertThat(resp).contains("<code value=\"11378-7\"/>");
		assertThat(resp).contains("<display value=\"Systolic blood pressure at First encounter\"/>");
		assertThat(resp).contains("</contains>");
		assertThat(resp).contains("</expansion>");

	}


}
