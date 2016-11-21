package ca.uhn.fhir.narrative.template.filters;

class Strip_Newlines extends Filter {

    /*
     * strip_newlines(input) click to toggle source
     *
     * Remove all newlines from the string
     */
    @Override
    public Object apply(Object value, Object... params) {

        return super.asString(value).replaceAll("[\r\n]++", "");
    }
}
