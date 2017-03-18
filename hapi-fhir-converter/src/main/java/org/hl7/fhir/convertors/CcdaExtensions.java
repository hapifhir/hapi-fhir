package org.hl7.fhir.convertors;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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


public class CcdaExtensions {
  public final static String DAF_NAME_RACE = "http://hl7.org/fhir/StructureDefinition/us-core-race";
  public final static String DAF_NAME_ETHNICITY = "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity";

	public final static String BASE = "http://hl7.org/ccda";
  public final static String NAME_RELIGION = BASE+"/religious-affiliation";
  public final static String NAME_BIRTHPLACE = BASE+"birthplace";
  public final static String NAME_LANG_PROF = BASE+"proficiency-level";


}
