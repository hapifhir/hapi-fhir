/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */

package ca.uhn.fhir.osgi.impl;

import java.util.Collection;

import ca.uhn.fhir.osgi.FhirProviderBundle;

/**
 *
 * @author Akana, Inc. Professional Services
 *
 */
public class SimpleFhirProviderBundle implements FhirProviderBundle {

	// /////////////////////////////////////
	// ////////    Spring Wiring    ////////
	// /////////////////////////////////////

	private Collection<Object> providers;

	public void setProviders (Collection<Object> providers) {
		this.providers = providers;
	}
	
	// /////////////////////////////////////
	// /////////////////////////////////////
	// /////////////////////////////////////
	
	public SimpleFhirProviderBundle () {
		super();
	}

	@Override
	public Collection<Object> getProviders () {
		return this.providers;
	}

}
