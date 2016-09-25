package ca.uhn.fhir.narrative.template.filters;

class Capitalize extends Filter {

    /*
     * (Object) capitalize(input)
     *
     * capitalize words in the input sentence
     */
    @Override
    public Object apply(Object value, Object... params) {

        String original = super.asString(value);

        if (original.isEmpty()) {
            return original;
        }

        char first = original.charAt(0);

        return Character.toUpperCase(first) + original.substring(1);
    }
}
