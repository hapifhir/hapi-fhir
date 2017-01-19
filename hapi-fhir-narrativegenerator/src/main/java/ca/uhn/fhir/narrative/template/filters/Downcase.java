package ca.uhn.fhir.narrative.template.filters;

class Downcase extends Filter {

    /*
     * downcase(input)
     *
     * convert a input string to DOWNCASE
     */
    @Override
    public Object apply(Object value, Object... params) {

        return super.asString(value).toLowerCase();
    }
}
