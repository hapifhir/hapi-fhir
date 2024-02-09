package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TitleStandardizerTest {

	IStandardizer myStandardizer = new TitleStandardizer();

	@Test
	public void testSpecialCases() {
		assertThat(myStandardizer.standardize("prescribeit")).isEqualTo("Prescribeit");
		assertThat(myStandardizer.standardize("MEDDIALOG")).isEqualTo("Meddialog");
		assertThat(myStandardizer.standardize("20 / 20")).isEqualTo("20 / 20");
		assertThat(myStandardizer.standardize("20/20")).isEqualTo("20/20");
		assertThat(myStandardizer.standardize("L.L.P.")).isEqualTo("L.L.P.");
		assertThat(myStandardizer.standardize("GREEN TRACTORS CLOW FARM EQUIPMENT/")).isEqualTo("Green Tractors Clow Farm Equipment/");
		assertThat(myStandardizer.standardize("Agilec - Barrie/Orillia (EPS)")).isEqualTo("Agilec - Barrie/Orillia (EPS)");
		assertThat(myStandardizer.standardize("CLEMENT'S/CALLANDER IDA PHARMACIES")).isEqualTo("Clement's/Callander Ida Pharmacies");
		assertThat(myStandardizer.standardize("LONGLEY/VICKAR L.L.P. BARRISTERS & SOLICITORS")).isEqualTo("Longley/Vickar L.L.P. Barristers & Solicitors");
		assertThat(myStandardizer.standardize("~Blan")).isEqualTo("-Blan");
		assertThat(myStandardizer.standardize("THE (C/O DR MARY COOKE)")).isEqualTo("The (C/O Dr Mary Cooke)");
		assertThat(myStandardizer.standardize("SARAH ANN MARY POLLOCK")).isEqualTo("Sarah Ann Mary Pollock");
		assertThat(myStandardizer.standardize("VOIR...ÊTRE VU! OPTICIENS")).isEqualTo("Voir...Être Vu! Opticiens");
		assertThat(myStandardizer.standardize("BACK IN SYNC: WELLNESS CENTRE")).isEqualTo("Back in Sync: Wellness Centre");
		assertThat(myStandardizer.standardize("PEARLE VISION 9861 (ORCHARD PARK S/C)")).isEqualTo("Pearle Vision 9861 (Orchard Park S/C)");
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/organization_titles.csv", numLinesToSkip = 0)
	public void testTitleOrganizationsStandardization(String theExpected, String theInput) {
		String standardizedTitle = myStandardizer.standardize(theInput);
		assertThat(standardizedTitle).isEqualTo(theExpected);
	}

}
