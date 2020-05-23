package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Resource;

public class VersionTypeConverterR4 implements VersionSpecificWorkerContextWrapper.IVersionTypeConverter {
    @Override
    public Resource toCanonical(IBaseResource theNonCanonical) {
        return VersionConvertor_40_50.convertResource((org.hl7.fhir.r4.model.Resource) theNonCanonical);
    }

    @Override
    public IBaseResource fromCanonical(Resource theCanonical) {
        return VersionConvertor_40_50.convertResource(theCanonical);
    }
}
