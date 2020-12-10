package org.opencds.cqf.dstu3.helpers;

import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class consists exclusively of static methods that assist with managing contained FHIR
 * Resources.
 *
 * The FHIR specification does not allow contained resources to contain additional resources:
 * &gt; Contained resources SHALL NOT contain additional contained resources.
 * https://www.hl7.org/fhir/references.html#contained
 *
 * When returning a resource from an annotated method that responds to an incoming request any
 * resources contained in another resource will be removed.  `liftContainedResourcesToParent` in
 * this class will move all contained resources to the parent so they are present in responses
 * to clients.
 */
public class ContainedHelper {

  /**
   * Adds all contained resources in resources contained on the parent to the parent.
   *
   * @param resource the parent resource where contained resources should be
   * @return the modified parent resource
   */
  public static DomainResource liftContainedResourcesToParent(DomainResource resource) {
    getContainedResourcesInContainedResources(resource)
        .forEach(resource::addContained);   // add them to the parent

    return resource;  // Return the resource to allow for method chaining
  }

  /**
   * Returns all contained resources that are not already directly present on the parent resource.
   *
   * @param resource the parent resource
   * @return list of the contained resources
   */
  private static List<Resource> getContainedResourcesInContainedResources(Resource resource) {
    if (!(resource instanceof DomainResource)) {
      return new ArrayList<>();
    }
    return streamContainedResourcesInContainedResources(resource).collect(Collectors.toList());
  }

  /**
   * Returns all contained resources, including resources directly contained on the parent and
   * resources contained on any other contained resource
   * @param resource the parent resource
   * @return list of all contained resources
   */
  public static List<Resource> getAllContainedResources(Resource resource) {
    if (!(resource instanceof DomainResource)) {
      return new ArrayList<>();
    }
    return streamAllContainedResources(resource).collect(Collectors.toList());
  }

  /**
   * @see ContainedHelper#getContainedResourcesInContainedResources(Resource)
   */
  private static Stream<Resource> streamContainedResourcesInContainedResources(Resource resource) {
    if (!(resource instanceof DomainResource)) {
      return Stream.empty();
    }
    return ((DomainResource) resource)
        .getContained() // We don't need to re-add any resources that are already on the parent.
        .stream()
        .flatMap(ContainedHelper::streamAllContainedResources);  // Get the resources contained
  }

  /**
   * @see ContainedHelper#getAllContainedResources(Resource)
   */
  private static Stream<Resource> streamAllContainedResources(Resource resource) {
    if (!(resource instanceof DomainResource)) {
      return Stream.empty();
    }
    List<Resource> contained = ((DomainResource) resource).getContained();

    return Stream
        .concat(contained.stream(),
            contained
                .stream()
                .flatMap(ContainedHelper::streamAllContainedResources));
  }
}
