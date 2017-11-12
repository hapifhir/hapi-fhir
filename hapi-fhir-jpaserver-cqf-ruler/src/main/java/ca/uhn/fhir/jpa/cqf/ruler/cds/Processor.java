package ca.uhn.fhir.jpa.cqf.ruler.cds;

import java.util.List;

public interface Processor {
    List<CdsCard> process();
}
