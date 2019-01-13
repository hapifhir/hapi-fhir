package org.hl7.fhir.utilities;

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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BulkDelete {


  private static final String DIR2 = "C:\\work\\org.hl7.fhir\\build\\vscache";
  private static final String DIR1 = "C:\\work\\org.hl7.fhir\\build\\vscache\\validation.cache";
  private static final String PATTERN1 = "hl7.org/fhir";
  private static final String PATTERN2 = "OperationOutcome";

  public static void main(String[] args) throws FileNotFoundException, IOException {
//    exec(DIR1, PATTERN1);
//    exec(DIR2, PATTERN1);
    exec(DIR1, PATTERN2);
    exec(DIR2, PATTERN2);
  }

  public static void exec(String d, String pattern) throws FileNotFoundException, IOException {
    for (File f : new File(d).listFiles()) {
      if (!f.isDirectory()) {
        String s = TextFile.fileToString(f);
        if (s.contains(pattern)) {
          System.out.println("delete "+f.getAbsolutePath());
          f.delete();
        }
      }

    }
  }

}
