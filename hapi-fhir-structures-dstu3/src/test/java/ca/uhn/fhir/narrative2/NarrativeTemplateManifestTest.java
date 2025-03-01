package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class NarrativeTemplateManifestTest {
	private static final Logger ourLog = LoggerFactory.getLogger(NarrativeTemplateManifestTest.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@Test
	public void getTemplateByResourceName_NoProfile() {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Collections.emptyList(),
			 Collections.emptyList());
		ourLog.info("Templates: {}", template);
		assertThat(template).hasSize(6);
		assertThat(template.get(0).getTemplateText()).contains("template3");
		assertThat(template.get(1).getTemplateText()).contains("template2");
		assertThat(template.get(2).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByResourceName_ByProfile_ExactMatch() {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile1"),
			Collections.emptyList());
		assertThat(template).hasSize(1);
		assertThat(template.get(0).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByResourceName_ByProfile_NoMatch() {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile99"),
			Collections.emptyList());
		assertThat(template).isEmpty();
	}

	@Test
	public void getTemplateByResourceName_WithFallback_ByProfile_ExactMatch() {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(
			"classpath:manifest/manifest2-test.properties",
			"classpath:manifest/manifest-test.properties"
		);
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile1"),
			Collections.emptyList());
		assertThat(template).hasSize(2);
		assertThat(template.get(0).getTemplateText()).contains("template2-1");
		assertThat(template.get(1).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByFragment() {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileContents(
			ClasspathUtil.loadResource("classpath:manifest/fragment-test.properties")
		);
		List<INarrativeTemplate> template = manifest.getTemplateByFragmentName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Foo");
		assertThat(template).hasSize(1);
		assertThat(template.get(0).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByElement_MatchOnLastCode() {
		BundleBuilder bundleBuilder = new BundleBuilder(FhirContext.forDstu3Cached());
		IBaseBundle bundle = bundleBuilder.getBundle();

		bundle.getMeta().addTag().setSystem("http://loinc.org").setCode("12345");
		bundle.getMeta().addTag().setSystem("http://loinc.org").setCode("67890");
		bundle.getMeta().addTag().setSystem("http://loinc.org").setCode("8716-3");

		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(
			 "classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByElement(
			 ourCtx,
			 EnumSet.of(TemplateTypeEnum.THYMELEAF),
			 bundle);

		assertThat(template).hasSize(1);
		assertThat(template.get(0).getTemplateText()).contains("template6");
	}

	@ParameterizedTest
	@MethodSource("getInvalidCodeSystemAndCode")
	public void getTemplateByElement_InvalidCode_GetsIgnored(String theCodeSystem, String theCode) {
		final BundleBuilder bundleBuilder = new BundleBuilder(FhirContext.forDstu3Cached());
		bundleBuilder.setMetaField("tag", new Coding(theCodeSystem, theCode, ""));

		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(
			 "classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByElement(
			 ourCtx,
			 EnumSet.of(TemplateTypeEnum.THYMELEAF),
			 bundleBuilder.getBundle());

		// should return 6 profiles since invalid codes will be filtered
		assertThat(template).hasSize(6);
	}

	@ParameterizedTest
	@MethodSource("getTemplateByElementValues")
	public void getTemplateByElement(int theTemplateListCount, String theSystem,
												String theCode, String theProfile, String theContents) {
		IBaseBundle bundle = buildBundle(theProfile, theSystem, theCode);

		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(
			 "classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByElement(
			 ourCtx,
			 EnumSet.of(TemplateTypeEnum.THYMELEAF),
			 bundle);

		assertThat(template).hasSize(theTemplateListCount);
		for(int i=0; i<theTemplateListCount; i++) {
			assertThat(template.get(i).getTemplateText()).contains(theContents);
		}
	}

	private static Stream<Arguments> getInvalidCodeSystemAndCode() {
		return Stream.of(
			 Arguments.of("http://loinc.org", null),
			 Arguments.of(null, "46240-8"),
			 Arguments.of("", "46240-8"),
			 Arguments.of("http://loinc.org", ""),
			 Arguments.of(null, ""),
			 Arguments.of("", null),
			 Arguments.of("", ""),
			 Arguments.of(null, null)
		);
	}

	private static Stream<Arguments> getTemplateByElementValues() {
		return Stream.of(
			 Arguments.of(2, "http://loinc.org", "46240-8", null, "template"),
			 Arguments.of(1, "http://loinc.org", "46240-8", "http://profile5", "template5"),
			 Arguments.of(0, "http://loinc.org", "INVALID", null, null),
			 Arguments.of(1, null, null, "http://profile1", "template1"),
			 Arguments.of(1, null, null, "http://profile2", "template2"),
			 Arguments.of(0, null, null, "http://INVALID", null),
			 Arguments.of(1, "http://loinc.org", "8716-3", "http://profile6", "template6"),
			 Arguments.of(6, null, null, null, "template")
		);
	}

	private static IBaseBundle buildBundle(String theProfile, String theCodeSystem, String theCode) {
		final BundleBuilder bundleBuilder = new BundleBuilder(FhirContext.forDstu3Cached());

		if (StringUtils.isNotEmpty(theProfile)) {
			bundleBuilder.setMetaField("profile", new StringType().setValue(theProfile));
		}
		if (StringUtils.isNotEmpty(theCodeSystem) && StringUtils.isNotEmpty(theCode)) {
			bundleBuilder.setMetaField("tag",
				 new Coding(theCodeSystem, theCode, ""));
		}
		return bundleBuilder.getBundle();
	}

}
