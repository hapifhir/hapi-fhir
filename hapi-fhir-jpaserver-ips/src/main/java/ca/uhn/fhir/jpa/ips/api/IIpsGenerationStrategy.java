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
package ca.uhn.fhir.jpa.ips.api;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Set;

/**
 * This interface is the primary configuration and strategy provider for the
 * HAPI FHIR International Patient Summary (IPS) generator.
 * <p>
 * Note that this API will almost certainly change as more real-world experience is
 * gained with the IPS generator.
 */
public interface IIpsGenerationStrategy {

	/**
	 * Provides a registry which defines the various sections that will be
	 * included when generating an IPS. It can be subclassed and customized
	 * as needed in order to add, change, or remove sections.
	 */
	SectionRegistry getSectionRegistry();

	/**
	 * Provides a list of configuration property files for the IPS narrative generator.
	 * <p>
	 * Entries should be of the format <code>classpath:path/to/file.properties</code>
	 * </p>
	 * <p>
	 * If more than one file is provided, the files will be evaluated in order. Therefore you
	 * might choose to include a custom file, followed by
	 * {@link ca.uhn.fhir.jpa.ips.strategy.DefaultIpsGenerationStrategy#DEFAULT_IPS_NARRATIVES_PROPERTIES}
	 * in order to fall back to the default templates for any sections you have not
	 * provided an explicit template for.
	 * </p>
	 */
	List<String> getNarrativePropertyFiles();

	/**
	 * Create and return a new <code>Organization</code> resource representing.
	 * the author of the IPS document. This method will be called once per IPS
	 * in order to
	 */
	IBaseResource createAuthor();

	/**
	 * Create and return a title for the composition document.
	 *
	 * @param theContext The associated context for the specific IPS document being generated.
	 */
	String createTitle(IpsContext theContext);

	/**
	 * Create and return a confidentiality code for the composition document. Must be a valid
	 * code for the element <code>Composition.confidentiality</code>
	 *
	 * @param theIpsContext The associated context for the specific IPS document being generated.
	 */
	String createConfidentiality(IpsContext theIpsContext);

	/**
	 * This method is used to determine the resource ID to assign to a resource that
	 * will be added to the IPS document Bundle. Implementations will probably either
	 * return the resource ID as-is, or generate a placeholder UUID to replace it with.
	 *
	 * @param theIpsContext The associated context for the specific IPS document being
	 *                      generated. Note that this will be <code>null</code> when
	 *                      massaging the ID of the subject (Patient) resource, but will
	 *                      be populated for all subsequent calls for a given IPS
	 *                      document generation.
	 * @param theResource   The resource to massage the resource ID for
	 * @return An ID to assign to the resource
	 */
	IIdType massageResourceId(@Nullable IpsContext theIpsContext, @Nonnull IBaseResource theResource);

	/**
	 * For each section and resource type combination defined in the
	 * {@link #getSectionRegistry() Section Registry}, this method will be invoked in order
	 * to determine whether a repository search for data should be performed. If this
	 * method returns <code>true</code>, a repository search will be performed for the
	 * given section context. If you wish to perform a manual fetch using
	 * {@link #fetchResourcesForSectionManually(IpsContext, IpsContext.IpsSectionContext)} you may
	 * choose to return <code>false</code>. Note that
	 * {@link #fetchResourcesForSectionManually(IpsContext, IpsContext.IpsSectionContext)} will still
	 * be called after the repository search even if this method returns <code>true</code>,
	 * so it should ignore any calls it does not wish to return data for.
	 *
	 * @param theSectionContext The section context, containing the section name and resource type.
	 * @since 7.0.0
	 */
	boolean shouldPerformRepositorySearch(IpsContext.IpsSectionContext theSectionContext);

	/**
	 * This method will be called once for each section context (section and resource type combination),
	 * and can be used to fetch resources to include in the given IPS section. This method can
	 * be used if you wish to fetch resources for a given section from a source other than
	 * the repository. This could mean fetching resources using a FHIR REST client to an
	 * external server, or could even mean fetching data directly from a database using JDBC
	 * or similar.
	 * <p>
	 * Note that {@link #shouldInclude(IpsContext.IpsSectionContext, IBaseResource)} will be called
	 * once for each resource returned by this method.
	 * </p>
	 *
	 * @param theIpsContext     The IPS context, containing the identity of the patient whose IPS is being generated.
	 * @param theSectionContext The section context, containing the section name and resource type.
	 * @return Returns a list of resources to add to the given section, or <code>null</code>.
	 * @see #shouldPerformRepositorySearch(IpsContext.IpsSectionContext)
	 * @see #shouldInclude(IpsContext.IpsSectionContext, IBaseResource)
	 * @since 7.0.0
	 */
	@Nullable
	List<IBaseResource> fetchResourcesForSectionManually(
			IpsContext theIpsContext, IpsContext.IpsSectionContext theSectionContext);

	/**
	 * This method can manipulate the {@link SearchParameterMap} that will
	 * be used to find candidate resources for the given IPS section. The map will already have
	 * a subject/patient parameter added to it. The map provided in {@literal theSearchParameterMap}
	 * will contain a subject/patient reference, but no other parameters. This method can add other
	 * parameters.
	 * <p>
	 * This method will only be called if {@link #shouldPerformRepositorySearch(IpsContext.IpsSectionContext)}
	 * returns <code>true</code> for a given section context (i.e. for a given section name and resource type).
	 * </p>
	 * <p>
	 * For example, for a Vital Signs section, the implementation might add a parameter indicating
	 * the parameter <code>category=vital-signs</code>.
	 *
	 * @param theIpsSectionContext  The context, which indicates the IPS section and the resource type
	 *                              being searched for.
	 * @param theSearchParameterMap The map to manipulate.
	 */
	void massageResourceSearch(
			IpsContext.IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap);

	/**
	 * Return a set of Include directives to be added to the resource search
	 * for resources to include for a given IPS section. These include statements will
	 * be added to the same {@link SearchParameterMap} provided to
	 * {@link #massageResourceSearch(IpsContext.IpsSectionContext, SearchParameterMap)}.
	 * This is a separate method in order to make subclassing easier.
	 * <p>
	 * This method will only be called if {@link #shouldPerformRepositorySearch(IpsContext.IpsSectionContext)}
	 * returns <code>true</code> for a given section context (i.e. for a given section name and resource type).
	 * </p>
	 *
	 * @param theIpsSectionContext The context, which indicates the IPS section and the resource type
	 *                             being searched for.
	 * @return Returns a set of <code>_include</code> values to include on the repository
	 * search for a given section context, or <code>null</code>.
	 */
	@Nullable
	Set<Include> provideResourceSearchIncludes(IpsContext.IpsSectionContext theIpsSectionContext);

	/**
	 * This method will be called for each found resource candidate for inclusion in the
	 * IPS document. The strategy can decide whether to include it or not.
	 * <p>
	 * This method is called once for every resource that is being considered for inclusion
	 * in an IPS section, whether it came from a repository search or a manual fetch.
	 * </p>
	 */
	boolean shouldInclude(IpsContext.IpsSectionContext theIpsSectionContext, IBaseResource theCandidate);
}
