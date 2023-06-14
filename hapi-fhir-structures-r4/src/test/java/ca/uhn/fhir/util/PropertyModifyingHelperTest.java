package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ca.uhn.fhir.context.FhirContext;
import java.util.List;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.Test;

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

        assertEquals(2, address.getLine().size());
        assertEquals("city", address.getCity());
        assertNull(address.getCountry());

        helper.setDelimiter(";");
        assertEquals("line1;line2;city", helper.getFields("line", "city"));
        List<String> lines = helper.getMultiple("line");
        assertEquals("[line1, line2]", lines.toString());
    }
}
