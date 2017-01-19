package ca.uhn.fhir.narrative.template.filters;

class Upcase extends Filter {

    /*
     * upcase(input)
     *
     * convert a input string to UPCASE
     */
    @Override
    public Object apply(Object value, Object... params) {

        return super.asString(value).toUpperCase();
    }
}
