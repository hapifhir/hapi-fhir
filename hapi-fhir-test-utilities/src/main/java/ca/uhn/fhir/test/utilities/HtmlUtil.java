package ca.uhn.fhir.test.utilities;

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

	public static HtmlPage parseAsHtml(String theRespString, URL theUrl) throws IOException {
		StringWebResponse response = new StringWebResponse(theRespString, theUrl);
		WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED, false, null, -1);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		final HtmlPage page = new HtmlPage(response, client.getCurrentWindow());
		HtmlUnitNekoHtmlParser htmlUnitNekoHtmlParser = new HtmlUnitNekoHtmlParser();
		htmlUnitNekoHtmlParser.parse(response, page, false);
		return page;
	}

	public static HtmlForm waitForForm(HtmlPage thePage, String theName) {
		return Awaitility.await().until(() -> thePage.getFormByName(theName), t -> t != null);
	}

	public static HtmlInput waitForInput(HtmlForm theForm, String theName) {
		return Awaitility.await().until(() -> theForm.getInputByName(theName), t -> t != null);
	}
}
