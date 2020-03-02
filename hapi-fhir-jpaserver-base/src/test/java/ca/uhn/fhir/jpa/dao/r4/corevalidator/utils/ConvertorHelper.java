package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestEntry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.codesystems.FHIRVersion;

public class ConvertorHelper {

    private static final String FOUR_POINT_0 = "4.0";
    private static final String THREE_POINT_0 = "3.0";
    private static final String ONE_POINT_0 = "1.0";

    /**
     * Returns true, if the provided version in the {@link TestEntry} is the correct version.
     *
     * @param entry {@link TestEntry} for the resource. The resource version is stored in this.
     * @return The r4 version of the passes in {@link IBaseResource}
     */
    public static boolean shouldTest(TestEntry entry) {
        String version = cleanVersionString(entry.getVersion());
        switch (FHIRVersion.fromCode(version)) {
            case _3_0_0:
            case _3_0_1:
            case _3_3_0:
            case _3_5_0:
            case _4_0_0:
                return true;
            case NULL:
            default:
                return false;
        }
    }

    /**
     * Some of the versions in the manifest.xml file for tests are not standard FHIR version numbers. This method
     * converts those version codes to the corresponding {@link FHIRVersion}.
     *
     * @param version {@link String} version from the {@link TestEntry}
     * @return updated version corresponding to one of {@link FHIRVersion}
     */
    public static String cleanVersionString(String version) {
        if (version == null) return FHIRVersion._4_0_0.toCode();
        switch (version) {
            case ONE_POINT_0:
                return FHIRVersion._1_0_0.toCode();
            case THREE_POINT_0:
                return FHIRVersion._3_0_0.toCode();
            case FOUR_POINT_0:
                return FHIRVersion._4_0_0.toCode();
            default:
                return version;
        }
    }

}
