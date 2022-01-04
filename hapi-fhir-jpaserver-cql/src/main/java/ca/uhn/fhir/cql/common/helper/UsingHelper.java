package ca.uhn.fhir.cql.common.helper;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
