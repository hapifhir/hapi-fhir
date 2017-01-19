package org.hl7.fhir.dstu2016may.utils;

import org.hl7.fhir.dstu2016may.model.ExpressionNode;
import org.hl7.fhir.dstu2016may.model.ExpressionNode.SourceLocation;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

// shared lexer for concrete syntaxes 
// - FluentPath
// - Mapping language

public class FHIRLexer {
  public class FHIRLexerException extends FHIRException {

    public FHIRLexerException() {
      super();
    }

    public FHIRLexerException(String message, Throwable cause) {
      super(message, cause);
    }

    public FHIRLexerException(String message) {
      super(message);
    }

    public FHIRLexerException(Throwable cause) {
      super(cause);
    }

  }
  private String path;
  private int cursor;
  private int currentStart;
  private String current;
  private SourceLocation currentLocation;
  private SourceLocation currentStartLocation;
  private int id;

  public FHIRLexer(String source) throws FHIRLexerException {
    this.path = source;
    currentLocation = new SourceLocation(1, 1);
    next();
  }
  public String getCurrent() {
    return current;
  }
  public SourceLocation getCurrentLocation() {
    return currentLocation;
  }

  public boolean isConstant(boolean incDoubleQuotes) {
    return current.charAt(0) == '\'' || (incDoubleQuotes && current.charAt(0) == '"') || current.charAt(0) == '@' || current.charAt(0) == '%' || current.charAt(0) == '-' || (current.charAt(0) >= '0' && current.charAt(0) <= '9') || current.equals("true") || current.equals("false") || current.equals("{}");
  }

  public boolean isStringConstant() {
    return current.charAt(0) == '\'' || current.charAt(0) == '"';
  }

  public String take() throws FHIRLexerException {
    String s = current;
    next();
    return s;
  }

  public boolean isToken() {
    if (Utilities.noString(current))
      return false;

    if (current.startsWith("$"))
      return true;

    if (current.equals("*") || current.equals("**"))
      return true;

    if ((current.charAt(0) >= 'A' && current.charAt(0) <= 'Z') || (current.charAt(0) >= 'a' && current.charAt(0) <= 'z')) {
      for (int i = 1; i < current.length(); i++) 
        if (!( (current.charAt(1) >= 'A' && current.charAt(1) <= 'Z') || (current.charAt(1) >= 'a' && current.charAt(1) <= 'z') ||
            (current.charAt(1) >= '0' && current.charAt(1) <= '9')))
          return false;
      return true;
    }
    return false;
  }

  public FHIRLexerException error(String msg) {
    return error(msg, currentLocation.toString());
  }

  public FHIRLexerException error(String msg, String location) {
    return new FHIRLexerException("Error in "+path+" at "+location+": "+msg);
  }

  public void next() throws FHIRLexerException {
    current = null;
    boolean last13 = false;
    while (cursor < path.length() && Character.isWhitespace(path.charAt(cursor))) {
      if (path.charAt(cursor) == '\r') {
        currentLocation.setLine(currentLocation.getLine() + 1);
        currentLocation.setColumn(1);
        last13 = true;
      } else if (!last13 && (path.charAt(cursor) == '\n')) {
        currentLocation.setLine(currentLocation.getLine() + 1);
        currentLocation.setColumn(1);
        last13 = false;
      } else {
        last13 = false;
        currentLocation.setColumn(currentLocation.getColumn() + 1);
      }
      cursor++;
    }
    currentStart = cursor;
    currentStartLocation = currentLocation;
    if (cursor < path.length()) {
      char ch = path.charAt(cursor);
      if (ch == '!' || ch == '>' || ch == '<' || ch == ':' || ch == '-' || ch == '=')  {
        cursor++;
        if (cursor < path.length() && (path.charAt(cursor) == '=' || path.charAt(cursor) == '~' || path.charAt(cursor) == '-')) 
          cursor++;
        current = path.substring(currentStart, cursor);
      } else if (ch >= '0' && ch <= '9') {
          cursor++;
        boolean dotted = false;
        while (cursor < path.length() && ((path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9') || (path.charAt(cursor) == '.') && !dotted)) {
          if (path.charAt(cursor) == '.')
            dotted = true;
          cursor++;
        }
        if (path.charAt(cursor-1) == '.')
          cursor--;
        current = path.substring(currentStart, cursor);
      }  else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
        while (cursor < path.length() && ((path.charAt(cursor) >= 'A' && path.charAt(cursor) <= 'Z') || (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z') || 
            (path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9') || path.charAt(cursor) == '_')) 
          cursor++;
        current = path.substring(currentStart, cursor);
      } else if (ch == '%') {
        cursor++;
        if (cursor < path.length() && (path.charAt(cursor) == '"')) {
          cursor++;
          while (cursor < path.length() && (path.charAt(cursor) != '"'))
            cursor++;
          cursor++;
        } else
        while (cursor < path.length() && ((path.charAt(cursor) >= 'A' && path.charAt(cursor) <= 'Z') || (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z') || 
            (path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9') || path.charAt(cursor) == ':' || path.charAt(cursor) == '-'))
          cursor++;
        current = path.substring(currentStart, cursor);
      } else if (ch == '/') {
        cursor++;
        if (cursor < path.length() && (path.charAt(cursor) == '/')) {
          cursor++;
          while (cursor < path.length() && !((path.charAt(cursor) == '\r') || path.charAt(cursor) == '\n')) 
            cursor++;
        }
        current = path.substring(currentStart, cursor);
      } else if (ch == '$') {
        cursor++;
        while (cursor < path.length() && (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z'))
          cursor++;
        current = path.substring(currentStart, cursor);
      } else if (ch == '{') {
        cursor++;
        ch = path.charAt(cursor);
        if (ch == '}')
          cursor++;
        current = path.substring(currentStart, cursor);
      } else if (ch == '"'){
        cursor++;
        boolean escape = false;
        while (cursor < path.length() && (escape || path.charAt(cursor) != '"')) {
          if (escape)
            escape = false;
          else 
            escape = (path.charAt(cursor) == '\\');
          cursor++;
        }
        if (cursor == path.length())
          throw error("Unterminated string");
        cursor++;
        current = "\""+path.substring(currentStart+1, cursor-1)+"\"";
      } else if (ch == '\''){
        cursor++;
        char ech = ch;
        boolean escape = false;
        while (cursor < path.length() && (escape || path.charAt(cursor) != ech)) {
          if (escape)
            escape = false;
          else 
            escape = (path.charAt(cursor) == '\\');
          cursor++;
        }
        if (cursor == path.length())
          throw error("Unterminated string");
        cursor++;
        current = path.substring(currentStart, cursor);
        if (ech == '\'')
          current = "\'"+current.substring(1, current.length() - 1)+"\'";
      } else if (ch == '@'){
        cursor++;
        while (cursor < path.length() && isDateChar(path.charAt(cursor)))
          cursor++;          
        current = path.substring(currentStart, cursor);
      } else { // if CharInSet(ch, ['.', ',', '(', ')', '=', '$']) then
        cursor++;
        current = path.substring(currentStart, cursor);
      }
    }
  }


  private boolean isDateChar(char ch) {
    return ch == '-' || ch == ':' || ch == 'T' || ch == '+' || ch == 'Z' || Character.isDigit(ch);
  }
  public boolean isOp() {
    return ExpressionNode.Operation.fromCode(current) != null;
  }
  public boolean done() {
    return currentStart >= path.length();
  }
  public int nextId() {
    id++;
    return id;
  }
  public SourceLocation getCurrentStartLocation() {
    return currentStartLocation;
  }
  
  // special case use
  public void setCurrent(String current) {
    this.current = current;
  }

  public boolean hasComment() {
    return !done() && current.startsWith("//");
  }
  public boolean hasToken(String kw) {
      return !done() && kw.equals(current);
  }
  public void token(String kw) throws FHIRLexerException {
    if (!kw.equals(current)) 
      throw error("Found \""+current+"\" expecting \""+kw+"\"");
    next();
  }
  public String readConstant(String desc) throws FHIRLexerException {
    if (!isStringConstant())
      throw error("Found "+current+" expecting \"["+desc+"]\"");

    return processConstant(take());
  }

  public String processConstant(String s) throws FHIRLexerException {
    StringBuilder b = new StringBuilder();
    int i = 1;
    while (i < s.length()-1) {
      char ch = s.charAt(i);
      if (ch == '\\') {
        i++;
        switch (s.charAt(i)) {
        case 't': 
          b.append('\t');
          break;
        case 'r':
          b.append('\r');
          break;
        case 'n': 
          b.append('\n');
          break;
        case 'f': 
          b.append('\f');
          break;
        case '\'':
          b.append('\'');
          break;
        case '\\': 
          b.append('\\');
          break;
        case '/': 
          b.append('\\');
          break;
        case 'u':
          i++;
          int uc = Integer.parseInt(s.substring(i, i+4), 16);
          b.append((char) uc);
          i = i + 4;
          break;
        default:
          throw new FHIRLexerException("Unknown character escape \\"+s.charAt(i));
        }
      } else {
        b.append(ch);
        i++;
      }
    }
    return b.toString();

  }
  public void skipToken(String token) throws FHIRLexerException {
    if (getCurrent().equals(token))
      next();
    
  }
  public String takeDottedToken() throws FHIRLexerException {
    StringBuilder b = new StringBuilder();
    b.append(take());
    while (!done() && getCurrent().equals(".")) {
      b.append(take());
      b.append(take());
    }
    return b.toString();
  }
  
  void skipComments() throws FHIRLexerException {
    while (!done() && hasComment())
      next();
  }

}
