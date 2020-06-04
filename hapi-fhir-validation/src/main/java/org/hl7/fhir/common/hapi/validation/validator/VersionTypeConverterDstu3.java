package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.convertors.VersionConvertor_30_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Resource;

public class VersionTypeConverterDstu3 implements VersionSpecificWorkerContextWrapper.IVersionTypeConverter {
    @Override
    public Resource toCanonical(IBaseResource theNonCanonical) {
        return VersionConvertor_30_50.convertResource((org.hl7.fhir.dstu3.model.Resource) theNonCanonical, true);
    }

    @Override
    public IBaseResource fromCanonical(Resource theCanonical) {
        return VersionConvertor_30_50.convertResource(theCanonical, true);
    }
}
