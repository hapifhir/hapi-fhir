package ca.uhn.fhir.cql.common.helper;

import org.apache.commons.lang3.tuple.Triple;
import org.cqframework.cql.elm.execution.Library.Usings;
import org.cqframework.cql.elm.execution.UsingDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsingHelper {

    private static Map<String, String> urlsByModelName = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("FHIR", "http://hl7.org/fhir");
            put("QDM", "urn:healthit-gov:qdm:v5_4");
        }
    };

    // Returns a list of (Model, Version, Url) for the usings in library. The
    // "System" using is excluded.
    public static List<Triple<String, String, String>> getUsingUrlAndVersion(Usings usings) {
        if (usings == null || usings.getDef() == null) {
            return Collections.emptyList();
        }

        List<Triple<String, String, String>> usingDefs = new ArrayList<>();
        for (UsingDef def : usings.getDef()) {

            if (def.getLocalIdentifier().equals("System"))
                continue;

            usingDefs.add(Triple.of(def.getLocalIdentifier(), def.getVersion(),
                    urlsByModelName.get(def.getLocalIdentifier())));
        }

        return usingDefs;
    }
}
