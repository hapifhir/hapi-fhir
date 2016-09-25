package ca.uhn.fhir.narrative.template.filters;

import java.util.regex.Pattern;

class Split extends Filter {

    /*
     * split(input, delimiter = ' ')
     *
     * Split a string on a matching pattern
     *
     * E.g. {{ "a~b" | split:'~' | first }} #=> 'a'
     */
    @Override
    public Object apply(Object value, Object... params) {

        String original = super.asString(value);

        String delimiter = super.asString(super.get(0, params));

        return original.split("(?<!^)" + Pattern.quote(delimiter));
    }
}
