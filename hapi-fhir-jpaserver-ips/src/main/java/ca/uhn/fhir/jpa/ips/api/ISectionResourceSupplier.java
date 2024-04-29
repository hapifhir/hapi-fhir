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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.thymeleaf.util.Validate;

import java.util.List;

/**
 * This interface is invoked for each section of the IPS, and fetches/returns the
 * resources which will be included in the IPS document for that section. This
 * might be by performing a search in a local repository, but could also be
 * done by calling a remote repository, performing a calculation, making
 * JDBC database calls directly, etc.
 * <p>
 * Note that you only need to implement this interface directly if you want to
 * provide manual logic for gathering and preparing resources to include in
 * an IPS document. If your resources can be collected by querying a JPS
 * repository, you can use {@link ca.uhn.fhir.jpa.ips.jpa.JpaSectionResourceSupplier}
 * as the implementation of this interface, and
 * {@link ca.uhn.fhir.jpa.ips.jpa.IJpaSectionSearchStrategy} becomes the class
 * that is used to define your searches.
 * </p>
 *
 * @since 7.2.0
 * @see ca.uhn.fhir.jpa.ips.jpa.JpaSectionResourceSupplier
 */
public interface ISectionResourceSupplier {

	/**
	 * This method will be called once for each section context (section and resource type combination),
	 * and will be used to supply the resources to include in the given IPS section. This method can
	 * be used if you wish to fetch resources for a given section from a source other than
	 * the repository. This could mean fetching resources using a FHIR REST client to an
	 * external server, or could even mean fetching data directly from a database using JDBC
	 * or similar.
	 *
	 * @param theIpsContext     The IPS context, containing the identity of the patient whose IPS is being generated.
	 * @param theSectionContext The section context, containing the section name and resource type.
	 * @param theRequestDetails The RequestDetails object associated with the HTTP request associated with this generation.
	 * @return Returns a list of resources to add to the given section, or <code>null</code>.
	 */
	@Nullable
	<T extends IBaseResource> List<ResourceEntry> fetchResourcesForSection(
			IpsContext theIpsContext, IpsSectionContext<T> theSectionContext, RequestDetails theRequestDetails);

	/**
	 * This enum specifies how an individual {@link ResourceEntry resource entry} that
	 * is returned by {@link #fetchResourcesForSection(IpsContext, IpsSectionContext, RequestDetails)}
	 * should be included in the resulting IPS document bundle.
	 */
	enum InclusionTypeEnum {

		/**
		 * The resource should be included in the document bundle and linked to
		 * from the Composition via the <code>Composition.section.entry</code>
		 * reference.
		 */
		PRIMARY_RESOURCE,

		/**
		 * The resource should be included in the document bundle, but not directly
		 * linked from the composition. This typically means that it is referenced
		 * by at least one primary resource.
		 */
		SECONDARY_RESOURCE,

		/**
		 * Do not include this resource in the document
		 */
		EXCLUDE
	}

	/**
	 * This class is the return type for {@link #fetchResourcesForSection(IpsContext, IpsSectionContext, RequestDetails)}.
	 */
	class ResourceEntry {

		private final IBaseResource myResource;

		private final InclusionTypeEnum myInclusionType;

		/**
		 * Constructor
		 *
		 * @param theResource The resource to include (must not be null)
		 * @param theInclusionType The inclusion type (must not be null)
		 */
		public ResourceEntry(@Nonnull IBaseResource theResource, @Nonnull InclusionTypeEnum theInclusionType) {
			Validate.notNull(theResource, "theResource must not be null");
			Validate.notNull(theInclusionType, "theInclusionType must not be null");
			myResource = theResource;
			myInclusionType = theInclusionType;
		}

		public IBaseResource getResource() {
			return myResource;
		}

		public InclusionTypeEnum getInclusionType() {
			return myInclusionType;
		}
	}
}
