/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
	public static String sanitizeHtmlFragment(String theHtml) {
		PolicyFactory idPolicy =
				new HtmlPolicyBuilder().allowAttributes("id").globally().toFactory();

		PolicyFactory policy = Sanitizers.FORMATTING
				.and(Sanitizers.BLOCKS)
				.and(Sanitizers.TABLES)
				.and(Sanitizers.STYLES)
				.and(idPolicy);
		return policy.sanitize(theHtml);
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

		String safeHTML = sanitizeHtmlFragment(html);

		XhtmlNode retVal = new XhtmlNode();
		retVal.setValueAsString(safeHTML);
		return retVal;
	}
}
