package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestEntry;
import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.codesystems.FHIRVersion;

public class ConvertorHelper {

    /**
     * Converts the passed in {@link IBaseResource} to the related r4 version.
     *
     * @param resource {@link IBaseResource} to convert to the equivalent r4 version.
     * @param entry    {@link TestEntry} for the resource. The resource version is stored in this.
     * @return The r4 version of the passes in {@link IBaseResource}
     */
    public static IBaseResource getR4Version(IBaseResource resource, TestEntry entry) {
        String version = entry.getVersion();

        if (version == null || version.equals("4.0")) version = "4.0.0";
        if (version.equals("3.0")) version = "3.0.0";

        switch (FHIRVersion.fromCode(version)) {
            case _0_01:
            case _0_0_80:
            case _0_0_81:
            case _0_0_82:
                // DSTU 1
                throw null;//new IllegalStateException("DSTU1 structure passed in for validation.");
            case _0_4_0:
            case _0_5_0:
            case _0_05:
            case _0_06:
            case _0_11:
            case _1_0_0:
            case _1_0_1:
                return null;//VersionConvertor_10_40.convertResource((org.hl7.fhir.dstu2.model.Resource) resource);
            case _1_0_2:
            case _1_1_0:
            case _1_4_0:
            case _1_6_0:
            case _1_8_0:
                return null;//VersionConvertor_30_40.convertResource((org.hl7.fhir.dstu3.model.Resource) resource, false);
            case _3_0_0:
            case _3_0_1:
            case _3_3_0:
            case _3_5_0:
            case _4_0_0:
                return resource;
            case NULL:
            default:
                return null;
        }
    }
}
//only keep the version of the current