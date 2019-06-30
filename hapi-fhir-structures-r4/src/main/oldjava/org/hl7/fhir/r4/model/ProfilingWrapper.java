package org.hl7.fhir.r4.model;

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
