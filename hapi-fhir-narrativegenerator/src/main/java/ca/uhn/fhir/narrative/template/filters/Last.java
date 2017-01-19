package ca.uhn.fhir.narrative.template.filters;

class Last extends Filter {

    /*
     * last(array)
     *
     * Get the last element of the passed in array
     */
    @Override
    public Object apply(Object value, Object... params) {

        Object[] array = super.asArray(value);

        return array.length == 0 ? null : super.asString(array[array.length - 1]);
    }
}
