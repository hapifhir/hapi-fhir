package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.BaseMethodBinding.IRequestReader;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.param.ResourceParameter.Mode;
import ca.uhn.fhir.rest.param.TransactionParameter.ParamStyle;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class RestfulServerUtil implements IRestfulServerUtil {
    
    /**
     * @see BaseMethodBinding#loadRequestContents(RequestDetails)
     */
    private static volatile IRequestReader ourRequestReader;
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerUtil.class);
    private byte[] requestContents;
    
    @Override
    public Object getResourceParameter(RequestDetails theRequest, Mode myMode, BaseMethodBinding<?> theMethodBinding,
            Class<? extends IBaseResource> myResourceType) {
        switch (myMode) {
        case BODY:
            try {
                return IOUtils.toString(createRequestReader(theRequest));
            } catch (IOException e) {
                // Shouldn't happen since we're reading from a byte array
                throw new InternalErrorException("Failed to load request");
            }            
        case ENCODING:
            return RestfulServerUtils.determineRequestEncoding(theRequest);
        case RESOURCE:
        default:
            return parseResourceFromRequest(theRequest, theMethodBinding, myResourceType);
        }        
    }
    
    
    protected byte[] loadRequestContents(RequestDetails theRequest) {
        if(requestContents != null) {
            return requestContents;
        }
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
            InputStream inputStream = reader.getInputStream(theRequest);
            requestContents = IOUtils.toByteArray(inputStream);
            return requestContents;
        }
        catch (IOException e) {
            ourLog.error("Could not load request resource", e);
            throw new InvalidRequestException(String.format("Could not load request resource: %s", e.getMessage()));
        }
        

    }


    public Reader createRequestReader(RequestDetails theRequest, Charset charset) {
        Reader requestReader = new InputStreamReader(new ByteArrayInputStream(loadRequestContents(theRequest)), charset);
        return requestReader;
    }

    public Reader createRequestReader(RequestDetails theRequest) throws IOException {
        return createRequestReader(theRequest, determineRequestCharset(theRequest));
    }

    public static Charset determineRequestCharset(RequestDetails theRequest) {
        String ct = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_TYPE);

        Charset charset = null;
        if (isNotBlank(ct)) {
            ContentType parsedCt = ContentType.parse(ct);
            charset = parsedCt.getCharset();
        }
        if (charset == null) {
            charset = Charset.forName("UTF-8");
        }
        return charset;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IBaseResource> T loadResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<T> theResourceType) {
        FhirContext ctx = theRequest.getServer().getFhirContext();

        final Charset charset = determineRequestCharset(theRequest);
        Reader requestReader = createRequestReader(theRequest, charset);

        RestOperationTypeEnum restOperationType = theMethodBinding != null ? theMethodBinding.getRestOperationType() : null;

        EncodingEnum encoding = RestfulServerUtils.determineRequestEncodingNoDefault(theRequest);
        if (encoding == null) {
            String ctValue = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_TYPE);
            if (ctValue != null) {
                if (ctValue.startsWith("application/x-www-form-urlencoded")) {
                    String msg = theRequest.getServer().getFhirContext().getLocalizer().getMessage(ResourceParameter.class, "invalidContentTypeInRequest", ctValue, theMethodBinding.getRestOperationType());
                    throw new InvalidRequestException(msg);
                }
            }
            if (isBlank(ctValue)) {
                /*
                 * If the client didn't send a content type, try to guess
                 */
                String body;
                try {
                    body = IOUtils.toString(requestReader);
                } catch (IOException e) {
                    // This shouldn't happen since we're reading from a byte array..
                    throw new InternalErrorException(e);
                }
                encoding = MethodUtil.detectEncodingNoDefault(body);
                if (encoding == null) {
                    String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "noContentTypeInRequest", restOperationType);
                    throw new InvalidRequestException(msg);
                } else {
                    requestReader = new InputStreamReader(new ByteArrayInputStream(loadRequestContents(theRequest)), charset);
                }
            } else {
                String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "invalidContentTypeInRequest", ctValue, restOperationType);
                throw new InvalidRequestException(msg);
            }
        }

        IParser parser = encoding.newParser(ctx);

        T retVal;
        if (theResourceType != null) {
            retVal = parser.parseResource(theResourceType, requestReader);
        } else {
            retVal = (T) parser.parseResource(requestReader);
        }

        if (theRequest.getId() != null && theRequest.getId().hasIdPart()) {
            retVal.setId(theRequest.getId());
        }

        if (theRequest.getServer().getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
            TagList tagList = new TagList();
            for (Enumeration<String> enumeration = theRequest.getServletRequest().getHeaders(Constants.HEADER_CATEGORY); enumeration.hasMoreElements();) {
                String nextTagComplete = enumeration.nextElement();
                MethodUtil.parseTagValue(tagList, nextTagComplete);
            }
            if (tagList.isEmpty() == false) {
                ((IResource) retVal).getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
            }
        }
        return retVal;
    }
    
    @Override
    public IBaseResource parseResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<? extends IBaseResource> theResourceType) {
        IBaseResource retVal;
        if (IBaseBinary.class.isAssignableFrom(theResourceType)) {
            FhirContext ctx = theRequest.getServer().getFhirContext();
            String ct = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_TYPE);
            IBaseBinary binary = (IBaseBinary) ctx.getResourceDefinition("Binary").newInstance();
            binary.setContentType(ct);
            binary.setContent(loadRequestContents(theRequest));

            retVal = binary;
        } else {
            retVal = loadResourceFromRequest(theRequest, theMethodBinding, theResourceType);
        }
        return retVal;
    }


    @Override
    public Object getRequestResource(RequestDetails theRequest, ParamStyle myParamStyle, Class<? extends IBaseResource> myResourceBundleType) {
        // TODO: don't use a default encoding, just fail!
        EncodingEnum encoding = RestfulServerUtils.determineRequestEncoding(theRequest);

        IParser parser = encoding.newParser(theRequest.getServer().getFhirContext());

        Reader reader;
        try {
            reader = createRequestReader(theRequest);
        }
        catch (IOException e) {
            ourLog.error("Could not load request resource", e);
            throw new InvalidRequestException(String.format("Could not load request resource: %s", e.getMessage()));
        }
        
        switch (myParamStyle) {
        case DSTU1_BUNDLE: {
            Bundle bundle;
            bundle = parser.parseBundle(reader);
            return bundle;
        }
        case RESOURCE_LIST: {
            Bundle bundle = parser.parseBundle(reader);
            ArrayList<IResource> resourceList = new ArrayList<IResource>();
            for (BundleEntry next : bundle.getEntries()) {
                if (next.getResource() != null) {
                    resourceList.add(next.getResource());
                }
            }
            return resourceList;
        }
        case RESOURCE_BUNDLE:
            return parser.parseResource(myResourceBundleType, reader);
        }

        throw new IllegalStateException("Unknown type: " + myParamStyle); // should not happen
    }
    
}
