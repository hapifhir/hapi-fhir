package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.dao.lastn.util.CodeSystemHash;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Entity
@Embeddable
@Table(name = "HFJ_SPIDX_LASTN_CODING")
public class ObservationIndexedCodeCodingEntity {

	// TODO: Fix this to allow multiple codings for observation code
//    @Id
//    @SequenceGenerator(name = "SEQ_CODING_FIELD", sequenceName = "SEQ_CODING_FIELD")
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CODING_FIELD")
//    private Long myId;

	@Id
    @Column(name="CODEABLE_CONCEPT_ID")
    private String myCodeableConceptId;

    @Field (name = "code", analyze = Analyze.NO)
    private String myCode;

    @Field (name = "system", analyze = Analyze.NO)
    private String mySystem;

    @Field (name = "code_system_hash", analyze = Analyze.NO)
    private String myCodeSystemHash;

    @Field (name = "display")
    private String myDisplay;

    public ObservationIndexedCodeCodingEntity() {}

    public ObservationIndexedCodeCodingEntity(String theSystem, String theCode, String theDisplay, String theCodeableConceptId) {
        myCode = theCode;
        mySystem = theSystem;
        myCodeSystemHash = String.valueOf(CodeSystemHash.hashCodeSystem(theSystem, theCode));
        myDisplay = theDisplay;
        myCodeableConceptId = theCodeableConceptId;
    }

}
