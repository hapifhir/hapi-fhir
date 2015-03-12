package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import org.hl7.fhir.instance.model.IBaseResource;

import java.util.Set;

/**
 * Created by Bill de Beaubien on 2/26/2015.
 */
public class ResourceReferenceInfo {
    private String myOwningResource;
    private String myName;
    private BaseResourceReferenceDt myResource;

    public ResourceReferenceInfo(IBaseResource theOwningResource, String theName, BaseResourceReferenceDt theResource) {
        myOwningResource = theOwningResource.getClass().getAnnotation(ResourceDef.class).name();
        myName = theName;
        myResource = theResource;
    }

    public String getName() {
        return myName;
    }

    public BaseResourceReferenceDt getResourceReference() {
        return myResource;
    }

    public boolean matchesIncludeSet(Set<Include> theIncludes) {
        if (theIncludes == null)
            return false;
        for (Include include : theIncludes) {
            if (matchesInclude(include))
                return true;
        }
        return false;
    }

    public boolean matchesInclude(Include theInclude) {
        if (theInclude.getValue().equals("*"))
            return true;
        return (theInclude.getValue().equals(myOwningResource + "." + myName));
    }
}
