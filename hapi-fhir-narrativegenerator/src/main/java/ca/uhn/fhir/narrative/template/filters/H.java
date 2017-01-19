package ca.uhn.fhir.narrative.template.filters;

class H extends Filter {

    /*
     * h(input)
     *
     * Alias for: escape
     */
    @Override
    public Object apply(Object value, Object... params) {

        return Filter.getFilter("escape").apply(value, params);
    }
}
