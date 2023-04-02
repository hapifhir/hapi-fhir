package ca.uhn.fhir.rest.server.util;

import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class NarrativeUtil {

	/**
	 * Non instantiable
	 */
	private NarrativeUtil() {
		super();
	}

	/**
	 * This method accepts an Xhtml (generally a narrative) and sanitizes it,
	 * removing unsafe elements. This method leverages the
	 * <a href="https://github.com/OWASP/java-html-sanitizer/blob/master/pom.xml">OWASP Java HTML Sanitizer</a>
	 * to perform this task. The policy allows the following:
	 * <ul>
	 *    <li>Block tags are allowed</li>
	 *    <li>Tables are allowed</li>
	 *    <li>Basic styles are allowed but any styles considered unsafe are removed from the document (e.g. any style declarations that could be used to load external content)</li>
	 *    <li>Attributes considered safe are allowed</li>
	 *    <li>Any links (&lta href="....") are removed although any text inside the link is retained</li>
	 *    <li>All other elements and attributes are removed</li>
	 * </ul>
	 */
	public static String sanitize(String theHtml) {
		XhtmlNode node = new XhtmlNode();
		node.setValueAsString(theHtml);
		return sanitize(node).getValueAsString();
	}

	/**
	 * This method accepts an Xhtml (generally a narrative) and sanitizes it,
	 * removing unsafe elements. This method leverages the
	 * <a href="https://github.com/OWASP/java-html-sanitizer/blob/master/pom.xml">OWASP Java HTML Sanitizer</a>
	 * to perform this task. The policy allows the following:
	 * <ul>
	 *    <li>Block tags are allowed</li>
	 *    <li>Tables are allowed</li>
	 *    <li>Basic styles are allowed but any styles considered unsafe are removed from the document (e.g. any style declarations that could be used to load external content)</li>
	 *    <li>Attributes considered safe are allowed</li>
	 *    <li>Any links (&lta href="....") are removed although any text inside the link is retained</li>
	 *    <li>All other elements and attributes are removed</li>
	 * </ul>
	 */
	public static XhtmlNode sanitize(XhtmlNode theNode) {
		String html = theNode.getValueAsString();

		PolicyFactory idPolicy = new HtmlPolicyBuilder()
			.allowAttributes("id").globally()
			.toFactory();

		PolicyFactory policy = Sanitizers.FORMATTING
			.and(Sanitizers.BLOCKS)
			.and(Sanitizers.TABLES)
			.and(Sanitizers.STYLES)
			.and(idPolicy);
		String safeHTML = policy.sanitize(html);

		XhtmlNode retVal = new XhtmlNode();
		retVal.setValueAsString(safeHTML);
		return retVal;
	}

}


