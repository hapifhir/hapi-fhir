package org.hl7.fhir.r4.model;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.r4.utils.FHIRPathEngine;

/**
 * This class is the base class for Profile classes - whether generated or manual
 * 
 * @author Grahame Grieve
 *
 */
public class ProfilingWrapper {

  // some discriminators require on features of external resources
  // to do slice matching. This provides external services to match 
  public interface IResourceResolver {
    Resource resolve(Base context, Base resource, Base reference);
  }

  // context
  private Base context; // bundle, if one is present and related; sole use is to pass to IResourceResolver.resolve as context
  private Base resource; // resource that contains the wrapped object
  protected Base wrapped; // the actual wrapped object 
  
  // FHIRPath engine
  private Map<String, ExpressionNode> cache; 
  private FHIRPathEngine engine;

  public ProfilingWrapper(Base context, Base resource, Base wrapped) {
    super();
    this.context = context;
    this.resource = resource;
    this.wrapped = wrapped;
  }
  
  public ProfilingWrapper(Base context, Base resource) {
    super();
    this.context = context;
    this.resource = resource;
    this.wrapped = resource;
  }
  
  /**
   * This is a convenient called for 
   * @param object
   * @return
   */
  private ProfilingWrapper wrap(Base object) {
    ProfilingWrapper res = new ProfilingWrapper(context, resource, object);
    res.cache = cache;
    res.engine = engine;
    return res;
  }
  
  
}
