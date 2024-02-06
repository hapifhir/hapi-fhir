/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.ISectionResourceSupplier;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.Section;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.thymeleaf.util.Validate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"HttpUrlsUsage"})
public abstract class BaseIpsGenerationStrategy implements IIpsGenerationStrategy {

	public static final String DEFAULT_IPS_NARRATIVES_PROPERTIES =
			"classpath:ca/uhn/fhir/jpa/ips/narrative/ips-narratives.properties";
	private final List<Section> mySections = new ArrayList<>();
	private final Map<Section, ISectionResourceSupplier> mySectionToResourceSupplier = new HashMap<>();

	/**
	 * Constructor
	 */
	public BaseIpsGenerationStrategy() {
		super();
	}

	@Override
	public String getBundleProfile() {
		return "http://hl7.org/fhir/uv/ips/StructureDefinition/Bundle-uv-ips";
	}

	@Nonnull
	@Override
	public final List<Section> getSections() {
		return Collections.unmodifiableList(mySections);
	}

	@Nonnull
	@Override
	public ISectionResourceSupplier getSectionResourceSupplier(@Nonnull Section theSection) {
		return mySectionToResourceSupplier.get(theSection);
	}

	/**
	 * This should be called once per section to add a section for inclusion in generated IPS documents.
	 * It should include a {@link Section} which contains static details about the section, and a {@link ISectionResourceSupplier}
	 * which is used to fetch resources for inclusion at runtime.
	 *
	 * @param theSection Contains static details about the section, such as the resource types it can contain, and a title.
	 * @param theSectionResourceSupplier The strategy object which will be used to supply content for this section at runtime.
	 */
	public void addSection(Section theSection, ISectionResourceSupplier theSectionResourceSupplier) {
		Validate.notNull(theSection, "theSection must not be null");
		Validate.notNull(theSectionResourceSupplier, "theSectionResourceSupplier must not be null");
		Validate.isTrue(
				!mySectionToResourceSupplier.containsKey(theSection),
				"A section with the given profile already exists");

		mySections.add(theSection);
		mySectionToResourceSupplier.put(theSection, theSectionResourceSupplier);
	}

	@Override
	public List<String> getNarrativePropertyFiles() {
		return Lists.newArrayList(DEFAULT_IPS_NARRATIVES_PROPERTIES);
	}

	@Override
	public IBaseResource createAuthor() {
		Organization organization = new Organization();
		organization
				.setName("eHealthLab - University of Cyprus")
				.addAddress(new Address()
						.addLine("1 University Avenue")
						.setCity("Nicosia")
						.setPostalCode("2109")
						.setCountry("CY"))
				.setId(IdType.newRandomUuid());
		return organization;
	}

	@Override
	public String createTitle(IpsContext theContext) {
		return "Patient Summary as of "
				+ DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now());
	}

	@Override
	public String createConfidentiality(IpsContext theIpsContext) {
		return Composition.DocumentConfidentiality.N.toCode();
	}

	@Override
	public IIdType massageResourceId(@Nullable IpsContext theIpsContext, @Nonnull IBaseResource theResource) {
		return null;
	}
}
