package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Resource;

public class VersionTypeConverterDstu3 implements VersionSpecificWorkerContextWrapper.IVersionTypeConverter {
    @Override
    public Resource toCanonical(IBaseResource theNonCanonical) {
        return VersionConvertorFactory_30_50.convertResource((org.hl7.fhir.dstu3.model.Resource) theNonCanonical, new BaseAdvisor_30_50(false));
    }

    @Override
    public IBaseResource fromCanonical(Resource theCanonical) {
        return VersionConvertorFactory_30_50.convertResource(theCanonical, new BaseAdvisor_30_50(false));
    }
}
