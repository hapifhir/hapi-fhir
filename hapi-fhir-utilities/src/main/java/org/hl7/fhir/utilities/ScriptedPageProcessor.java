package org.hl7.fhir.utilities;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * This object processes a string that contains <%%> or [%%] where each of these 
 * represents a 'command' that either does something, or inserts content into the
 * string - which is then reprocessed 
 * 
 * The outcome is a string. 
 * 
 * This is a base class that is expected to be subclassed for various context
 * specific processors
 * 
 * @author Grahame Grieve
 *
 */
public abstract class ScriptedPageProcessor {

  protected String title;
  protected int level;
  
  public ScriptedPageProcessor(String title, int level) {
    super();
    this.title = title;
    this.level = level;
  }

  public String process(String content) throws Exception {
    StringBuilder outcome = new StringBuilder();
    int cursor = 0;
    while (cursor < content.length()) {
      if (nextIs(content, cursor, "[%")) {
        cursor = processEntry(outcome, content, cursor, "%]");
      } else if (nextIs(content, cursor, "<%")) {
        cursor = processEntry(outcome, content, cursor, "%>");        
      } else {
        outcome.append(content.charAt(cursor));
        cursor++;
      }
    }
    return outcome.toString();
  }

  private int processEntry(StringBuilder outcome, String content, int cursor, String endText) throws Exception {
    int start = cursor+endText.length();
    int end = start;
    while (end < content.length()) {
      if (nextIs(content, end, endText)) {
        outcome.append(process(getContent(content.substring(start, end))));
        return end+endText.length();
      }
      end++;
    }
    throw new FHIRException("unterminated insert sequence");
  }

  private String getContent(String command) throws Exception {
    if (Utilities.noString(command) || command.startsWith("!"))
      return "";
    
    String[] parts = command.split("\\ ");
    return processCommand(command, parts);
  }

  protected String processCommand(String command, String[] com) throws Exception {
    if (com[0].equals("title"))
      return title;
    else if (com[0].equals("xtitle"))
      return Utilities.escapeXml(title);
    else if (com[0].equals("level"))
      return genlevel();  
    else if (com[0].equals("settitle")) {
      title = command.substring(9).replace("{", "<%").replace("}", "%>");
      return "";
    }
    else
      throw new FHIRException("Unknown command "+com[0]);
  }

  private boolean nextIs(String content, int cursor, String value) {
    if (cursor + value.length() > content.length())
      return false;
    else {
      String v = content.substring(cursor, cursor+value.length());
      return v.equals(value);
    }
  }
 
  public String genlevel() {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < level; i++) {
      b.append("../");
    }
    return b.toString();
  }


}
 