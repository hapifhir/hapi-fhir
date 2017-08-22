package org.hl7.fhir.utilities.graphql;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;

import java.util.List;

public interface IGraphQLStorageServices<RT extends IAnyResource, REFT extends IBaseReference, BT extends IBaseBundle> {

  /**
   * given a reference inside a context, return what it references (including resolving internal references (e.g. start with #)
   */
  ReferenceResolution<RT> lookup(Object appInfo, RT context, REFT reference) throws FHIRException;

  /**
   * just get the identified resource
   */
  RT lookup(Object appInfo, String type, String id) throws FHIRException;

  /**
   * list the matching resources. searchParams are the standard search params.
   * this instanceof different to search because the server returns all matching resources, or an error. There instanceof no paging on this search
   */
  void listResources(Object appInfo, String type, List<Argument> searchParams, List<RT> matches) throws FHIRException;

  /**
   * just perform a standard search, and return the bundle as you return to the client
   */
  BT search(Object appInfo, String type, List<Argument> searchParams) throws FHIRException;

}
