package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyModifyingHelperTest {

	private static FhirContext ourContext = FhirContext.forR4();

	@Test
	public void testSetAndGet() {
		Address address = new Address();

		PropertyModifyingHelper helper = new PropertyModifyingHelper(ourContext, address);
		helper.set("line", "line1");
		helper.set("line", "line2");
		helper.set("city", "city");

		address = (Address) helper.getBase();

		assertThat(address.getLine()).hasSize(2);
		assertThat(address.getCity()).isEqualTo("city");
		assertThat(address.getCountry()).isNull();

		helper.setDelimiter(";");
		assertThat(helper.getFields("line", "city")).isEqualTo("line1;line2;city");
		List<String> lines = helper.getMultiple("line");
		assertThat(lines.toString()).isEqualTo("[line1, line2]");
	}

}
