package ca.uhn.fhir.narrative.template;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.narrative.template.nodes.AtomNode;

/**
 * An abstract class the Filter and Tag classes extend.
 * <p/>
 * It houses some utility methods easily available for said
 * classes.
 */
public abstract class LValue {

    /**
     * Returns true iff a and b are equals, where (int) 1 is
     * equals to (double) 1.0
     *
     * @param a
     *         the first object to compare.
     * @param b
     *         the second object to compare.
     *
     * @return true iff a and b are equals, where (int) 1 is
     *         equals to (double) 1.0
     */
    public static boolean areEqual(Object a, Object b) {

        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        // TODO refactor the instance-ofs below

        if (a instanceof Number && b instanceof Number) {

            double delta = ((Number) a).doubleValue() - ((Number) b).doubleValue();

            // To account for floating point rounding errors, return true if
            // the difference between double a and double b is very small.
            return Math.abs(delta) < 0.00000000001;
        }

        if (AtomNode.isEmpty(a) && (b instanceof CharSequence)) {
            return ((CharSequence)b).length() == 0;
        }

        if (AtomNode.isEmpty(b) && (a instanceof CharSequence)) {
            return ((CharSequence)a).length() == 0;
        }

        if (AtomNode.isEmpty(a) && (b instanceof Collection)) {
            return ((Collection)b).size() == 0;
        }

        if (AtomNode.isEmpty(b) && (a instanceof Collection)) {
            return ((Collection)a).size() == 0;
        }

        if (AtomNode.isEmpty(a) && (b.getClass().isArray())) {
            return ((Object[])b).length == 0;
        }

        if (AtomNode.isEmpty(b) && (a.getClass().isArray())) {
            return ((Object[])a).length == 0;
        }

        return a.equals(b);
    }

    /**
     * Returns this value as an array. If a value is already an array,
     * it is casted to a `Object[]`, if it's a `java.util.List`, it is
     * converted to an array and in all other cases, `value` is simply
     * returned as an `Object[]` with a single value in it.
     *
     * @param value
     *         the value to convert/cast to an array.
     *
     * @return this value as an array.
     */
    public Object[] asArray(Object value) {

        if(value == null) {
            return null;
        }

        if (value.getClass().isArray()) {
            return (Object[]) value;
        }

        if (value instanceof List) {
            return ((List) value).toArray();
        }

        return new Object[]{value};
    }

    /**
     * Convert `value` to a boolean. Note that only `nil` and `false`
     * are `false`, all other values are `true`.
     *
     * @param value
     *         the value to convert.
     *
     * @return `value` as a boolean.
     */
    public boolean asBoolean(Object value) {

        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        return true;
    }

    /**
     * Returns `value` as a Number. Strings will be coerced into
     * either a Long or Double.
     *
     * @param value
     *         the value to cast to a Number.
     *
     * @return `value` as a Number.
     *
     * @throws NumberFormatException when `value` is a String which could
     *                               not be parsed as a Long or Double.
     */
    public Number asNumber(Object value) throws NumberFormatException {

        if(value instanceof Number) {
            return (Number) value;
        }

        String str = String.valueOf(value);

        return str.matches("\\d+") ? Long.valueOf(str) : Double.valueOf(str);
    }

    /**
     * Returns `value` as a String.
     *
     * @param value
     *         the value to convert to a String.
     *
     * @return `value` as a String.
     */
    public String asString(Object value) {

        if (value == null) {
            return "";
        }

        if (!this.isArray(value)) {
            return String.valueOf(value);
        }

        Object[] array = this.asArray(value);

        StringBuilder builder = new StringBuilder();

        for (Object obj : array) {
            builder.append(this.asString(obj));
        }

        return builder.toString();
    }

    /**
     * Returns true iff `value` is an array or a java.util.List.
     *
     * @param value
     *         the value to check.
     *
     * @return true iff `value` is an array or a java.util.List.
     */
    public boolean isArray(Object value) {

        return value != null && (value.getClass().isArray() || value instanceof List);
    }

    /**
     * Returns true iff `value` is a whole number (Integer or Long).
     *
     * @param value
     *         the value to check.
     *
     * @return true iff `value` is a whole number (Integer or Long).
     */
    public boolean isInteger(Object value) {
        return value != null && (value instanceof Long || value instanceof Integer);
    }

    /**
     * Returns true iff `value` is a Number.
     *
     * @param value
     *         the value to check.
     *
     * @return true iff `value` is a Number.
     */
    public boolean isNumber(Object value) {

        if(value == null) {
            return false;
        }

        if(value instanceof Number) {
            return true;
        }

        // valid Long?
        if(String.valueOf(value).matches("\\d+")) {
            return true;
        }

        try {
            // valid Double?
            Double.parseDouble(String.valueOf(value));
        } catch(Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Returns true iff `value` is a String.
     *
     * @param value
     *         the value to check.
     *
     * @return true iff `value` is a String.
     */
    public boolean isString(Object value) {

        return value != null && value instanceof CharSequence;
    }
}
