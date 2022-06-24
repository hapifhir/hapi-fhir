package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.collections.map.HashedMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This testing class and the used resources are work in progress and will likely be subject to change.
 */
public class CqlMeasureEvaluationR4ImmunizationTest extends BaseCqlR4Test {
    Logger ourLog = LoggerFactory.getLogger(CqlMeasureEvaluationR4ImmunizationTest.class);

    @Autowired
	 MeasureOperationsProvider myMeasureOperationsProvider;

	 private static final String MY_TESTBUNDLE_MMR_SIMPLE = "r4/immunization/testdata-bundles/Testbundle_3Patients_1MMRVaccinated_1PVaccinated_1NoVaccination.json";
	 private static final String MY_TESTBUNDLE_MMR_INCL_PRACTITIONER = "r4/immunization/testdata-bundles/Testbundle_Patients_By_Practitioner.json";


    //overall testing function including bundle manipulation and evaluation and assertion
    protected void testMeasureScoresByBundleAndCQLLocation(String theBundleLocation, String theCQLMeasureLocation, String thePractitionerRef, Map<String, Double> theExpectedScores) throws IOException {

        //load provided bundle and replace placeholder CQL content of Library with CQL content of provided file location
        Bundle bundle = loadBundleFromFileLocationAndManipulate(theBundleLocation, theCQLMeasureLocation);
        //we require at least one MeasureReport in the bundle
        List<MeasureReport> measureReports = findMeasureReportsOrThrowException(bundle);

        //evaluate each measure report of the provided bundle
        for (MeasureReport report : measureReports) {
			  double expectedScore = theExpectedScores.get(report.getIdentifierFirstRep().getValue());
			  evaluateSingleReport(report, thePractitionerRef, expectedScore);
        }
    }

	//overall testing function including bundle manipulation and evaluation and assertion
	protected void testMeasureScoresByBundleAndCQLLocation(String theBundleLocation, String theCQLMeasureLocation, String thePractitionerRef, double theExpectedScore) throws IOException {
		//load provided bundle and replace placeholder CQL content of Library with CQL content of provided file location
		Bundle bundle = loadBundleFromFileLocationAndManipulate(theBundleLocation, theCQLMeasureLocation);
		//we require at least one MeasureReport in the bundle
		List<MeasureReport> measureReports = findMeasureReportsOrThrowException(bundle);

		//evaluate each measure report of the provided bundle
		for (MeasureReport report : measureReports) {
			evaluateSingleReport(report, thePractitionerRef, theExpectedScore);
		}
	}

	protected void evaluateSingleReport (MeasureReport theReport, String thePractitionerRef, double theExpectedScore) {
		MeasureReport actualEvaluatedReport = this.evaluateMeasure(theReport, thePractitionerRef);
		ourLog.info("Score of evaluation: {}", actualEvaluatedReport.getGroupFirstRep().getMeasureScore().getValue());
		assertMeasureScore(actualEvaluatedReport, theExpectedScore);
	}
    //double compare to assert no difference between expected and actual measure score
    protected void assertMeasureScore(MeasureReport theReport, double theExpectedScore) {
        //find the predefined expected score by looking up the report identifier
        double epsilon = 0.000001d;
        double actualScore = theReport.getGroupFirstRep().getMeasureScore().getValue().doubleValue();
        assertEquals(theExpectedScore, actualScore, epsilon);
    }

    //evaluates a Measure to produce one certain MeasureReport
    protected MeasureReport evaluateMeasure(MeasureReport theMeasureReport, String thePractitionerRef) {
        String measureId = this.getMeasureId(theMeasureReport);
        String patientId = null;
        //only when the type of the MeasureReport is set to 'individual', there is a patient required as a subject (i.e. not for a 'summary' report)
        if (theMeasureReport.getSubject().getReference() != null) {
            patientId = this.getPatientId(theMeasureReport);
        }
        String periodStart = this.getPeriodStart(theMeasureReport);
        String periodEnd = this.getPeriodEnd(theMeasureReport);
        String measureReportIdentifier = theMeasureReport.getIdentifierFirstRep().getValue();
        String measureReportType = theMeasureReport.getTypeElement().getValueAsString();

        //only when the type of the MeasureReport is set to 'individual', there is a patient required as a subject (i.e. not for a 'summary' report)
        if (patientId == null) {
            ourLog.info("Evaluating Measure '{}' for MeasureReport '{}' of type '{}': [{} - {}]", measureId, measureReportIdentifier, measureReportType, periodStart, periodEnd);
        } else {
            ourLog.info("Evaluating Measure '{}' for MeasureReport '{}' of type '{}' for Patient '{}' [{} - {}]", measureId, measureReportIdentifier, measureReportType, patientId, periodStart, periodEnd);
        }

        return this.myMeasureOperationsProvider.evaluateMeasure(new IdType("Measure", measureId),
                periodStart, periodEnd, null,

                "patient", patientId,

                null, thePractitionerRef, null, null, null, null, myRequestDetails);
    }

    //helper function to manipulate a test bundle
    //loads a bundle from theBundleLocation: requirement: containing 1 exact FHIR Measure
    //finds the relevant measure, determines the related Library and replaces the CQL content of that Library with a separate CQL file
    //the CQL file defined by theCQLLocation contains regular CQL text and is automatically transformed into base64 content and automatically replaces the Library content
    //this process keeps manual testing simple, as the only place to change the CQL logic is by adding a new CQL file (containing no test resources and no other content) and writing a unit test with that CQL file path
    protected Bundle loadBundleFromFileLocationAndManipulate(String theBundleLocation, String theCQLLocation) throws IOException {
        Bundle bundle = parseBundle(theBundleLocation);
        //manipulate Bundle
        Measure measure = findExactlyOneMeasuresOrThrowException(bundle);
        Library libraryToManipulate = findLibraryById(bundle, measure.getLibrary().get(0).getValue());
        replaceCQLOfLibrary(libraryToManipulate, theCQLLocation);
        loadBundle(bundle, myRequestDetails);
        return bundle;
    }

    //requirement: test bundle must contain exactly one Measure resource
    protected Measure findExactlyOneMeasuresOrThrowException(Bundle theBundle) {
        List<Measure> measures = BundleUtil.toListOfResourcesOfType(myFhirContext, theBundle, Measure.class);
        if (measures == null || measures.isEmpty()) {
            throw new IllegalArgumentException(String.format("No measures found for Bundle %s", theBundle.getId()));
        } else if (measures.size() > 1) {
            throw new IllegalArgumentException(String.format("Too many measures found for Bundle %s. Only one measure is allowed for this automated testing setup.", theBundle.getId()));
        }
        return measures.get(0);
    }

    //requirement: test bundle must contain at least one MeasureReport resource
    protected List<MeasureReport> findMeasureReportsOrThrowException(Bundle theBundle) {
        List<MeasureReport> reports = BundleUtil.toListOfResourcesOfType(myFhirContext, theBundle, MeasureReport.class);
        if (reports == null || reports.isEmpty()) {
            throw new IllegalArgumentException(String.format("No measure reports found for Bundle %s", theBundle.getId()));
        }
        return reports;
    }

    //returns a Library resource of a bundle by a given ID
    protected Library findLibraryById(Bundle theBundle, String theLibraryId) {
		 List<Library> libraries = BundleUtil.toListOfResourcesOfType(myFhirContext, theBundle, Library.class);
		 return libraries.stream().filter(lib -> lib.getId().equals(theLibraryId)).findFirst().orElse(null);
    }

    //the provided Library resource only contains a placeholder CQL
    //this function replaces this placeholder CQL with the content of a specified file
    protected Library replaceCQLOfLibrary(Library theLibrary, String theCQLFileLocation) throws IOException {
        String decodedCQLString = stringFromResource(theCQLFileLocation);

        //replace cql in library
        String encodedCQLString = Base64.getEncoder().encodeToString(decodedCQLString.getBytes(StandardCharsets.UTF_8));
        Base64BinaryType encodedCQLBinary = new Base64BinaryType();
        encodedCQLBinary.setValueAsString(encodedCQLString);
        theLibrary.getContentFirstRep().setDataElement(encodedCQLBinary);
        return theLibrary;
    }

    // TODO: In R4 the Subject will not necessarily be a Patient.
    public String getPatientId(MeasureReport measureReport) {
        String[] subjectRefParts = measureReport.getSubject().getReference().split("/");
        String patientId = subjectRefParts[subjectRefParts.length - 1];
        return patientId;
    }

    public String getMeasureId(MeasureReport measureReport) {
        String[] measureRefParts = measureReport.getMeasure().split("/");
        String measureId = measureRefParts[measureRefParts.length - 1];
        return measureId;
    }

    public String getPeriodStart(MeasureReport measureReport) {
        Date periodStart = measureReport.getPeriod().getStart();
        if (periodStart != null) {
            return toDateString(periodStart);
        }
        return null;
    }

    public String getPeriodEnd(MeasureReport measureReport) {
        Date periodEnd = measureReport.getPeriod().getEnd();
        if (periodEnd != null) {
            return toDateString(periodEnd);
        }
        return null;
    }

    public String toDateString(Date date) {
        return new DateTimeType(date).getValueAsString();
    }


	@Test
	public void test_Immunization_MMR_Individual_Vaccinated() throws IOException {
		Map<String, Double> expectedScoresByIdentifier = new HashedMap();

		//expected result: individual should be in numerator because Patient is MMR vaccinated
		expectedScoresByIdentifier.put("measureReportIndividualVaccinatedPatient", 1.0);
		//expected result: individual should not be in numerator because Patient is not MMR vaccinated (only Pertussis)
		expectedScoresByIdentifier.put("measureReportIndividualNotMMRVaccinatedPatient", 0.0);
		//expected result: individual should not be in numerator because Patient is not at all vaccinated (no associated Immmunization resource)
		expectedScoresByIdentifier.put("measureReportIndividualNotAtAllVaccinatedPatient", 0.0);
		//expected result: summary confirms that 1 out of all 3 patients are MMR immunized
		expectedScoresByIdentifier.put("measureReportSummary", 1.0 / 3.0);

		//note: all those CQL files specified as the second parameter produce the exact same outcome with the given test resources provided by the first parameter.
		//TODO: tests are dependent and will fail if the order is incorrect. --> clean up tests
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_SIMPLE, "r4/immunization/cqls/3-Vaccine-Codes-Defined-By-ValueSet-MMR-Vaccine-Codes.cql", null, expectedScoresByIdentifier);
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_SIMPLE, "r4/immunization/cqls/1-Explicit-Vaccine-Codes-From-Any-System.cql", null, expectedScoresByIdentifier);
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_SIMPLE, "r4/immunization/cqls/2-Explicit-Vaccine-Codes-And-Systems.cql", null, expectedScoresByIdentifier);
	}


	@Test
	public void test_Immunization_ByPractitioner_MMR_Summary() throws IOException {
		//half of dreric' s patients (total of 2) are vaccinated, so 1/2 is vaccinated.
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_INCL_PRACTITIONER, "r4/immunization/cqls/3-Vaccine-Codes-Defined-By-ValueSet-MMR-Vaccine-Codes.cql", "Practitioner/dreric", 1.0/2.0);
		//of drfrank's patients (total of 1), none are vaccinated
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_INCL_PRACTITIONER, "r4/immunization/cqls/3-Vaccine-Codes-Defined-By-ValueSet-MMR-Vaccine-Codes.cql", "Practitioner/drfrank", 0.0/1.0);
	}

	@Test
	public void test_Immunization_ByAge() throws IOException {
		//be aware that this test will fail eventually, because this patient will at some point become one years old (today - birthdate > 1 year)
		//this patient is not yet immunized, because too young. so it won't be counted as a denominator patient and therefore the measure score is corrected to have a higher percentage
		//of dreric' s patients, all are vaccinated, because the second patient who is not vaccinated yet doesn't meet the age criteria (denominator), so 1/1 is vaccinated.
		this.testMeasureScoresByBundleAndCQLLocation(MY_TESTBUNDLE_MMR_INCL_PRACTITIONER, "r4/immunization/cqls/4-Patients-ByAge.cql", "Practitioner/dreric", 1.0/1.0);
	 }
}
