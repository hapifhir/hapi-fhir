/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

/**
 * Holder class that manages two different MDM expansion implementation approaches based on MdmSettings.
 * <p>
 * This class addresses the dependency injection challenge where MdmSettings may not be available when these
 * service objects are created. It holds references to both implementation approaches and determines which ones
 * to use based on the MdmSettings. setMdmSettings method should be called when MdmSettings is constructed or updated
 * for proper functioning.
 * <p>
 * The two implementation approaches are:
 * <p>
 * <strong>1. Eid Match-Only Mode:</strong> A simplified approach where resources are matched based solely on
 * Enterprise ID (EID) values without creating golden resources or MDM links. This mode is activated when
 * MdmSettings specify MATCH_ONLY mode and EID systems are defined.
 * <p>
 * <strong>2. Full MDM Mode:</strong> The complete MDM solution that creates golden resources and manages
 * MDM links between resources.
 * <p>
 * Each approach has two service implementations, one for mdm expansion for searches and the other for mdm expansion for group bulk export:
 * <p>
 * <strong>Eid Match-Only Mode implementations:</strong>
 * - MdmEidMatchOnlyLinkExpandSvc
 * - BulkExportMdmEidMatchOnlyResourceExpander
 * <p>
 * <strong>Full MDM Mode implementations:</strong>
 * - MdmLinkExpandSvc
 * - BulkExportMdmResourceExpander
 * <p>
 *
 * This class holds references to these service objects rather than creating them by itself, because some of these service objects have Spring annotations
 * like @Transactional, for those annotations to work the objects need to be created by Spring itself.
 */
public class MdmExpandersHolder {

	/** MDM configuration settings used to determine which implementation to use */
	private IMdmSettings myMdmSettings;

	/** Cached instance of the selected link expand service */
	private IMdmLinkExpandSvc myLinkExpandSvcInstanceToUse;

	/** Cached instance of the selected bulk export resource expander */
	private IBulkExportMdmResourceExpander<? extends IResourcePersistentId>
			myBulkExportMDMResourceExpanderInstanceToUse;

	/** Full MDM link expand service implementation
	 * We have to use the interface as the type here instead of concrete implementing class MdmLinkExpandSvc
	 * because the class has Spring annotations like @Transactional which rely on a Proxy interface implementation and doesn't work if concrete classes are used as beans. */
	private final IMdmLinkExpandSvc myMdmLinkExpandSvc;

	/** EID-only match expand service implementation */
	private final MdmEidMatchOnlyExpandSvc myMdmEidMatchOnlyExpandSvc;

	/** Full MDM bulk export resource expander implementation */
	private final IBulkExportMdmFullResourceExpander myBulkExportMDMResourceExpander;

	/** EID-only match bulk export resource expander implementation */
	private final IBulkExportMdmEidMatchOnlyResourceExpander myBulkExportMDMEidMatchOnlyResourceExpander;

	private final FhirContext myFhirContext;

	public MdmExpandersHolder(
			FhirContext theFhirContext,
			IMdmLinkExpandSvc theMdmLinkExpandSvc,
			MdmEidMatchOnlyExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			IBulkExportMdmFullResourceExpander theBulkExportMdmFullResourceExpander,
			IBulkExportMdmEidMatchOnlyResourceExpander theBulkExportMdmEidMatchOnlyResourceExpander) {

		myFhirContext = theFhirContext;
		myMdmLinkExpandSvc = theMdmLinkExpandSvc;
		myMdmEidMatchOnlyExpandSvc = theMdmEidMatchOnlyLinkExpandSvc;
		myBulkExportMDMResourceExpander = theBulkExportMdmFullResourceExpander;
		myBulkExportMDMEidMatchOnlyResourceExpander = theBulkExportMdmEidMatchOnlyResourceExpander;
	}

	/**
	 * Returns the appropriate expand service instance appropriate for the mdm settings
	 */
	public IMdmLinkExpandSvc getLinkExpandSvcInstance() {
		if (myLinkExpandSvcInstanceToUse != null) {
			// we already determined instance to use, just return it
			return myLinkExpandSvcInstanceToUse;
		}

		myLinkExpandSvcInstanceToUse = determineExpandSvsInstanceToUse();

		return myLinkExpandSvcInstanceToUse;
	}

	/**
	 * Returns the appropriate bulk export resource expander instance appropriate for the mdm settings
	 */
	public IBulkExportMdmResourceExpander getBulkExportMDMResourceExpanderInstance() {
		if (myBulkExportMDMResourceExpanderInstanceToUse != null) {
			// we already determined instance to use, just return it
			return myBulkExportMDMResourceExpanderInstanceToUse;
		}

		myBulkExportMDMResourceExpanderInstanceToUse = determineBulkExportMDMResourceExpanderInstanceToUse();

		return myBulkExportMDMResourceExpanderInstanceToUse;
	}

	/**
	 * Determines which bulk export resource expander to use based on MDM mode and EID configuration.
	 */
	public IBulkExportMdmResourceExpander determineBulkExportMDMResourceExpanderInstanceToUse() {
		if (isMatchOnlyWithEidSystems()) {
			return myBulkExportMDMEidMatchOnlyResourceExpander;
		} else {
			return myBulkExportMDMResourceExpander;
		}
	}

	/**
	 * Determines if MDM is configured in MATCH_ONLY mode and EID systems are defined in the MDM rules.
	 */
	private boolean isMatchOnlyWithEidSystems() {

		if (myMdmSettings == null) {
			// if mdmSettings is not set yet, assume we are using the full mdm mode
			// to not break existing code, because previously we were just using the
			// full mdm implementation without checking the mdm settings.
			// This would be called again when mdmSettings setter is called
			return false;
		}
		boolean isMatchOnly = myMdmSettings.getMode() == MdmModeEnum.MATCH_ONLY;
		boolean hasEidSystems = false;
		if (myMdmSettings.getMdmRules() != null) {
			hasEidSystems = myMdmSettings.getMdmRules().getEnterpriseEIDSystems() != null
					&& !myMdmSettings.getMdmRules().getEnterpriseEIDSystems().isEmpty();
		}
		return isMatchOnly && hasEidSystems;
	}

	/**
	 * Determines which expand service to use and configures it if necessary.
	 */
	private IMdmLinkExpandSvc determineExpandSvsInstanceToUse() {
		if (isMatchOnlyWithEidSystems()) {
			myMdmEidMatchOnlyExpandSvc.setMyEidHelper(new EIDHelper(myFhirContext, myMdmSettings));
			return myMdmEidMatchOnlyExpandSvc;
		} else {
			return myMdmLinkExpandSvc;
		}
	}

	/**
	 * Sets the MDM settings and immediately determines which service implementations to use.
	 * This method is called after MDM settings become available during application startup.
	 */
	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
		myLinkExpandSvcInstanceToUse = determineExpandSvsInstanceToUse();
		myBulkExportMDMResourceExpanderInstanceToUse = determineBulkExportMDMResourceExpanderInstanceToUse();
	}
}
