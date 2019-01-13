package org.hl7.fhir.dstu3.utils.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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


public class XmlLocationData {

	 public static final String LOCATION_DATA_KEY = "locationDataKey";

   private final String systemId;
   private final int startLine;
   private final int startColumn;
   private final int endLine;
   private final int endColumn;

   public XmlLocationData(String systemId, int startLine,
           int startColumn, int endLine, int endColumn) {
       super();
       this.systemId = systemId;
       this.startLine = startLine;
       this.startColumn = startColumn;
       this.endLine = endLine;
       this.endColumn = endColumn;
   }

   public String getSystemId() {
       return systemId;
   }

   public int getStartLine() {
       return startLine;
   }

   public int getStartColumn() {
       return startColumn;
   }

   public int getEndLine() {
       return endLine;
   }

   public int getEndColumn() {
       return endColumn;
   }

   @Override
   public String toString() {
       return getSystemId() + "[line " + startLine + ":"
               + startColumn + " to line " + endLine + ":"
               + endColumn + "]";
   }
}
