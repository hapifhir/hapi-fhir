package ca.uhn.fhir.jpa.batch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.config.BatchJobConfig;
import ca.uhn.fhir.jpa.batch.config.InMemoryJobRepositoryBatchConfig;
import ca.uhn.fhir.jpa.batch.svc.DummyService;
import ca.uhn.fhir.jpa.config.TestJpaR4Config;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchJobConfig.class, InMemoryJobRepositoryBatchConfig.class, TestJpaR4Config.class})
abstract public class BaseBatchR4Test extends BaseJpaR4Test {
 	private static final Logger ourLog = getLogger(BaseBatchR4Test.class);

 	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	protected DummyService myDummyService;
	@Autowired
	protected JobLauncher myJobLauncher;
	@Autowired
	protected Job myJob;

}
