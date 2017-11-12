package ca.uhn.fhir.jpa.cqf.ruler;

import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.Assert;
import org.junit.Test;
import ca.uhn.fhir.jpa.cqf.ruler.helpers.XlsxToValueSet;

import java.io.IOException;

public class RulerHelperTests {

    @Test
    public void XlsxToValueSetTest() throws IOException {
        String[] args = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/test.xlsx", "-b=1", "-s=3", "-v=4", "-c=5", "-d=6"};
        Bundle bundle = XlsxToValueSet.convertVs(args);
        XlsxToValueSet.main(args);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaAffectedAreasArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-affected-areas.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaAffectedAreasArgs);
        XlsxToValueSet.main(zikaAffectedAreasArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaSignsSymptomsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-virus-signs-symptoms.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaSignsSymptomsArgs);
        XlsxToValueSet.main(zikaSignsSymptomsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaArboSignsSymptomsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-arbovirus-signs-symptoms.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaArboSignsSymptomsArgs);
        XlsxToValueSet.main(zikaArboSignsSymptomsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaVirusTestArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-virus-tests.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaVirusTestArgs);
        XlsxToValueSet.main(zikaVirusTestArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaArbovirusTestArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-arbovirus-tests.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaArbovirusTestArgs);
        XlsxToValueSet.main(zikaArbovirusTestArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaChikungunyaTestsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-chikungunya-tests.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaChikungunyaTestsArgs);
        XlsxToValueSet.main(zikaChikungunyaTestsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaDengueTestsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-dengue-tests.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaDengueTestsArgs);
        XlsxToValueSet.main(zikaDengueTestsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaIgmELISAResultsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-igm-elisa-results.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaIgmELISAResultsArgs);
        XlsxToValueSet.main(zikaIgmELISAResultsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaNeutralizingAntibodyResultsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-neutralizing-antibody-results.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaNeutralizingAntibodyResultsArgs);
        XlsxToValueSet.main(zikaNeutralizingAntibodyResultsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaArbovirusTestResultsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-arbovirus-test-results.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaArbovirusTestResultsArgs);
        XlsxToValueSet.main(zikaArbovirusTestResultsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaChikungunyaTestResultsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-chikungunya-test-results.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaChikungunyaTestResultsArgs);
        XlsxToValueSet.main(zikaChikungunyaTestResultsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] zikaDengueTestResultsArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-dengue-test-results.xlsx", "-b=1", "-o=9", "-s=10", "-v=7", "-c=0", "-d=2" };
        bundle = XlsxToValueSet.convertVs(zikaDengueTestResultsArgs);
        XlsxToValueSet.main(zikaDengueTestResultsArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        String[] csArgs = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/test.xlsx", "-b=1", "-o=8", "-u=7", "-v=4", "-c=5", "-d=6", "-cs", "-outDir=src/main/resources/codesystems/"};
        bundle = XlsxToValueSet.convertCs(csArgs);
        XlsxToValueSet.main(csArgs);
        Assert.assertTrue(!bundle.getEntry().isEmpty());

        // TODO - fix this
//        String[] csArgsZika = { "src/test/resources/ca/uhn/fhir/jpa/cqf/ruler/zika-codesystem.xlsx", "-b=1", "-cs", "-outDir=src/main/resources/codesystems/"};
//        bundle = XlsxToValueSet.convertCs(csArgsZika);
//        XlsxToValueSet.main(csArgsZika);
//        Assert.assertTrue(!bundle.getEntry().isEmpty());
    }
}
