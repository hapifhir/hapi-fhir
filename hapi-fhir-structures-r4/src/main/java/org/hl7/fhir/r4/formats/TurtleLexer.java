package org.hl7.fhir.r4.formats;

public class TurtleLexer {

  public enum TurtleTokenType {
    NULL, 
    TOKEN, SPECIAL, LITERAL
  }

  private String source;
  private int cursor; 
  private String token;
  private TurtleTokenType type;
  
  public TurtleLexer(String source) throws Exception {
    this.source = source;
    cursor = 0;
    readNext();
  }

  private void readNext() throws Exception {    
    if (cursor >= source.length()) {
      token = null;
      type = TurtleTokenType.NULL;
    } else if (source.charAt(cursor) == '"')
      readLiteral();
    else if (source.charAt(cursor) == '[' || source.charAt(cursor) == ']')
      readDelimiter();
    else if (source.charAt(cursor) == '(')
      throw new Exception("not supported yet");
    else if (source.charAt(cursor) == ';' || source.charAt(cursor) == '.' || source.charAt(cursor) == ',')
      readDelimiter();
    else if (Character.isLetter(source.charAt(cursor)))
      readToken();
    
  }

  private void readLiteral() {
    StringBuilder b = new StringBuilder();
    cursor++; // skip "        
    while (cursor < source.length() && source.charAt(cursor) != '"') {
      if (source.charAt(cursor) == '\\') {
        b.append(source.charAt(cursor));
        cursor++;        
      } 
      b.append(source.charAt(cursor));
      cursor++;
    }
    token = "\""+b.toString()+"\"";
    type = TurtleTokenType.LITERAL;
    cursor++; // skip "
    while (cursor < source.length() && Character.isWhitespace(source.charAt(cursor))) 
      cursor++;    
  }

  private void readDelimiter() {
    StringBuilder b = new StringBuilder();
    b.append(source.charAt(cursor));
    cursor++;
    token = b.toString();
    type = TurtleTokenType.SPECIAL;
    while (cursor < source.length() && Character.isWhitespace(source.charAt(cursor))) 
      cursor++;
  }

  private void readToken() {
    StringBuilder b = new StringBuilder();
    while (cursor < source.length() && isValidTokenChar(source.charAt(cursor))) {
      if (source.charAt(cursor) == '\\') {
        b.append(source.charAt(cursor));
        cursor++;        
      } 
      b.append(source.charAt(cursor));
      cursor++;
    }
    token = b.toString();
    type = TurtleTokenType.TOKEN;
    if (token.endsWith(".")) {
      cursor--;
      token = token.substring(0, token.length()-1);
    }
    while (cursor < source.length() && Character.isWhitespace(source.charAt(cursor))) 
      cursor++;
  }

  private boolean isValidTokenChar(char c) {
    return Character.isLetter(c) || Character.isDigit(c) || c == ':' || c == '\\' || c == '.';
  }

  public boolean done() {
    return type == TurtleTokenType.NULL;
  }

  public String next() throws Exception {
    String res = token;
    readNext();
    return res;
  }

  public String peek() throws Exception {
    return token;
  }

  public TurtleTokenType peekType() {
    return type;
  }
  
  
}
