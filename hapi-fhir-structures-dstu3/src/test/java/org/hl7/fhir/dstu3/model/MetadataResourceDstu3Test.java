package org.hl7.fhir.dstu3.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MetadataResourceDstu3Test {
    /**
     * Ensuring that fields defined in {@link MetadataResource}, {@link DomainResource}, and {@link Resource}
     * get copied when the {@link MetadataResource#copy()} method is called
     */
    @Test
    public void testCodeSystemCopy() {
        CodeSystem codeSystem1 = new CodeSystem();
        codeSystem1.setId("1001");
        codeSystem1.setName("Name123");
        codeSystem1.setText(new Narrative().setStatus(Narrative.NarrativeStatus.ADDITIONAL));

        CodeSystem copiedCodeSystem = codeSystem1.copy();
        String copiedCodeSystemID = copiedCodeSystem.getId();
        String copiedCodeSystemName = copiedCodeSystem.getName();
        Narrative.NarrativeStatus copiedCodeSystemTextStatus = copiedCodeSystem.getText().getStatus();

        assertTrue(copiedCodeSystem instanceof MetadataResource); // Just making sure this assumption still holds up, otherwise this test isn't very useful
        assertEquals("1001", copiedCodeSystemID);
        assertEquals("Name123", copiedCodeSystemName);
        assertEquals(new Narrative().setStatus(Narrative.NarrativeStatus.ADDITIONAL).getStatus(), copiedCodeSystemTextStatus);
    }
}
