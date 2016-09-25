package ca.uhn.fhir.narrative.template.filters;

class First extends Filter {

    /*
     * first(array)
     *
     * Get the first element of the passed in array
     */
    @Override
    public Object apply(Object value, Object... params) {

        Object[] array = super.asArray(value);

        return array.length == 0 ? null : super.asString(array[0]);
    }
}
