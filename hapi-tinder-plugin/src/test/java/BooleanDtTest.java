import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanDtTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BooleanDtTest.class);


	/**
	 * This test demonstrates the problem with the tinder-generated ca.uhn.fhir.model.dstu2.composite.CodingDt
	 * described in <a href="https://github.com/hapifhir/hapi-fhir/issues/4540">this issue</a>
	 */
	@Test
	void CodingDt_default_constructor_test() {
		CodingDt codingDt = new CodingDt().setCode("ABC");

		NullPointerException npe = assertThrows(
			NullPointerException.class,
			() -> { boolean userSelected = codingDt.getUserSelected(); }
		);

		ourLog.error("Exception thrown: {}", npe.getMessage());
	}
}
