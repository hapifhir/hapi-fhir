package ca.uhn.fhir.rest.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * <pre>
 * When populating the StructureDefinition links in a capability statement,
 * it can be useful to know the lowest common superclass for the profiles in use for a given resource name.
 * This class finds this superclass, by incrementally computing the greatest common sequence of ancestor classes in the class hierarchies of registered resources.
 * For instance, given the following classes
 * MyPatient extends Patient
 * MyPatient2 extends MyPatient
 * MyPatient3 extends MyPatient
 * MyPatient4 extends MyPatient3
 * this class will find the common ancestor sequence "IBaseResource -> Patient -> MyPatient". MyPatient is the lowest common superclass in this hierarchy.
 * </pre>
 * 
 */
public class CommonResourceSupertypeScanner {

  private List<Class<? extends IBaseResource>> greatestSharedAncestorsDescending;
  private boolean initialized;

  /**
   * Recomputes the lowest common superclass by adding a new resource definition to the hierarchy.
   * @param resourceClass  The resource class to add.
   */
  public void register(Class<? extends IBaseResource> resourceClass) {
    List<Class<? extends IBaseResource>> resourceClassesInHierarchy = new LinkedList<>();
    Class<?> currentClass = resourceClass;
    while (IBaseResource.class.isAssignableFrom(currentClass)
            && currentClass.getAnnotation(ResourceDef.class) != null) {
      resourceClassesInHierarchy.add((Class<? extends IBaseResource>)currentClass);
      currentClass = currentClass.getSuperclass();
    }
    Collections.reverse(resourceClassesInHierarchy);
    if (initialized) {
      for (int i = 0; i < Math.min(resourceClassesInHierarchy.size(), greatestSharedAncestorsDescending.size()); i++) {
        if (greatestSharedAncestorsDescending.get(i) != resourceClassesInHierarchy.get(i)) {
          greatestSharedAncestorsDescending = greatestSharedAncestorsDescending.subList(0, i);
          break;
        }
      }
    } else {
      greatestSharedAncestorsDescending = resourceClassesInHierarchy;
      initialized = true;
    }
  }
  
  /**
   * @return The lowest common superclass of currently registered resources.
   */
  public Optional<Class<? extends IBaseResource>> getLowestCommonSuperclass() {
    if (!initialized || greatestSharedAncestorsDescending.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(greatestSharedAncestorsDescending.get(greatestSharedAncestorsDescending.size() - 1));
  }

}
