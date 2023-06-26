package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameStandardizerTest {

	private LastNameStandardizer myLastNameStandardizer = new LastNameStandardizer();
	private FirstNameStandardizer myFirstNameStandardizer = new FirstNameStandardizer();

	// for rules refer to https://docs.google.com/document/d/1Vz0vYwdDsqu6WrkRyzNiBJDLGmWAej5g/edit#

	@Test
	public void testCleanNoiseCharacters() {
		assertEquals("Public", myLastNameStandardizer.standardize("\u0070\u0075\u0062\u006c\u0069\u0063\u0020\u0020\u0020\u0020"));
		assertEquals("This - Text Has /ã Lot # Of Special Characters", myLastNameStandardizer.standardize("\t\r\nThis - ┴\t┬\t├\t─\t┼ text ! has \\\\ /Ã lot # of % special % characters\n"));
		assertEquals("Nbsp", myLastNameStandardizer.standardize("nbsp \u00A0"));
	}

	@Test
	public void testFrenchRemains() {
		assertEquals("Ààââ Ææ", myLastNameStandardizer.standardize("   ÀàÂâ \tÆæ\n   "));
		assertEquals("D D'Équateur", myLastNameStandardizer.standardize("D d'Équateur\n "));
		assertEquals("Des Idées L'Océan", myLastNameStandardizer.standardize("Des idées\nl'océan\n\n "));
		assertEquals("Ne T'Arrêtes Pas", myLastNameStandardizer.standardize("Ne\tt'arrêtes\npas\n "));
	}

	@Test
	public void testMe() {
		assertEquals("Tim", myFirstNameStandardizer.standardize("tim   ☺ "));
	}

	@Test
	public void testNameNormalization() {
		assertEquals("Tim", myFirstNameStandardizer.standardize("   TIM   "));
		assertEquals("Tim", myFirstNameStandardizer.standardize("tim   ☺ "));
		assertEquals("Tim Berners-Lee", myLastNameStandardizer.standardize("      TiM BeRnErS-lEE\n"));
		assertEquals("Sara O'Leary", myLastNameStandardizer.standardize("\t\nSAra     o'leARy        \n\n"));
		assertEquals("Bill McMaster", myLastNameStandardizer.standardize("\nBILL MCMASTER \n\n"));
		assertEquals("John MacMaster", myLastNameStandardizer.standardize("\njohn macmASTER \n\n"));
		assertEquals("Vincent van Gogh", myLastNameStandardizer.standardize("vincent van gogh"));
		assertEquals("Charles de Gaulle", myLastNameStandardizer.standardize("charles de gaulle\n"));
		assertEquals("Charles-Gaspard de la Rive", myLastNameStandardizer.standardize("charles-gaspard de la rive"));
		assertEquals("Niccolò Machiavelli", myLastNameStandardizer.standardize("niccolò machiavelli"));
		assertEquals("O'Reilly M'Grego D'Angelo MacDonald McFry", myLastNameStandardizer.standardize("o'reilly m'grego d'angelo macdonald mcfry"));
		assertEquals("Cornelius Vanderbilt", myLastNameStandardizer.standardize("cornelius vanderbilt"));
		assertEquals("Cornelius Vanderbilt Jr.", myLastNameStandardizer.standardize("cornelius vanderbilt jr."));
		assertEquals("William Shakespeare", myLastNameStandardizer.standardize("william shakespeare"));
		assertEquals("Mr. William Shakespeare", myLastNameStandardizer.standardize("mr. william shakespeare"));
		assertEquals("Amber-Lynn O'Brien", myLastNameStandardizer.standardize("AMBER-LYNN O\u0080�BRIEN\n"));
		assertEquals("Noelle Bethea", myLastNameStandardizer.standardize("NOELLE  BETHEA\n"));
		assertEquals("Naomi Anne Ecob", myLastNameStandardizer.standardize("NAOMI ANNE  ECOB\n"));
		assertEquals("Sarah Ann Mary Pollock", myLastNameStandardizer.standardize("SARAH ANN MARY POLLOCK\n"));
		assertEquals("Tarit Kumar Kanungo", myLastNameStandardizer.standardize("TARIT KUMAR  KANUNGO\n"));
		assertEquals("Tram Anh Thi Nguyen", myLastNameStandardizer.standardize("TRAM ANH THI  NGUYEN\n"));
		assertEquals("William L. Trenwith / Paul J. Trenwith", myLastNameStandardizer.standardize("WILLIAM L. TRENWITH / PAUL J. TRENWITH\n"));
	}

	@Test
	public void testFirstNameNoPrefix() {
		assertEquals("Mackenzie-Jonah", myFirstNameStandardizer.standardize("MACKENZIE-JONAH"));
		assertEquals("Charles-Gaspard", myFirstNameStandardizer.standardize("CHARLES-Gaspard"));
	}

	@Test
	public void testTranslateMagic() {
		assertEquals("O'Brien", myLastNameStandardizer.standardize("O\u0080�BRIEN\n"));
		assertEquals("O ' Brien", myLastNameStandardizer.standardize("O \u0080� BRIEN\n"));
		assertEquals("O 'Brien", myLastNameStandardizer.standardize("O \u0080�BRIEN\n"));
		assertEquals("O' Brien", myLastNameStandardizer.standardize("O\u0080 BRIEN\n"));
	}
}
