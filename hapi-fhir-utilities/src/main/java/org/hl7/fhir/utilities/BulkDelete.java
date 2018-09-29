package org.hl7.fhir.utilities;

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
