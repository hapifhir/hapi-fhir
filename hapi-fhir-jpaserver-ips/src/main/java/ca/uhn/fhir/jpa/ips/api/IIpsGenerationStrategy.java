package ca.uhn.fhir.jpa.ips.api;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface IIpsGenerationStrategy {

	/**
	 * Provides a registry of the IPS sections.
	 */
	SectionRegistry getSectionRegistry();

	/**
	 * Provides a list of configuration property files for the IPS narrative generator
	 */
	List<String> getNarrativePropertyFiles();

	/**
	 * Create and return a new <code>Organization</code> resource representing.
	 * the author of the IPS document.
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
	 * This method can manipulate the {@link SearchParameterMap} that will
	 * be used to find candidate resources for the given IPS section. The map will already have
	 * a subject/patient parameter added to it. The map provided in {@literal theSearchParameterMap}
	 * will contain a subject/patient reference, but no other parameters. This method can add other
	 * parameters.
	 * <p>
	 * For example, for a Vital Signs section, the implementation might add a parameter indicating
	 * the parameter <code>category=vital-signs</code>.
	 *
	 * @param theIpsSectionContext  The context, which indicates the IPS section and the resource type
	 *                              being searched for.
	 * @param theSearchParameterMap The map to manipulate.
	 */
	void massageResourceSearch(IpsContext.IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap);

	/**
	 * Return a set of Include directives to be added to the resource search
	 * for resources to include for a given IPS section. These include statements will
	 * be added to the same {@link SearchParameterMap} provided to
	 * {@link #massageResourceSearch(IpsContext.IpsSectionContext, SearchParameterMap)}.
	 * This is a separate method in order to make subclassing easier.
	 *
	 * @param theIpsSectionContext  The context, which indicates the IPS section and the resource type
	 *                              being searched for.
	 */
	@Nonnull
	Set<Include> provideResourceSearchIncludes(IpsContext.IpsSectionContext theIpsSectionContext);

	/**
	 * This method will be called for each found resource candidate for inclusion in the
	 * IPS document. The strategy can decide whether to include it or not.
	 */
	boolean shouldInclude(IpsContext.IpsSectionContext theIpsSectionContext, IBaseResource theCandidate);

}
