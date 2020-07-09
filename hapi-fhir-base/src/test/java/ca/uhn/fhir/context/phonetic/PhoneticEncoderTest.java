package ca.uhn.fhir.context.phonetic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

class PhoneticEncoderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(PhoneticEncoderTest.class);

	private static final String NUMBER = "123";
	private static final String STREET = "Nohili St, Suite";
	private static final String SUITE = "456";
	private static final String ADDRESS_LINE = NUMBER + " " + STREET + " " + SUITE;

	@ParameterizedTest
	@EnumSource(PhoneticEncoderEnum.class)
	public void testEncodeAddress(PhoneticEncoderEnum thePhoneticEncoderEnum) {
		String encoded = thePhoneticEncoderEnum.getPhoneticEncoder().encode(ADDRESS_LINE);
		ourLog.info("{}: {}", thePhoneticEncoderEnum.name(), encoded);
		assertThat(encoded, startsWith(NUMBER + " "));
		assertThat(encoded, endsWith(" " + SUITE));
	}
}
