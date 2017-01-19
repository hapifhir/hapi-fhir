package ca.uhn.fhir.narrative.template.filters;

class Strip_HTML extends Filter {

    /*
     * strip_html(input)
     *
     * Remove all HTML tags from the string
     */
    @Override
    public Object apply(Object value, Object... params) {
   	 throw new UnsupportedOperationException();
//
//        String html = super.asString(value);
//
//        return Jsoup.parse(html).text();
    }
}
