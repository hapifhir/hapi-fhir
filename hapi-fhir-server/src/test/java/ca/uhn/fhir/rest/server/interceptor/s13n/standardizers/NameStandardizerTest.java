package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NameStandardizerTest {

	private LastNameStandardizer myLastNameStandardizer = new LastNameStandardizer();
	private FirstNameStandardizer myFirstNameStandardizer = new FirstNameStandardizer();

	// for rules refer to https://docs.google.com/document/d/1Vz0vYwdDsqu6WrkRyzNiBJDLGmWAej5g/edit#

	@Test
	public void testCleanNoiseCharacters() {
		assertThat(myLastNameStandardizer.standardize("\u0070\u0075\u0062\u006c\u0069\u0063\u0020\u0020\u0020\u0020")).isEqualTo("Public");
		assertThat(myLastNameStandardizer.standardize("\t\r\nThis - ┴\t┬\t├\t─\t┼ text ! has \\\\ /Ã lot # of % special % characters\n")).isEqualTo("This - Text Has /ã Lot # Of Special Characters");
		assertThat(myLastNameStandardizer.standardize("nbsp \u00A0")).isEqualTo("Nbsp");
	}

	@Test
	public void testFrenchRemains() {
		assertThat(myLastNameStandardizer.standardize("   ÀàÂâ \tÆæ\n   ")).isEqualTo("Ààââ Ææ");
		assertThat(myLastNameStandardizer.standardize("D d'Équateur\n ")).isEqualTo("D D'Équateur");
		assertThat(myLastNameStandardizer.standardize("Des idées\nl'océan\n\n ")).isEqualTo("Des Idées L'Océan");
		assertThat(myLastNameStandardizer.standardize("Ne\tt'arrêtes\npas\n ")).isEqualTo("Ne T'Arrêtes Pas");
	}

	@Test
	public void testMe() {
		assertThat(myFirstNameStandardizer.standardize("tim   ☺ ")).isEqualTo("Tim");
	}

	@Test
	public void testNameNormalization() {
		assertThat(myFirstNameStandardizer.standardize("   TIM   ")).isEqualTo("Tim");
		assertThat(myFirstNameStandardizer.standardize("tim   ☺ ")).isEqualTo("Tim");
		assertThat(myLastNameStandardizer.standardize("      TiM BeRnErS-lEE\n")).isEqualTo("Tim Berners-Lee");
		assertThat(myLastNameStandardizer.standardize("\t\nSAra     o'leARy        \n\n")).isEqualTo("Sara O'Leary");
		assertThat(myLastNameStandardizer.standardize("\nBILL MCMASTER \n\n")).isEqualTo("Bill McMaster");
		assertThat(myLastNameStandardizer.standardize("\njohn macmASTER \n\n")).isEqualTo("John MacMaster");
		assertThat(myLastNameStandardizer.standardize("vincent van gogh")).isEqualTo("Vincent van Gogh");
		assertThat(myLastNameStandardizer.standardize("charles de gaulle\n")).isEqualTo("Charles de Gaulle");
		assertThat(myLastNameStandardizer.standardize("charles-gaspard de la rive")).isEqualTo("Charles-Gaspard de la Rive");
		assertThat(myLastNameStandardizer.standardize("niccolò machiavelli")).isEqualTo("Niccolò Machiavelli");
		assertThat(myLastNameStandardizer.standardize("o'reilly m'grego d'angelo macdonald mcfry")).isEqualTo("O'Reilly M'Grego D'Angelo MacDonald McFry");
		assertThat(myLastNameStandardizer.standardize("cornelius vanderbilt")).isEqualTo("Cornelius Vanderbilt");
		assertThat(myLastNameStandardizer.standardize("cornelius vanderbilt jr.")).isEqualTo("Cornelius Vanderbilt Jr.");
		assertThat(myLastNameStandardizer.standardize("william shakespeare")).isEqualTo("William Shakespeare");
		assertThat(myLastNameStandardizer.standardize("mr. william shakespeare")).isEqualTo("Mr. William Shakespeare");
		assertThat(myLastNameStandardizer.standardize("AMBER-LYNN O\u0080�BRIEN\n")).isEqualTo("Amber-Lynn O'Brien");
		assertThat(myLastNameStandardizer.standardize("NOELLE  BETHEA\n")).isEqualTo("Noelle Bethea");
		assertThat(myLastNameStandardizer.standardize("NAOMI ANNE  ECOB\n")).isEqualTo("Naomi Anne Ecob");
		assertThat(myLastNameStandardizer.standardize("SARAH ANN MARY POLLOCK\n")).isEqualTo("Sarah Ann Mary Pollock");
		assertThat(myLastNameStandardizer.standardize("TARIT KUMAR  KANUNGO\n")).isEqualTo("Tarit Kumar Kanungo");
		assertThat(myLastNameStandardizer.standardize("TRAM ANH THI  NGUYEN\n")).isEqualTo("Tram Anh Thi Nguyen");
		assertThat(myLastNameStandardizer.standardize("WILLIAM L. TRENWITH / PAUL J. TRENWITH\n")).isEqualTo("William L. Trenwith / Paul J. Trenwith");
	}

	@Test
	public void testFirstNameNoPrefix() {
		assertThat(myFirstNameStandardizer.standardize("MACKENZIE-JONAH")).isEqualTo("Mackenzie-Jonah");
		assertThat(myFirstNameStandardizer.standardize("CHARLES-Gaspard")).isEqualTo("Charles-Gaspard");
	}

	@Test
	public void testTranslateMagic() {
		assertThat(myLastNameStandardizer.standardize("O\u0080�BRIEN\n")).isEqualTo("O'Brien");
		assertThat(myLastNameStandardizer.standardize("O \u0080� BRIEN\n")).isEqualTo("O ' Brien");
		assertThat(myLastNameStandardizer.standardize("O \u0080�BRIEN\n")).isEqualTo("O 'Brien");
		assertThat(myLastNameStandardizer.standardize("O\u0080 BRIEN\n")).isEqualTo("O' Brien");
	}
}
