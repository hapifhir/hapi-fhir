package ca.uhn.fhir.narrative.template.filters;

import ca.uhn.fhir.i18n.Msg;
import java.util.regex.Pattern;

class Remove_First extends Filter {

    /*
     * remove_first(input, string)
     *
     * remove the first occurrences of a substring
     */
    @Override
    public Object apply(Object value, Object... params) {

        String original = super.asString(value);

        Object needle = super.get(0, params);

        if (needle == null) {
            throw new RuntimeException(Msg.code(717) + "invalid pattern: " + needle);
        }

        return original.replaceFirst(Pattern.quote(String.valueOf(needle)), "");
    }
}
