package ca.uhn.fhir.rest.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.BaseMethodBinding.IRequestReader;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ServletRequestDetails extends RequestDetails {

	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;
    /**
     * @see BaseMethodBinding#loadRequestContents(RequestDetails)
     */
    private static volatile IRequestReader ourRequestReader;
    private byte[] requestContents;
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletRequestDetails.class);	
	private RestfulServer myServer;
	
	public ServletRequestDetails() {
	    super();
	    setResponse(new ServletRestfulResponse(this));
	}
	
    public RestfulServer getServer() {
        return myServer;
    }

    public void setServer(RestfulServer theServer) {
        this.myServer = theServer;
    }
	

    public static RequestDetails withResourceAndParams(String theResourceName, RequestTypeEnum theRequestType, Set<String> theParamNames) {
        RequestDetails retVal = new ServletRequestDetails();
        retVal.setResourceName(theResourceName);
        retVal.setRequestType(theRequestType);
        Map<String, String[]> paramNames = new HashMap<String, String[]>();
        for (String next : theParamNames) {
            paramNames.put(next, new String[0]);
        }
        retVal.setParameters(paramNames);
        return retVal;
    }

    @Override
    public String getHeader(String name) {
        return getServletRequest().getHeader(name);
    }
    
    @Override
    public List<String> getHeaders(String name) {
        Enumeration<String> headers = getServletRequest().getHeaders(name);
        return headers == null ? Collections.<String>emptyList() : Collections.list(getServletRequest().getHeaders(name));
    }

    public HttpServletRequest getServletRequest() {
        return myServletRequest;
    }

    public void setServletRequest(HttpServletRequest myServletRequest) {
        this.myServletRequest = myServletRequest;
    }

    public HttpServletResponse getServletResponse() {
        return myServletResponse;
    }

    public void setServletResponse(HttpServletResponse myServletResponse) {
        this.myServletResponse = myServletResponse;
    }

    @Override
    public Reader getReader() throws IOException {
        return getServletRequest().getReader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getServletRequest().getInputStream();
    }

    @Override
    public String getServerBaseForRequest() {
        return getServer().getServerBaseForRequest(getServletRequest());
    }

    protected byte[] getByteStreamRequestContents() {
        /*
         * This is weird, but this class is used both in clients and in servers, and we want to avoid needing to depend on servlet-api in clients since there is no point. So we dynamically load a class
         * that does the servlet processing in servers. Down the road it may make sense to just split the method binding classes into server and client versions, but this isn't actually a huge deal I
         * don't think.
         */
        IRequestReader reader = ourRequestReader;
        if (reader == null) {
            try {
                Class.forName("javax.servlet.ServletInputStream");
                String className = BaseMethodBinding.class.getName() + "$" + "ActiveRequestReader";
                try {
                    reader = (IRequestReader) Class.forName(className).newInstance();
                } catch (Exception e1) {
                    throw new ConfigurationException("Failed to instantiate class " + className, e1);
                }
            } catch (ClassNotFoundException e) {
                String className = BaseMethodBinding.class.getName() + "$" + "InactiveRequestReader";
                try {
                    reader = (IRequestReader) Class.forName(className).newInstance();
                } catch (Exception e1) {
                    throw new ConfigurationException("Failed to instantiate class " + className, e1);
                }
            }
            ourRequestReader = reader;
        }

        try {
            InputStream inputStream = reader.getInputStream(this);
            requestContents = IOUtils.toByteArray(inputStream);
            return requestContents;
        }
        catch (IOException e) {
            ourLog.error("Could not load request resource", e);
            throw new InvalidRequestException(String.format("Could not load request resource: %s", e.getMessage()));
        }
    }

}
