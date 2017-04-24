package org.hl7.fhir.dstu3.utils;

import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.ExpressionNode.SourceLocation;
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
  private String source;
  private int cursor;
  private int currentStart;
  private String current;
  private SourceLocation currentLocation;
  private SourceLocation currentStartLocation;
  private int id;

  public FHIRLexer(String source) throws FHIRLexerException {
    this.source = source;
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
    return current.charAt(0) == '\'' || (incDoubleQuotes && current.charAt(0) == '"') || current.charAt(0) == '@' || current.charAt(0) == '%' || 
        current.charAt(0) == '-' || current.charAt(0) == '+' || (current.charAt(0) >= '0' && current.charAt(0) <= '9') || 
        current.equals("true") || current.equals("false") || current.equals("{}");
  }

  public boolean isStringConstant() {
    return current.charAt(0) == '\'' || current.charAt(0) == '"';
  }

  public String take() throws FHIRLexerException {
    String s = current;
    next();
    return s;
  }

  public int takeInt() throws FHIRLexerException {
    String s = current;
    if (!Utilities.isInteger(s))
      throw error("Found "+current+" expecting an integer");
    next();
    return Integer.parseInt(s);
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
    return new FHIRLexerException("Error at "+location+": "+msg);
  }

  public void next() throws FHIRLexerException {
    current = null;
    boolean last13 = false;
    while (cursor < source.length() && Character.isWhitespace(source.charAt(cursor))) {
      if (source.charAt(cursor) == '\r') {
        currentLocation.setLine(currentLocation.getLine() + 1);
        currentLocation.setColumn(1);
        last13 = true;
      } else if (!last13 && (source.charAt(cursor) == '\n')) {
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
    if (cursor < source.length()) {
      char ch = source.charAt(cursor);
      if (ch == '!' || ch == '>' || ch == '<' || ch == ':' || ch == '-' || ch == '=')  {
        cursor++;
        if (cursor < source.length() && (source.charAt(cursor) == '=' || source.charAt(cursor) == '~' || source.charAt(cursor) == '-')) 
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch == '.' ) {
        cursor++;
        if (cursor < source.length() && (source.charAt(cursor) == '.')) 
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch >= '0' && ch <= '9') {
          cursor++;
        boolean dotted = false;
        while (cursor < source.length() && ((source.charAt(cursor) >= '0' && source.charAt(cursor) <= '9') || (source.charAt(cursor) == '.') && !dotted)) {
          if (source.charAt(cursor) == '.')
            dotted = true;
          cursor++;
        }
        if (source.charAt(cursor-1) == '.')
          cursor--;
        current = source.substring(currentStart, cursor);
      }  else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
        while (cursor < source.length() && ((source.charAt(cursor) >= 'A' && source.charAt(cursor) <= 'Z') || (source.charAt(cursor) >= 'a' && source.charAt(cursor) <= 'z') || 
            (source.charAt(cursor) >= '0' && source.charAt(cursor) <= '9') || source.charAt(cursor) == '_')) 
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch == '%') {
        cursor++;
        if (cursor < source.length() && (source.charAt(cursor) == '"')) {
          cursor++;
          while (cursor < source.length() && (source.charAt(cursor) != '"'))
            cursor++;
          cursor++;
        } else
        while (cursor < source.length() && ((source.charAt(cursor) >= 'A' && source.charAt(cursor) <= 'Z') || (source.charAt(cursor) >= 'a' && source.charAt(cursor) <= 'z') || 
            (source.charAt(cursor) >= '0' && source.charAt(cursor) <= '9') || source.charAt(cursor) == ':' || source.charAt(cursor) == '-'))
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch == '/') {
        cursor++;
        if (cursor < source.length() && (source.charAt(cursor) == '/')) {
          cursor++;
          while (cursor < source.length() && !((source.charAt(cursor) == '\r') || source.charAt(cursor) == '\n')) 
            cursor++;
        }
        current = source.substring(currentStart, cursor);
      } else if (ch == '$') {
        cursor++;
        while (cursor < source.length() && (source.charAt(cursor) >= 'a' && source.charAt(cursor) <= 'z'))
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch == '{') {
        cursor++;
        ch = source.charAt(cursor);
        if (ch == '}')
          cursor++;
        current = source.substring(currentStart, cursor);
      } else if (ch == '"'){
        cursor++;
        boolean escape = false;
        while (cursor < source.length() && (escape || source.charAt(cursor) != '"')) {
          if (escape)
            escape = false;
          else 
            escape = (source.charAt(cursor) == '\\');
          cursor++;
        }
        if (cursor == source.length())
          throw error("Unterminated string");
        cursor++;
        current = "\""+source.substring(currentStart+1, cursor-1)+"\"";
      } else if (ch == '\''){
        cursor++;
        char ech = ch;
        boolean escape = false;
        while (cursor < source.length() && (escape || source.charAt(cursor) != ech)) {
          if (escape)
            escape = false;
          else 
            escape = (source.charAt(cursor) == '\\');
          cursor++;
        }
        if (cursor == source.length())
          throw error("Unterminated string");
        cursor++;
        current = source.substring(currentStart, cursor);
        if (ech == '\'')
          current = "\'"+current.substring(1, current.length() - 1)+"\'";
      } else if (ch == '@'){
        cursor++;
        while (cursor < source.length() && isDateChar(source.charAt(cursor)))
          cursor++;          
        current = source.substring(currentStart, cursor);
      } else { // if CharInSet(ch, ['.', ',', '(', ')', '=', '$']) then
        cursor++;
        current = source.substring(currentStart, cursor);
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
    return currentStart >= source.length();
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
  public boolean hasToken(String... names) {
    if (done()) 
      return false;
    for (String s : names)
      if (s.equals(current))
        return true;
    return false;
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
