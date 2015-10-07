package ca.uhn.fhir.rest.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ResourceParameter.Mode;
import ca.uhn.fhir.rest.param.TransactionParameter.ParamStyle;

public interface IRestfulServerUtil {

    Object getResourceParameter(
            RequestDetails requestDetails, 
            Mode myMode, 
            BaseMethodBinding<?> theMethodBinding, 
            Class<? extends IBaseResource> myResourceType);

    Object getRequestResource(RequestDetails theRequest, ParamStyle myParamStyle, Class<? extends IBaseResource> myResourceBundleType);

    <T extends IBaseResource> T loadResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<T> theResourceType);

    IBaseResource parseResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<? extends IBaseResource> theResourceType);

}
