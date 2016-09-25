package ca.uhn.fhir.narrative.template.filters;

import java.util.Arrays;

class Join extends Filter {

    /*
     * join(input, glue = ' ')
     *
     * Join elements of the array with certain character between them
     */
    @Override
    public Object apply(Object value, Object... params) {

        if (value == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Object[] array = super.asArray(value);
        String glue = params.length == 0 ? " " : super.asString(super.get(0, params));

        for (int i = 0; i < array.length; i++) {

            builder.append(super.asString(array[i]));

            if (i < array.length - 1) {
                builder.append(glue);
            }
        }

        return builder.toString();
    }
}
