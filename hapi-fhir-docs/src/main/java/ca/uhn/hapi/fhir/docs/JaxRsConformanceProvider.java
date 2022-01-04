package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Conformance Rest Service
 * 
 * @author Peter Van Houte
 */
 // START SNIPPET: jax-rs-conformance
@Path("")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsConformanceProvider extends AbstractJaxRsConformanceProvider {

  @EJB
  private JaxRsPatientRestProvider provider;

  public JaxRsConformanceProvider() {
    super("My Server Description", "My Server Name", "My Server Version");
  }

  @Override
  protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
    ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
    map.put(JaxRsConformanceProvider.class, this);
    map.put(JaxRsPatientRestProvider.class, provider);
    return map;
  }
}
// END SNIPPET: jax-rs-conformance
