package ca.uhn.fhir.jpa.ips.api;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IIpsGenerationStrategy {

	/**
	 * Provides a registry of the IPS sections.
	 */
	SectionRegistry getSectionRegistry();

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
	 * In this method, the strategy can manipulate the {@link SearchParameterMap} that will
	 * be used to find candidate resources for the given IPS section. The map will already have
	 * a subject/patient parameter added to it.
	 *
	 * @param theIpsSectionContext The context.
	 * @param theSearchParameterMap The map to manipulate.
	 */
	void massageResourceSearch(IpsContext.IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap);

	/**
	 * This method will be called for each found resource candidate for inclusion in the
	 * IPS document. The strategy can decide whether to include it or not.
	 */
	boolean shouldInclude(IpsContext.IpsSectionContext theIpsSectionContext, IBaseResource theCandidate);
}
