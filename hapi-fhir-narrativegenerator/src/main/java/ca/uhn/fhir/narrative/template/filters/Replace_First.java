package ca.uhn.fhir.narrative.template.filters;

import ca.uhn.fhir.i18n.Msg;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Replace_First extends Filter {

    /*
     * replace_first(input, string, replacement = '')
     *
     * Replace the first occurrences of a string with another
     */
    @Override
    public Object apply(Object value, Object... params) {

        String original = super.asString(value);

        Object needle = super.get(0, params);
        String replacement = "";

        if (needle == null) {
            throw new RuntimeException(Msg.code(719) + "invalid pattern: " + needle);
        }

        if (params.length >= 2) {

            Object obj = super.get(1, params);

            if (obj == null) {
                throw new RuntimeException(Msg.code(720) + "invalid replacement: " + needle);
            }

            replacement = super.asString(super.get(1, params));
        }

        return original.replaceFirst(Pattern.quote(String.valueOf(needle)),
                Matcher.quoteReplacement(replacement));
    }
}
