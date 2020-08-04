package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.convertors.VersionConvertor_14_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Resource;

public class VersionTypeConverterDstu21 implements VersionSpecificWorkerContextWrapper.IVersionTypeConverter {
    @Override
    public Resource toCanonical(IBaseResource theNonCanonical) {
        return VersionConvertor_14_50.convertResource((org.hl7.fhir.dstu2016may.model.Resource) theNonCanonical);
    }

    @Override
    public IBaseResource fromCanonical(Resource theCanonical) {
        return VersionConvertor_14_50.convertResource(theCanonical);
    }
}
