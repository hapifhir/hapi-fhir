package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.Type;

import java.util.Date;

public class AnnotationBuilder extends BaseBuilder<Annotation> {

    public AnnotationBuilder() {
        super(new Annotation());
    }

    // Type is one of the following: Reference or String
    public AnnotationBuilder buildAuthor(Type choice) {
        complexProperty.setAuthor(choice);
        return this;
    }

    public AnnotationBuilder buildTime(Date date) {
        complexProperty.setTime(date);
        return this;
    }

    // required
    public AnnotationBuilder buildText(String text) {
        complexProperty.setText(text);
        return this;
    }
}
