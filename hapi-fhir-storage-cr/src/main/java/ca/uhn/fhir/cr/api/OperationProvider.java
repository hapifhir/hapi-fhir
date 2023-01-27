package ca.uhn.fhir.cr.api;

/**
 * Marker interface for OperationProviders contributed by plugins
 */
public interface OperationProvider {
	public static final String HTML_DIV_CONTENT = "<div xmlns=\"http://www.w3.org/1999/xhtml\">%s</div>";
	public static final String HTML_PARAGRAPH_CONTENT = "<p>%s</p>";
	public static final String HTML_DIV_PARAGRAPH_CONTENT = String.format(HTML_DIV_CONTENT, HTML_PARAGRAPH_CONTENT);
}
