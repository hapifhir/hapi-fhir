package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBase;

import static com.google.common.base.Preconditions.checkNotNull;

public class FhirVersions {

    private FhirVersions() {}

    /**
     * Returns a FhirVersionEnum for a given BaseType
     *
     * @param <BaseType> an IBase type
     * @param baseTypeClass the class of the resource to get the version for
     * @return the FhirVersionEnum corresponding to the baseTypeClass
     */
    public static <BaseType extends IBase> FhirVersionEnum forClass(final Class<? extends BaseType> baseTypeClass) {
        checkNotNull(baseTypeClass);

        String packageName = baseTypeClass.getPackage().getName();
        if (packageName.contains("r5")) {
            return FhirVersionEnum.R5;
        } else if (packageName.contains("r4")) {
            return FhirVersionEnum.R4;
        } else if (packageName.contains("dstu3")) {
            return FhirVersionEnum.DSTU3;
        } else if (packageName.contains("dstu2016may")) {
            return FhirVersionEnum.DSTU2_1;
        } else if (packageName.contains("org.hl7.fhir.dstu2")) {
            return FhirVersionEnum.DSTU2_HL7ORG;
        } else if (packageName.contains("ca.uhn.fhir.model.dstu2")) {
            return FhirVersionEnum.DSTU2;
        } else {
            throw new IllegalArgumentException(
                    "Unable to determine FHIR version for IBaseResource type: %s".formatted(baseTypeClass.getName()));
        }
    }
}
