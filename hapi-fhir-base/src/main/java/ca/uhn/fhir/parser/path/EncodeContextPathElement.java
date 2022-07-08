package ca.uhn.fhir.parser.path;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EncodeContextPathElement {
    private final String myName;
    private final boolean myResource;

    public EncodeContextPathElement(String theName, boolean theResource) {
        Validate.notBlank(theName);
        myName = theName;
        myResource = theResource;
    }


    public boolean matches(EncodeContextPathElement theOther) {
        if (myResource != theOther.isResource()) {
            return false;
        }
        String otherName = theOther.getName();
        if (myName.equals(otherName)) {
            return true;
        }
        /*
         * This is here to handle situations where a path like
         *    Observation.valueQuantity has been specified as an include/exclude path,
         * since we only know that path as
         *    Observation.value
         * until we get to actually looking at the values there.
         */
        if (myName.length() > otherName.length() && myName.startsWith(otherName)) {
            char ch = myName.charAt(otherName.length());
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return myName.equals("*") || otherName.equals("*");
    }

    @Override
    public boolean equals(Object theO) {
        if (this == theO) {
            return true;
        }

        if (theO == null || getClass() != theO.getClass()) {
            return false;
        }

        EncodeContextPathElement that = (EncodeContextPathElement) theO;

        return new EqualsBuilder()
            .append(myResource, that.myResource)
            .append(myName, that.myName)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(myName)
            .append(myResource)
            .toHashCode();
    }

    @Override
    public String toString() {
        if (myResource) {
            return myName + "(res)";
        }
        return myName;
    }

    public String getName() {
        return myName;
    }

    public boolean isResource() {
        return myResource;
    }
}
