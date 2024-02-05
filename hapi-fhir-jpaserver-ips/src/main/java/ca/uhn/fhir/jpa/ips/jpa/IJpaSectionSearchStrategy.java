package ca.uhn.fhir.jpa.ips.jpa;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementations of this interface are used to fetch resources to include
 * for a given IPS section by performing a search in a local JPA repository.
 *
 * @since 7.2.0
 */
public interface IJpaSectionSearchStrategy {

	/**
	 * This method can manipulate the {@link SearchParameterMap} that will
	 * be used to find candidate resources for the given IPS section. The map will already have
	 * a subject/patient parameter added to it. The map provided in {@literal theSearchParameterMap}
	 * will contain a subject/patient reference (e.g. <code>?patient=Patient/123</code>), but no
	 * other parameters. This method can add other parameters. The default implementation of this
	 * interface performs no action.
	 * <p>
	 * For example, for a Vital Signs section, the implementation might add a parameter indicating
	 * the parameter <code>category=vital-signs</code>.
	 * </p>
	 *
	 * @param theIpsSectionContext  The context, which indicates the IPS section and the resource type
	 *                              being searched for.
	 * @param theSearchParameterMap The map to manipulate.
	 */
	default void massageResourceSearch(
		IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		// no action taken by default
	}

	/**
	 * This method will be called for each found resource candidate for inclusion in the
	 * IPS document. The strategy can decide whether to include it or not. Note that the
	 * default implementation will always return {@literal true}.
	 * <p>
	 * This method is called once for every resource that is being considered for inclusion
	 * in an IPS section.
	 * </p>
	 */
	default boolean shouldInclude(IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {
		return true;
	}


}
