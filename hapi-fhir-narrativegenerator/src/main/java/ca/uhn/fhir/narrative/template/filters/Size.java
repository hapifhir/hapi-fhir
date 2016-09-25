package ca.uhn.fhir.narrative.template.filters;

class Size extends Filter {

    /*
     * size(input)
     *
     * Return the size of an array or of an string
     */
    @Override
    public Object apply(Object value, Object... params) {

        if (super.isArray(value)) {
            return super.asArray(value).length;
        }

        if (super.isString(value)) {
            return super.asString(value).length();
        }

        if (super.isNumber(value)) {
            // we're only using 64 bit longs, no BigIntegers or the like.
            // So just return 8 (the number of bytes in a long).
            return 8;
        }

        // boolean or nil
        return 0;
    }
}
