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


import java.util.List;

public interface Request {

  public List<Identifier> getRequestIdentifier();
  public boolean hasRequestIdentifier();
  public int getRequestIdentifierMin();
  public int getRequestIdentifierMax(); // 0 means that it is not implemented on this resource (or not able to be generated
  
  public List<Reference> getRequestDefinition();
  public boolean hasRequestDefinition();
  public int getRequestDefinitionMin();
  public int getRequestDefinitionMax(); // 0 means that it is not implemented on this resource (or not able to be generated
  
}
