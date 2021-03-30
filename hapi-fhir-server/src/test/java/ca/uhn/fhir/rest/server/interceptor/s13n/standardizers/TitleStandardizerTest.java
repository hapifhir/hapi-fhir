package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TitleStandardizerTest {

	IStandardizer myStandardizer = new TitleStandardizer();

	@Test
	public void testSpecialCases() {
		assertEquals("Prescribeit", myStandardizer.standardize("prescribeit"));
		assertEquals("Meddialog", myStandardizer.standardize("MEDDIALOG"));
		assertEquals("20 / 20", myStandardizer.standardize("20 / 20"));
		assertEquals("20/20", myStandardizer.standardize("20/20"));
		assertEquals("L.L.P.", myStandardizer.standardize("L.L.P."));
		assertEquals("Green Tractors Clow Farm Equipment/", myStandardizer.standardize("GREEN TRACTORS CLOW FARM EQUIPMENT/"));
		assertEquals("Agilec - Barrie/Orillia (EPS)", myStandardizer.standardize("Agilec - Barrie/Orillia (EPS)"));
		assertEquals("Clement's/Callander Ida Pharmacies", myStandardizer.standardize("CLEMENT'S/CALLANDER IDA PHARMACIES"));
		assertEquals("Longley/Vickar L.L.P. Barristers & Solicitors", myStandardizer.standardize("LONGLEY/VICKAR L.L.P. BARRISTERS & SOLICITORS"));
		assertEquals("-Blan", myStandardizer.standardize("~Blan"));
		assertEquals("The (C/O Dr Mary Cooke)", myStandardizer.standardize("THE (C/O DR MARY COOKE)"));
		assertEquals("Sarah Ann Mary Pollock", myStandardizer.standardize("SARAH ANN MARY POLLOCK"));
		assertEquals("Voir...Être Vu! Opticiens", myStandardizer.standardize("VOIR...ÊTRE VU! OPTICIENS"));
		assertEquals("Back in Sync: Wellness Centre", myStandardizer.standardize("BACK IN SYNC: WELLNESS CENTRE"));
		assertEquals("Pearle Vision 9861 (Orchard Park S/C)", myStandardizer.standardize("PEARLE VISION 9861 (ORCHARD PARK S/C)"));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/organization_titles.csv", numLinesToSkip = 0)
	public void testTitleOrganizationsStandardization(String theExpected, String theInput) {
		String standardizedTitle = myStandardizer.standardize(theInput);
		assertEquals(theExpected, standardizedTitle);
	}

}
