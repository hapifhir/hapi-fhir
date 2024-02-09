package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class NoiseCharactersTest {

	private NoiseCharacters myFilter = new NoiseCharacters();

	@Test
	public void testInit() {
		myFilter.initializeFromClasspath();
		assertThat(myFilter.getSize() > 0).isTrue();

		myFilter = new NoiseCharacters();
	}

	@Test
	public void testAdd() {
		myFilter.add("#x0487");

		char check = (char) Integer.parseInt("487", 16);
		assertThat(myFilter.isNoise(check)).isTrue();
		assertThat(myFilter.isNoise('A')).isFalse();
	}

	@Test
	public void testAddRange() {
		myFilter.addRange("#x0487-#x0489");

		char check = (char) Integer.parseInt("487", 16);
		assertThat(myFilter.isNoise(check)).isTrue();
		check = (char) Integer.parseInt("488", 16);
		assertThat(myFilter.isNoise(check)).isTrue();
		check = (char) Integer.parseInt("489", 16);
		assertThat(myFilter.isNoise(check)).isTrue();

		assertThat(myFilter.isNoise('A')).isFalse();
	}

	@Test
	public void testAddLongRange() {
		myFilter.addRange("#x0487-#xA489");

		char check = (char) Integer.parseInt("487", 16);
		assertThat(myFilter.isNoise(check)).isTrue();
		check = (char) Integer.parseInt("488", 16);
		assertThat(myFilter.isNoise(check)).isTrue();
		check = (char) Integer.parseInt("489", 16);
		assertThat(myFilter.isNoise(check)).isTrue();

		assertThat(myFilter.isNoise('A')).isFalse();
	}

	@Test
	public void testInvalidChar() {
		String[] invalidPatterns = new String[]{"", "1", "ABC", "\\u21", "#x0001-#x0000"
			, "#x0001 - #x - #x0000", "#x0000 #x0022"};

		for (String i : invalidPatterns) {
			assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
				myFilter.add(i);
			});
		}
	}

}
