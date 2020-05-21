package ca.uhn.fhir.parser.path;

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
