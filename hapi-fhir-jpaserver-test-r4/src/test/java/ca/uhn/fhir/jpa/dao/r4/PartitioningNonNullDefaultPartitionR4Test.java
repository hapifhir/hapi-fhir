package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class PartitioningNonNullDefaultPartitionR4Test extends BasePartitioningR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitioningNonNullDefaultPartitionR4Test.class);

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setDefaultPartitionId(1);
	}

	@AfterEach
	@Override
	public void after() {
		super.after();

		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
	}

	@Test
	public void testCreateAndSearch_NonPartitionable() {
		addCreateDefaultPartition();
		// we need two read partition accesses for when the creation of the SP triggers a reindex of Patient
		addReadDefaultPartition(); // one for search param validation
		addReadDefaultPartition(); // one to rewrite the resource url
		addReadDefaultPartition(); // and one for the job request itself
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		Long id = mySearchParameterDao.create(sp, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous().add("code", new TokenParam("extpatorg")), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/" + id);

		// Search on All Resources
		addReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/" + id);

	}

	@Test
	public void testCreateAndSearch_NonPartitionable_ForcedId() {
		addCreateDefaultPartition();
		// we need two read partition accesses for when the creation of the SP triggers a reindex of Patient
		addReadDefaultPartition(); // one for search param validation
		addReadDefaultPartition(); // one to rewrite the resource url
		addReadDefaultPartition(); // and one for the job request itself
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/A");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		mySearchParameterDao.update(sp, mySrd);

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findAll().get(0);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous().add("code", new TokenParam("extpatorg")), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/A");

		// Search on All Resources
		addReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(mySearchParameterDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("SearchParameter/A");

	}

	@Test
	public void testCreateAndSearch_Partitionable_ForcedId() {
		addCreateDefaultPartition();
		Patient patient = new Patient();
		patient.setId("A");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findAll().get(0);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("identifier", new TokenParam("http://foo", "123")), mySrd));
		assertThat(outcome).containsExactly("Patient/A");

		// Search on All Resources
		addReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("Patient/A");

	}


	@Test
	public void testCreateAndSearch_Partitionable() {
		addCreateDefaultPartition();
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem("http://foo").setCode("TAG");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		Long id = myPatientDao.create(patient, mySrd).getId().getIdPartAsLong();

		logAllResourceTags();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(1, resourceTable.getPartitionId().getPartitionId().intValue());
		});

		// Search on Token
		addReadDefaultPartition();
		List<String> outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("identifier", new TokenParam("http://foo", "123")), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

		// Search on Tag
		addReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous().add("_tag", new TokenParam("http://foo", "TAG")), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

		// Search on All Resources
		addReadDefaultPartition();
		outcome = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd));
		assertThat(outcome).containsExactly("Patient/" + id);

	}



	@Test
	public void testRead_Partitionable() {
		addCreateDefaultPartition();
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem("http://foo").setCode("TAG");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setActive(true);
		Long id = myPatientDao.create(patient, mySrd).getId().getIdPartAsLong();

		addReadDefaultPartition();
		patient = myPatientDao.read(new IdType("Patient/" + id), mySrd);
		assertTrue(patient.getActive());

		// Wrong partition
		addReadPartition(2);
		try {
			myPatientDao.read(new IdType("Patient/" + id), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}


}
