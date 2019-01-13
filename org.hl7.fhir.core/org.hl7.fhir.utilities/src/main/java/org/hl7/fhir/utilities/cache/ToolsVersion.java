package org.hl7.fhir.utilities.cache;

/*-
 * #%L
 * org.hl7.fhir.utilities
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


public class ToolsVersion {

  /**
   * This constant is the maseter tool version. Any time this is updated, all the downstream tools know that something about the cache has changed, and require to be synchronised.
   * 
   * This constant is released in the following ways:
   *  - with the master source code
   *  - in the jar code for the publisher
   *  - in the packages (spec.internals, and package.json)
   *  - in the validator package (validator.tgz)
   *  
   * this constant is checked 
   *  - when loading the current version package
   */
  public final static int TOOLS_VERSION = 3;
  public final static String TOOLS_VERSION_STR = "3";
  
  // version history:
  // 3: invalidate the current packages because of an error in the version value in the package.json
  // 2: split auto-ig into r3/r4 content
  // arbitrarily started at 1 when changing to git
}

