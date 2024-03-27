package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoiseCharactersTest {

	private NoiseCharacters myFilter = new NoiseCharacters();

	@Test
	public void testInit() {
		myFilter.initializeFromClasspath();
		assertTrue(myFilter.getSize() > 0);

		myFilter = new NoiseCharacters();
	}

	@Test
	public void testAdd() {
		myFilter.add("#x0487");

		char check = (char) Integer.parseInt("487", 16);
		assertTrue(myFilter.isNoise(check));
		assertFalse(myFilter.isNoise('A'));
	}

	@Test
	public void testAddRange() {
		myFilter.addRange("#x0487-#x0489");

		char check = (char) Integer.parseInt("487", 16);
		assertTrue(myFilter.isNoise(check));
		check = (char) Integer.parseInt("488", 16);
		assertTrue(myFilter.isNoise(check));
		check = (char) Integer.parseInt("489", 16);
		assertTrue(myFilter.isNoise(check));

		assertFalse(myFilter.isNoise('A'));
	}

	@Test
	public void testAddLongRange() {
		myFilter.addRange("#x0487-#xA489");

		char check = (char) Integer.parseInt("487", 16);
		assertTrue(myFilter.isNoise(check));
		check = (char) Integer.parseInt("488", 16);
		assertTrue(myFilter.isNoise(check));
		check = (char) Integer.parseInt("489", 16);
		assertTrue(myFilter.isNoise(check));

		assertFalse(myFilter.isNoise('A'));
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "1", "ABC", "\\u21", "#x0001-#x0000", "#x0001 - #x - #x0000", "#x0000 #x0022"})
	public void testInvalidChar(String invalidPattern) {
		assertThrows(IllegalArgumentException.class, () -> myFilter.add(invalidPattern));
	}

}
