package ca.uhn.fhir.cql.r4.helper;

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

import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.r4.model.CanonicalType;

public class CanonicalHelper {

    public static String getId(CanonicalType canonical) {
        if (canonical.hasValue()) {
            String id = canonical.getValue();
            String temp = id.contains("/") ? id.substring(id.lastIndexOf("/") + 1) : id;
            return temp.split("\\|")[0];
        }

        throw new RuntimeException(Msg.code(1675) + "CanonicalType must have a value for id extraction");
    }

    public static String getResourceName(CanonicalType canonical) {
        if (canonical.hasValue()) {
            String id = canonical.getValue();
            if (id.contains("/")) {
                id = id.replace(id.substring(id.lastIndexOf("/")), "");
                return id.contains("/") ? id.substring(id.lastIndexOf("/") + 1) : id;
            }
            return null;
        }

        throw new RuntimeException(Msg.code(1676) + "CanonicalType must have a value for resource name extraction");
    }
}
