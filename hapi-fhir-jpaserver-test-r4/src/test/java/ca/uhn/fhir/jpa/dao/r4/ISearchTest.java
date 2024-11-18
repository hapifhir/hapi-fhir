package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public interface ISearchTest {
//	@Autowired
//	IFhirSystemDao mySystemDao;
//
//	@Autowired
//	DaoRegistry myDaoRegistry;
//
//	@Test
//	default void test() {
//		int numOfObs=10000;
//		int txSize=500;
//		myCaptureQueriesListener.clear();
//
//		String prefix = "p2";
//
////		IGenericClient myFhirClient = myFhirContext.newRestfulGenericClient("http://localhost:8000/");
////		myFhirClient.registerInterceptor(new BasicAuthInterceptor("admin","password"));
//		long startTime = System.currentTimeMillis();
//		System.out.println("start time---------------------->"+startTime);
//		doUpdateOrCreate(prefix, numOfObs, txSize);
//		System.out.println("---- INSERTS");
////		myCaptureQueriesListener.logInsertQueries();
//		doUpdateOrCreate(prefix, numOfObs, txSize);
//		System.out.println("----- UPDATES");
//		myCaptureQueriesListener.logAllQueries();
//
//		IFhirResourceDao<Observation> obsDao = myDaoRegistry.getResourceDao("Observation");
//		IBundleProvider result = obsDao.search(new SearchParameterMap().setLoadSynchronous(true), new SystemRequestDetails());
//		assertFalse(result.isEmpty());
//
//
////		myCaptureQueriesListener.logAllQueries();
//		long endTime = System.currentTimeMillis();
//		System.out.println("End time---------------------->"+endTime);
//		long elapsedTime = endTime - startTime;
//		System.out.println("Elapsed time (in ms) = "+elapsedTime);
//	}
//
//	private void doUpdateOrCreate(String prefix, int numOfObs, int txSize) {
//		for(int i=0;i<numOfObs/txSize;i++){
//			BundleBuilder builder = new BundleBuilder();
//			Bundle bundle = new Bundle();
//			bundle.setType(Bundle.BundleType.TRANSACTION);
//			List<Bundle.BundleEntryComponent> bundleEntryComponents = createObservations(prefix,10000 + i * txSize, txSize);
//			bundle.setEntry(bundleEntryComponents);
//			mySystemDao.transaction(new SystemRequestDetails(),
//				bundle);
////			myFhirClient.transaction()
////				.withBundle(bundle)
////				.withAdditionalHeader("X-RequestTrace-Enabled", "true")
////				.execute();
//			System.out.println("==================> "+i);
//		}
//
//	}
//
//	public static List<Bundle.BundleEntryComponent> createObservations(String prefix, int observationId, int num){
//		List<Bundle.BundleEntryComponent> list = new ArrayList<Bundle.BundleEntryComponent>();
//		for(int i=0;i<num;i++){
//			Bundle.BundleEntryComponent bundleEntryComponent = new Bundle.BundleEntryComponent();
//			Observation obs = new Observation();
//			List<Identifier> identifierList = new ArrayList<>();
//			identifierList.add(new Identifier().setValue(prefix+observationId+i));
//			obs.setIdentifier(identifierList);
//			obs.setStatus(Enumerations.ObservationStatus.FINAL);
//			Coding code = new Coding("http://loinc.org","85354-9","Blood pressure");
//			obs.setCode(new CodeableConcept().addCoding(code));
//			obs.setEffective(createDateTime("2020-01-01T12:00:00-05:00"));
//			Coding code1 = new Coding("http://loinc.org","8480-6","Systolic blood pressure");
//			List<Observation.ObservationComponentComponent> obsccList = new ArrayList<>();
//			Observation.ObservationComponentComponent obsvC = new Observation.ObservationComponentComponent();
//			CodeableConcept cc = new CodeableConcept().addCoding(code1);
//			obsvC.setValue(cc);
//			Quantity quantity = new Quantity();
//			quantity.setUnit("mmHg");
//			quantity.setValue(170);
//			quantity.setSystem("http://unitsofmeasure.org");
//			quantity.setCode("mm[Hg]");
//			obsvC.setValue(quantity);
//			obsccList.add(obsvC);
//
//			Observation.ObservationComponentComponent obsvC1 = new Observation.ObservationComponentComponent();
//			CodeableConcept cc1 = new CodeableConcept( new Coding("http://loinc.org", "8462-4", "Diastolic blood pressure"));
//			Quantity quantity1 = new Quantity();
//			quantity1.setValue(110);
//			quantity1.setUnit("mmHg");
//			quantity1.setSystem("http://unitsofmeasure.org");
//			quantity1.setCode("mm[Hg]");
//			obsvC1.setCode(cc1);
//			obsvC1.setValue(quantity1);
//			obsccList.add(obsvC1);
//			bundleEntryComponent.setResource(obs);
//			Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
//			bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.PUT);
//			bundleEntryRequestComponent.setUrl("Observation?identifier="+prefix+observationId+i);
//			bundleEntryComponent.setRequest(bundleEntryRequestComponent);
//			list.add(bundleEntryComponent);
//
//		}
//		return list;
//	}
//	static DateTimeType createDateTime(String theDateString) {
//		return new DateTimeType(theDateString);
//	}
}
