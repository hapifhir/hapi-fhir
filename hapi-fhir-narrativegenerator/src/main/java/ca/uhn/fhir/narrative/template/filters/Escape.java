package ca.uhn.fhir.narrative.template.filters;

class Escape extends Filter {

    /*
     * escape(input)
     *
     * escape a string
     */
    @Override
    public Object apply(Object value, Object... params) {

        String str = super.asString(value);

        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
