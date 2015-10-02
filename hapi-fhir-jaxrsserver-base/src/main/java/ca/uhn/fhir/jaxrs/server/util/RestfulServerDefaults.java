package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;

public class RestfulServerDefaults implements IRestfulServerDefaults {

    public boolean isUseBrowserFriendlyContentTypes() {
        return true;
    }

    public ETagSupportEnum getETagSupport() {
        return ETagSupportEnum.DISABLED;
    }

    public AddProfileTagEnum getAddProfileTag() {
        return AddProfileTagEnum.NEVER;
    }

    public boolean isDefaultPrettyPrint() {
        return true;
    }

    public IPagingProvider getPagingProvider() {
        //Integer count = getIntParam("_count");
        Integer count = 0;
        return count == 0 ? null :  new FifoMemoryPagingProvider(count);
    }

    public BundleInclusionRule getBundleInclusionRule() {
        return BundleInclusionRule.BASED_ON_INCLUDES;
    }

}
