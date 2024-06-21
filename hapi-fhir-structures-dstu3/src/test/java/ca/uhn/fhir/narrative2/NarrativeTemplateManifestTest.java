package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NarrativeTemplateManifestTest {
	private static final Logger ourLog = LoggerFactory.getLogger(NarrativeTemplateManifestTest.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@Test
	public void getTemplateByResourceName_NoProfile() throws IOException {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Collections.emptyList());
		ourLog.info("Templates: {}", template);
		assertThat(template).hasSize(3);
		assertThat(template.get(0).getTemplateText()).contains("template3");
		assertThat(template.get(1).getTemplateText()).contains("template2");
		assertThat(template.get(2).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByResourceName_ByProfile_ExactMatch() throws IOException {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile1"));
		assertThat(template).hasSize(1);
		assertThat(template.get(0).getTemplateText()).contains("template1");
	}

	@Test
	public void getTemplateByResourceName_ByProfile_NoMatch() throws IOException {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation("classpath:manifest/manifest-test.properties");
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile99"));
		assertThat(template).isEmpty();
	}

	@Test
	public void getTemplateByResourceName_WithFallback_ByProfile_ExactMatch() throws IOException {
		INarrativeTemplateManifest manifest = NarrativeTemplateManifest.forManifestFileLocation(
			"classpath:manifest/manifest2-test.properties",
			"classpath:manifest/manifest-test.properties"
		);
		List<INarrativeTemplate> template = manifest.getTemplateByResourceName(
			ourCtx,
			EnumSet.of(TemplateTypeEnum.THYMELEAF),
			"Bundle",
			Lists.newArrayList("http://profile1"));
		assertThat(template).hasSize(2);
		assertThat(template.get(0).getTemplateText()).contains("template2-1");
		assertThat(template.get(1).getTemplateText()).contains("template1");
	}


	@Test
	public void getTemplateByFragment() throws IOException {
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

}
