package ca.uhn.fhir.context.phonetic;

import ca.uhn.fhir.util.PhoneticEncoderUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PhoneticEncoderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(PhoneticEncoderTest.class);

	private static final String NUMBER = "123";
	private static final String STREET = "Nohili St, Suite";
	private static final String SUITE = "456";
	private static final String ADDRESS_LINE = NUMBER + " " + STREET + " " + SUITE;

	@ParameterizedTest
	@EnumSource(PhoneticEncoderEnum.class)
	public void testEncodeAddress(PhoneticEncoderEnum thePhoneticEncoderEnum) {
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(thePhoneticEncoderEnum.name());
		assertNotNull(encoder);
		String encoded = encoder.encode(ADDRESS_LINE);
		ourLog.info("{}: {}", thePhoneticEncoderEnum.name(), encoded);
		if (thePhoneticEncoderEnum == PhoneticEncoderEnum.NUMERIC) {
			assertEquals(NUMBER + SUITE, encoded);
		} else {
			assertThat(encoded).startsWith(NUMBER + " ");
			assertThat(encoded).endsWith(" " + SUITE);
		}
	}
}
