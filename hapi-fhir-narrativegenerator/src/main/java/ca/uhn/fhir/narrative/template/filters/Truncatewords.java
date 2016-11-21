package ca.uhn.fhir.narrative.template.filters;

class Truncatewords extends Filter {

    /*
     * truncatewords(input, words = 15, truncate_string = "...")
     *
     * Truncate a string down to x words
     */
    @Override
    public Object apply(Object value, Object... params) {

        if (value == null) {
            return "";
        }

        String text = super.asString(value);
        String[] words = text.split("\\s++");
        int length = 15;
        String truncateString = "...";

        if (params.length >= 1) {
            length = super.asNumber(super.get(0, params)).intValue();
        }

        if (params.length >= 2) {
            truncateString = super.asString(super.get(1, params));
        }

        if (length >= words.length) {
            return text;
        }

        return join(words, length) + truncateString;
    }

    private String join(String[] words, int length) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(words[i]).append(" ");
        }

        return builder.toString().trim();
    }
}
