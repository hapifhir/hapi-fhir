package ca.uhn.fhir.test.utilities.server;

import org.apache.commons.lang3.Validate;

import javax.servlet.http.HttpServlet;

public class HttpServletExtension extends BaseJettyServerExtension<HttpServletExtension> {
	private HttpServlet myServlet;

	public HttpServletExtension withServlet(HttpServlet theServlet) {
		myServlet = theServlet;
		return this;
	}

	@Override
	protected HttpServlet provideServlet() {
		Validate.notNull(myServlet);
		return myServlet;
	}
}
