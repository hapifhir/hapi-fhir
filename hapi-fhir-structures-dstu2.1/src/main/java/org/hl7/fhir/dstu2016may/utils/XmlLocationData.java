package org.hl7.fhir.dstu2016may.utils;

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
