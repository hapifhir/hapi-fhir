package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiResourceComparator;
import ca.uhn.fhir.empi.rules.EmpiRulesJson;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.empi.config.EmpiConfig;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;

	protected Person myPerson;
	protected Patient myPatient;
	protected IIdType myPersonId;
	protected IIdType myPatientId;
	protected EmpiResourceComparator myEmpiResourceComparator;
	protected Long myPersonPid;
	protected Long myPatientPid;

	@Before
	public void before() throws IOException {
		{
			DaoMethodOutcome outcome = myPersonDao.create(new Person());
			myPersonId = outcome.getId().toUnqualifiedVersionless();
			myPersonPid = ResourceTableHelper.getPidOrNull(outcome.getResource());
			myPerson = myPersonDao.read(myPersonId);
		}
		{
			DaoMethodOutcome outcome = myPatientDao.create(new Patient());
			myPatientId = outcome.getId().toUnqualifiedVersionless();
			myPatientPid = ResourceTableHelper.getPidOrNull(outcome.getResource());
			myPatient = myPatientDao.read(myPatientId);
		}
		myEmpiResourceComparator = buildEmpiResourceComparator();
	}

	public EmpiResourceComparator buildEmpiResourceComparator() throws IOException {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("empi/empi-rules.json");
		String json = IOUtils.toString(resource.getInputStream(), Charsets.UTF_8);
		EmpiRulesJson rules = JsonUtil.deserialize(json, EmpiRulesJson.class);
		return new EmpiResourceComparator(myFhirContext, rules);
	}


}
