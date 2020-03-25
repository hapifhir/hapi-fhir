package ca.uhn.fhir.jpa.dao.lastn.entity;

import ca.uhn.fhir.jpa.dao.lastn.util.CodeSystemHash;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Embeddable
public class ObservationIndexedCategoryCodingEntity {

    @Field (name = "code", analyze = Analyze.NO)
    private String myCode;
    @Field (name = "system", analyze = Analyze.NO)
    private String mySystem;
    @Field (name = "code_system_hash", analyze = Analyze.NO)
    private String myCodeSystemHash;
    @Field (name = "display")
    private String myDisplay;

    public ObservationIndexedCategoryCodingEntity(String theSystem, String theCode, String theDisplay) {
        myCode = theCode;
        mySystem = theSystem;
        myCodeSystemHash = String.valueOf(CodeSystemHash.hashCodeSystem(theSystem, theCode));
        myDisplay = theDisplay;
    }

}
