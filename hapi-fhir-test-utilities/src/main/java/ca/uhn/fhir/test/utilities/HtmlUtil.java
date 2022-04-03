package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.neko.HtmlUnitNekoHtmlParser;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.net.URL;

public class HtmlUtil {

	private HtmlUtil() {
	}

	public static HtmlPage parseAsHtml(String theRespString, URL theUrl) throws IOException {
		StringWebResponse response = new StringWebResponse(theRespString, theUrl);
		WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED, false, null, -1);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		final HtmlPage page = new HtmlPage(response, client.getCurrentWindow());
		HtmlUnitNekoHtmlParser htmlUnitNekoHtmlParser = new HtmlUnitNekoHtmlParser();
		htmlUnitNekoHtmlParser.parse(response, page, false, false);
		return page;
	}

	public static HtmlForm waitForForm(HtmlPage thePage, String theName) {
		return Awaitility.await().until(() -> thePage.getFormByName(theName), t -> t != null);
	}

	public static HtmlInput waitForInput(HtmlForm theForm, String theName) {
		return Awaitility.await().until(() -> theForm.getInputByName(theName), t -> t != null);
	}
}
