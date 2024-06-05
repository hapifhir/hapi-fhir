package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class BaseHfqlExecutorTest {

	protected final RequestDetails mySrd = new SystemRequestDetails();
	@Spy
	protected FhirContext myCtx = FhirContext.forR4Cached();
	@Mock
	protected DaoRegistry myDaoRegistry;
	@Mock
	protected IPagingProvider myPagingProvider;
	@Spy
	protected ISearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myCtx);
	@InjectMocks
	protected HfqlExecutor myHfqlExecutor = new HfqlExecutor();
	@Captor
	protected ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@SuppressWarnings("unchecked")
	protected <T extends IBaseResource> IFhirResourceDao<T> initDao(Class<T> theType) {
		IFhirResourceDao<T> retVal = mock(IFhirResourceDao.class);
		String type = myCtx.getResourceType(theType);
		when(myDaoRegistry.getResourceDao(type)).thenReturn(retVal);
		return retVal;
	}

	@Nonnull
	protected static List<List<Object>> readAllRowValues(IHfqlExecutionResult result) {
		List<List<Object>> rowValues = new ArrayList<>();
		while (result.hasNext()) {
			rowValues.add(new ArrayList<>(result.getNextRow().getRowValues()));
		}
		return rowValues;
	}

	@Nonnull
	protected static Observation createCardiologyNoteObservation(String id, String noteText) {
		Observation obs = new Observation();
		obs.setId(id);
		obs.getCode().addCoding()
			.setSystem("http://loinc.org")
			.setCode("34752-6");
		obs.setValue(new StringType(noteText));
		return obs;
	}

	@Nonnull
	protected static Observation createWeightObservationWithKilos(String obsId, long kg) {
		Observation obs = new Observation();
		obs.setId(obsId);
		obs.getCode().addCoding()
			.setSystem("http://loinc.org")
			.setCode("29463-7");
		obs.setValue(new Quantity(null, kg, "http://unitsofmeasure.org", "kg", "kg"));
		return obs;
	}

	@Nonnull
	protected static SimpleBundleProvider createProviderWithSparseNames() {
		Patient patientNoValues = new Patient();
		patientNoValues.setActive(true);
		Patient patientFamilyNameOnly = new Patient();
		patientFamilyNameOnly.addName().setFamily("Simpson");
		Patient patientGivenNameOnly = new Patient();
		patientGivenNameOnly.addName().addGiven("Homer");
		Patient patientBothNames = new Patient();
		patientBothNames.addName().setFamily("Simpson").addGiven("Homer");
		return new SimpleBundleProvider(List.of(
			patientNoValues, patientFamilyNameOnly, patientGivenNameOnly, patientBothNames));
	}

	@Nonnull
	protected static SimpleBundleProvider createProviderWithSomeSimpsonsAndFlanders() {
		return new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientNedFlanders(),
			createPatientBartSimpson(),
			createPatientLisaSimpson(),
			createPatientMaggieSimpson()
		);
	}

	@Nonnull
	protected static SimpleBundleProvider createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates() {
		return new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientHomerSimpson(),
			createPatientNedFlanders(),
			createPatientNedFlanders(),
			createPatientBartSimpson(),
			createPatientLisaSimpson(),
			createPatientMaggieSimpson());
	}

	@Nonnull
	protected static Patient createPatientMaggieSimpson() {
		Patient maggie = new Patient();
		maggie.addName().setFamily("Simpson").addGiven("Maggie").addGiven("Evelyn");
		maggie.addIdentifier().setSystem("http://system").setValue("value4");
		return maggie;
	}

	@Nonnull
	protected static Patient createPatientLisaSimpson() {
		Patient lisa = new Patient();
		lisa.getMeta().setVersionId("1");
		lisa.addName().setFamily("Simpson").addGiven("Lisa").addGiven("Marie");
		lisa.addIdentifier().setSystem("http://system").setValue("value3");
		return lisa;
	}

	@Nonnull
	protected static Patient createPatientBartSimpson() {
		Patient bart = new Patient();
		bart.getMeta().setVersionId("3");
		bart.addName().setFamily("Simpson").addGiven("Bart").addGiven("El Barto");
		bart.addIdentifier().setSystem("http://system").setValue("value2");
		return bart;
	}

	@Nonnull
	protected static Patient createPatientNedFlanders() {
		Patient nedFlanders = new Patient();
		nedFlanders.getMeta().setVersionId("1");
		nedFlanders.addName().setFamily("Flanders").addGiven("Ned");
		nedFlanders.addIdentifier().setSystem("http://system").setValue("value1");
		return nedFlanders;
	}

	@Nonnull
	protected static Patient createPatientHomerSimpson() {
		Patient homer = new Patient();
		homer.setId("HOMER0");
		homer.getMeta().setVersionId("2");
		homer.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		homer.addIdentifier().setSystem("http://system").setValue("value0");
		homer.setBirthDateElement(new DateType("1950-01-01"));
		return homer;
	}


}
