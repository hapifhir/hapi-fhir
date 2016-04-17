package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by Bill de Beaubien on 3/3/2015.
 */
public class Dstu1BundleFactoryTest {
    private static FhirContext ourCtx;
    private List<IBaseResource> myResourceList;
    private Dstu1BundleFactory myBundleFactory;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ourCtx = new FhirContext(Patient.class);
    }

    @Before
    public void setUp() throws Exception {
        // DiagnosticReport
        //     Performer(practitioner)
        //     Subject(patient)
        //     Result(observation)
        //         Specimen(specimen1)
        //             Subject(patient)
        //             Collector(practitioner)
        //         Subject(patient)
        //         Performer(practitioner)
        //     Specimen(specimen2)

        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setId("DiagnosticReport/1");

        Observation observation = new Observation();
        observation.setId("Observation/1");

        Specimen specimen1 = new Specimen();
        specimen1.setId("Specimen/1");

        Specimen specimen2 = new Specimen();
        specimen2.setId("Specimen/2");

        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner/1");

        Patient patient = new Patient();
        patient.setId("Patient/1");

        diagnosticReport.setPerformer(new ResourceReferenceDt(practitioner));
        diagnosticReport.setSubject(new ResourceReferenceDt(patient));
        diagnosticReport.setResult(Arrays.asList(new ResourceReferenceDt[]{new ResourceReferenceDt(observation)}));
        diagnosticReport.setSpecimen(Arrays.asList(new ResourceReferenceDt(specimen2)));

        observation.setSpecimen(new ResourceReferenceDt(specimen1));
        observation.setSubject(new ResourceReferenceDt(patient));
        observation.setPerformer(Arrays.asList(new ResourceReferenceDt[]{new ResourceReferenceDt(practitioner)}));

        specimen1.setSubject(new ResourceReferenceDt(patient));
        specimen1.getCollection().setCollector(new ResourceReferenceDt(practitioner));

        myResourceList = Arrays.asList(new IBaseResource[]{diagnosticReport});

        myBundleFactory = new Dstu1BundleFactory(ourCtx);
    }

    @Test
    public void whenMedicationHasIngredients_include_shouldIncludeThem() throws Exception {
        Medication medication = new Medication();
        medication.setName("Main Medication");
        medication.setId("Medication/1");
        Medication ingredientMedication = new Medication();
        ingredientMedication.setName("Ingredient");
        ingredientMedication.setId("Medication/2");
        Medication.ProductIngredient ingredient = new Medication.ProductIngredient();
        ingredient.setItem(new ResourceReferenceDt(ingredientMedication));
        medication.getProduct().getIngredient().add(ingredient);

        myResourceList = Arrays.asList(new IBaseResource[]{medication});
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes("Medication.product.ingredient.item"));
        assertEquals(2, bundle.getEntries().size());
    }

    @Test
    public void whenIncludeIsAsterisk_bundle_shouldContainAllReferencedResources() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes("*"));

        assertEquals(6, bundle.getEntries().size());
        assertEquals(2, numberOfEntriesOfType(bundle, Specimen.class));
    }

    @Test
    public void whenIncludeIsNull_bundle_shouldOnlyContainPrimaryResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, null);
        assertEquals(1, bundle.getEntries().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
    }

    @Test
    public void whenIncludeIsDiagnosticReportSubject_bundle_shouldIncludePatient() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(DiagnosticReport.INCLUDE_SUBJECT.getValue()));

        assertEquals(2, bundle.getEntries().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Patient.class));
    }

    @Test
    public void whenAChainedResourceIsIncludedAndItsParentIsAlsoIncluded_bundle_shouldContainTheChainedResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(DiagnosticReport.INCLUDE_RESULT.getValue(), Observation.INCLUDE_SPECIMEN.getValue()));

        assertEquals(3, bundle.getEntries().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Observation.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Specimen.class));
        List<Specimen> specimens = getResourcesOfType(bundle, Specimen.class);
        assertEquals(1, specimens.size());
        assertEquals("1", specimens.get(0).getId().getIdPart());
    }

    @Test
    public void whenAChainedResourceIsIncludedButItsParentIsNot_bundle_shouldNotContainTheChainedResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(Observation.INCLUDE_SPECIMEN.getValue()));

        assertEquals(1, bundle.getEntries().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
    }

    @Test
    public void whenBundleInclusionRuleSetToResourcePresence_bundle_shouldContainAllResources() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE, null);

        assertEquals(6, bundle.getEntries().size());
        assertEquals(2, numberOfEntriesOfType(bundle, Specimen.class));
    }

    Bundle makeBundle(BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes) {
        myBundleFactory.addResourcesToBundle(myResourceList, null, "http://foo", theBundleInclusionRule, theIncludes);
        return myBundleFactory.getDstu1Bundle();
    }

    private Set<Include> includes(String... includes) {
        Set<Include> includeSet = new HashSet<Include>();
        for (String include : includes) {
            includeSet.add(new Include(include));
        }
        return includeSet;
    }

    private <T extends IResource> int numberOfEntriesOfType(Bundle theBundle, Class<T> theResourceClass) {
        int count = 0;
        for (BundleEntry entry : theBundle.getEntries()) {
            if (theResourceClass.isAssignableFrom(entry.getResource().getClass()))
                count++;
        }
        return count;
    }

    private <T extends IResource> List<T> getResourcesOfType(Bundle theBundle, Class<T> theResourceClass) {
        List<T> resources = new ArrayList<T>();
        for (BundleEntry entry : theBundle.getEntries()) {
            if (theResourceClass.isAssignableFrom(entry.getResource().getClass())) {
                resources.add((T) entry.getResource());
            }
        }
        return resources;
    }

 	@AfterClass
 	public static void afterClassClearContext() {
 		TestUtil.clearAllStaticFieldsForUnitTest();
 	}

}
