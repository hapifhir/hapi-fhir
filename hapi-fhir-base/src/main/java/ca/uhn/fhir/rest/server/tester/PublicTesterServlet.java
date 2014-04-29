package ca.uhn.fhir.rest.server.tester;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.TemplateResolver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.client.api.IBasicClient;

public class PublicTesterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PublicTesterServlet.class);
	private TemplateEngine myTemplateEngine;
	private HashMap<String, String> myStaticResources;
	private String myServerBase;
	private FhirContext myCtx;

	public void setServerBase(String theServerBase) {
		myServerBase = theServerBase;
	}

	public PublicTesterServlet() {
		myStaticResources = new HashMap<String, String>();
		myStaticResources.put("jquery-2.1.0.min.js", "text/javascript");
		myStaticResources.put("PublicTester.js", "text/javascript");
		myStaticResources.put("PublicTester.css", "text/css");
		myStaticResources.put("hapi_fhir_banner.png", "image/png");
		myStaticResources.put("hapi_fhir_banner_right.png", "image/png");
		
		myCtx = new FhirContext();
	}
	
	@Override
	protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		myTemplateEngine.getCacheManager().clearAllCaches();

		ourLog.info("RequestURI: {}", theReq.getPathInfo());
		
		String resName = theReq.getPathInfo().substring(1);
		if (myStaticResources.containsKey(resName)) {
			streamResponse(resName, myStaticResources.get(resName), theResp);
			return;
		}
		
		ConformanceClient client = myCtx.newRestfulClient(ConformanceClient.class, myServerBase);
		Conformance conformance = client.getConformance();
		
		WebContext ctx = new WebContext(theReq, theResp, theReq.getServletContext(), theReq.getLocale());
		ctx.setVariable("conf", conformance);
		ctx.setVariable("base", myServerBase);
        myTemplateEngine.process(theReq.getPathInfo(), ctx, theResp.getWriter());
        
	}

	private interface ConformanceClient extends IBasicClient
	{
		@Metadata
		Conformance getConformance();
	}
	
	@Override
	public void init(ServletConfig theConfig) throws ServletException {
		myTemplateEngine = new TemplateEngine();
		TemplateResolver resolver = new TemplateResolver();
		resolver.setResourceResolver(new ProfileResourceResolver());
		myTemplateEngine.setTemplateResolver(resolver);
		StandardDialect dialect = new StandardDialect();
		myTemplateEngine.setDialect(dialect);
		myTemplateEngine.initialize();
	}

	private final class ProfileResourceResolver implements IResourceResolver {
		@Override
		public String getName() {
			return getClass().getCanonicalName();
		}

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters theTemplateProcessingParameters, String theName) {
			ourLog.info("Loading template: {}", theName);
			if ("/".equals(theName)) {
				return PublicTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/PublicTester.html");
			}

			return null; 
		}
	}

	private void streamResponse(String theResourceName, String theContentType, HttpServletResponse theResp) throws IOException {
		InputStream res = PublicTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/" + theResourceName);
		theResp.setContentType(theContentType);
		IOUtils.copy(res, theResp.getOutputStream());
	}

}
