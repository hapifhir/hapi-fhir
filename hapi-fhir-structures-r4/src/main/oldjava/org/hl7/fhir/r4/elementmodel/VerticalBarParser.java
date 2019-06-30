package org.hl7.fhir.r4.elementmodel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;

/**
 * This class provides special support for parsing v2 by the v2 logical model
 * For the logical model, see the FHIRPath spec
 *         
 * @author Grahame Grieve
 *
 */
public class VerticalBarParser extends ParserBase {

  /**
   * Delimiters for a message. Note that the application rarely needs to concern
   * itself with this information; it mainly exists for internal use. However if
   * a message is being written to a spec that calls for non-standard delimiters,
   * the application can set them here.  
   * 
   * @author Grahame
   *
   */
  public class Delimiters {

    /**
     * Hl7 defined default delimiter for a field
     */
    public final static char DEFAULT_DELIMITER_FIELD = '|';

    /**
     * Hl7 defined default delimiter for a component
     */
    public final static char  DEFAULT_DELIMITER_COMPONENT = '^';

    /**
     * Hl7 defined default delimiter for a subcomponent
     */
    public final static char  DEFAULT_DELIMITER_SUBCOMPONENT = '&';

    /**
     * Hl7 defined default delimiter for a repeat
     */
    public final static char  DEFAULT_DELIMITER_REPETITION = '~';

    /**
     * Hl7 defined default delimiter for an escape
     */
    public final static char  DEFAULT_CHARACTER_ESCAPE = '\\';


    /**
     * defined escape character for this message
     */
    private char escapeCharacter;

    /**
     * defined repetition character for this message
     */
      private char repetitionDelimiter;

    /**
     * defined field character for this message
     */
      private char fieldDelimiter;

    /**
     * defined subComponent character for this message
     */
      private char subComponentDelimiter;

    /**
     * defined component character for this message
     */
      private char componentDelimiter;

      /**
       * create
       *
       */
    public Delimiters() {
      super();
      reset();
    }

    public boolean matches(Delimiters other) {
      return escapeCharacter == other.escapeCharacter &&
      repetitionDelimiter == other.repetitionDelimiter &&
      fieldDelimiter == other.fieldDelimiter &&
      subComponentDelimiter == other.subComponentDelimiter &&
      componentDelimiter == other.componentDelimiter;
    }
    
    /**
     * get defined component character for this message
     * @return
     */
    public char getComponentDelimiter() {
      return componentDelimiter;
    }

    /**
     * set defined component character for this message
     * @param componentDelimiter
     */
    public void setComponentDelimiter(char componentDelimiter) {
      this.componentDelimiter = componentDelimiter;
    }

    /**
     * get defined escape character for this message
     * @return
     */
    public char getEscapeCharacter() {
      return escapeCharacter;
    }

    /**
     * set defined escape character for this message
     * @param escapeCharacter
     */
    public void setEscapeCharacter(char escapeCharacter) {
      this.escapeCharacter = escapeCharacter;
    }

    /**
     * get defined field character for this message
     * @return
     */
    public char getFieldDelimiter() {
      return fieldDelimiter;
    }

    /**
     * set defined field character for this message
     * @param fieldDelimiter
     */
    public void setFieldDelimiter(char fieldDelimiter) {
      this.fieldDelimiter = fieldDelimiter;
    }

    /**
     * get repeat field character for this message
     * @return
     */
    public char getRepetitionDelimiter() {
      return repetitionDelimiter;
    }

    /**
     * set repeat field character for this message
     * @param repetitionDelimiter
     */
    public void setRepetitionDelimiter(char repetitionDelimiter) {
      this.repetitionDelimiter = repetitionDelimiter;
    }

    /**
     * get sub-component field character for this message
     * @return
     */
    public char getSubComponentDelimiter() {
      return subComponentDelimiter;
    }

    /**
     * set sub-component field character for this message
     * @param subComponentDelimiter
     */
    public void setSubComponentDelimiter(char subComponentDelimiter) {
      this.subComponentDelimiter = subComponentDelimiter;
    }

    /**
     * reset to default HL7 values
     *
     */
    public void reset () {
      fieldDelimiter = DEFAULT_DELIMITER_FIELD;
      componentDelimiter = DEFAULT_DELIMITER_COMPONENT;
      subComponentDelimiter = DEFAULT_DELIMITER_SUBCOMPONENT;
      repetitionDelimiter = DEFAULT_DELIMITER_REPETITION;
      escapeCharacter = DEFAULT_CHARACTER_ESCAPE;
    }
    
    /**
     * check that the delimiters are valid
     * 
     * @throws FHIRException
     */
    public void check() throws FHIRException {
        rule(componentDelimiter != fieldDelimiter, "Delimiter Error: \""+componentDelimiter+"\" is used for both CPComponent and CPField");
        rule(subComponentDelimiter != fieldDelimiter, "Delimiter Error: \""+subComponentDelimiter+"\" is used for both CPSubComponent and CPField");
        rule(subComponentDelimiter != componentDelimiter, "Delimiter Error: \""+subComponentDelimiter+"\" is used for both CPSubComponent and CPComponent");
        rule(repetitionDelimiter != fieldDelimiter, "Delimiter Error: \""+repetitionDelimiter+"\" is used for both Repetition and CPField");
        rule(repetitionDelimiter != componentDelimiter, "Delimiter Error: \""+repetitionDelimiter+"\" is used for both Repetition and CPComponent");
        rule(repetitionDelimiter != subComponentDelimiter, "Delimiter Error: \""+repetitionDelimiter+"\" is used for both Repetition and CPSubComponent");
        rule(escapeCharacter != fieldDelimiter, "Delimiter Error: \""+escapeCharacter+"\" is used for both Escape and CPField");
        rule(escapeCharacter != componentDelimiter, "Delimiter Error: \""+escapeCharacter+"\" is used for both Escape and CPComponent");
        rule(escapeCharacter != subComponentDelimiter, "Delimiter Error: \""+escapeCharacter+"\" is used for both Escape and CPSubComponent");
        rule(escapeCharacter != repetitionDelimiter, "Delimiter Error: \""+escapeCharacter+"\" is used for both Escape and Repetition");
    }

    /**
     * check to see whether ch is a delimiter character (vertical bar parser support)
     * @param ch
     * @return
     */
    public boolean isDelimiter(char ch) {
      return ch == escapeCharacter || ch == repetitionDelimiter || ch == fieldDelimiter || ch == subComponentDelimiter  || ch ==  componentDelimiter;
    }

    /** 
     * check to see whether ch is a cell delimiter char (vertical bar parser support)
     * @param ch
     * @return
     */
    public boolean isCellDelimiter(char ch) {
      return ch == repetitionDelimiter || ch == fieldDelimiter || ch == subComponentDelimiter  || ch ==  componentDelimiter;
    }

    /**
     * get the escape for a character
     * @param ch
     * @return
     */ 
    public String getEscape(char ch) {
      if (ch == escapeCharacter)
        return escapeCharacter + "E" + escapeCharacter;
      else if (ch == fieldDelimiter)
        return escapeCharacter + "F" + escapeCharacter;
      else if (ch == componentDelimiter)
        return escapeCharacter + "S" + escapeCharacter;
      else if (ch == subComponentDelimiter)
        return escapeCharacter + "T" + escapeCharacter;
      else if (ch == repetitionDelimiter)
        return escapeCharacter + "R" + escapeCharacter;
      else
        return null;
      }

    /**
     * build the MSH-2 content
     * @return
     */
    public String forMSH2() {
      return "" + componentDelimiter + repetitionDelimiter + escapeCharacter + subComponentDelimiter;
    }

    /** 
     * check to see whether ch represents a delimiter escape
     * @param ch
     * @return
     */
    public boolean isDelimiterEscape(char ch) {
      return ch == 'F' || ch == 'S'  || ch == 'E' || ch == 'T' || ch == 'R';
    }

    /** 
     * get escape for ch in an escape
     * @param ch
     * @return
     * @throws DefinitionException 
     * @throws FHIRException
     */
    public char getDelimiterEscapeChar(char ch) throws DefinitionException {
      if (ch == 'E')
        return escapeCharacter;
      else if (ch == 'F')
        return fieldDelimiter;
      else if (ch == 'S')
        return componentDelimiter;
      else if (ch == 'T')
        return subComponentDelimiter;
      else if (ch == 'R')
        return repetitionDelimiter;
      else
        throw new DefinitionException("internal error in getDelimiterEscapeChar");
    }
  }
  
  public class VerticalBarParserReader {

    
    private BufferedInputStream stream;
    private String charsetName; 
    private InputStreamReader reader = null;
    private boolean finished;
    private char peeked;
    private char lastValue;
    private int offset;
    private int lineNumber;

    public VerticalBarParserReader(BufferedInputStream stream, String charsetName) throws FHIRException {
      super();
      setStream(stream);
      setCharsetName(charsetName);
      open();
    }

    public String getCharsetName() {
      return charsetName;
    }

    public void setCharsetName(String charsetName) {
      this.charsetName = charsetName;
    }

    public BufferedInputStream getStream() {
      return stream;
    }

    public void setStream(BufferedInputStream stream) {
      this.stream = stream;
    }
    
    private void open() throws FHIRException {
      try {
        stream.mark(2048);
        reader = new InputStreamReader(stream, charsetName);
        offset = 0;
        lineNumber = 0;
        lastValue = ' ';
        next();
      } catch (Exception e) {
        throw new FHIRException(e);
      }
    }

    private void next() throws IOException, FHIRException {
      finished = !reader.ready();
      if (!finished) {
        char[] temp = new char[1];
        rule(reader.read(temp, 0, 1) == 1, "unable to read 1 character from the stream");
        peeked = temp[0];
      } 
    }

    public String read(int charCount) throws FHIRException {
      String value = "";
      for (int i = 0; i < charCount; i++) 
        value = value + read();
      return value;
    }
    
    public void skipEOL () throws FHIRException {
      while (!finished && (peek() == '\r' || peek() == '\n'))
        read();
    }
    
    public char read () throws FHIRException {
      rule(!finished, "No more content to read");
      char value = peek();
      offset++;
      if (value == '\r' || value == '\n') {
        if (lastValue != '\r' || value != '\n')
          lineNumber++;
      }
      lastValue = value;
      try {
        next();
      } catch (Exception e) {
        throw new FHIRException(e);
      } 
      return value;
    }

    public boolean isFinished () {
      return finished;    
    }
    
    public char peek() throws FHIRException {
      rule(!finished, "Cannot peek");
      return peeked;    
    }

    public void mark() {
      stream.mark(2048);
    }
    
    public void reset() throws FHIRException {
      try {
        stream.reset();
      } catch (IOException e) {
        throw new FHIRException(e);
      }
      open();
    }

    public boolean IsEOL() throws FHIRException {
      return peek() == '\r' || peek() == '\n';
    }

    public int getLineNumber() {
      return lineNumber;
    }

    public int getOffset() {
      return offset;
    }

  }

  public VerticalBarParser(IWorkerContext context) {
    super(context);
  }

  private String charset = "ASCII";
  private Delimiters delimiters = new Delimiters();
  
  @Override
  public Element parse(InputStream stream) throws IOException, FHIRFormatError, DefinitionException, FHIRException {
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/v2/StructureDefinition/Message");
    Element message = new Element("Message", new Property(context, sd.getSnapshot().getElementFirstRep(), sd));
    VerticalBarParserReader reader = new VerticalBarParserReader(new BufferedInputStream(stream), charset);
    
    preDecode(reader);
    while (!reader.isFinished()) //  && (getOptions().getSegmentLimit() == 0 || getOptions().getSegmentLimit() > message.getSegments().size()))
      readSegment(message, reader);

    return message;
  }

  private void preDecode(VerticalBarParserReader reader) throws FHIRException {
    reader.skipEOL();
    String temp = reader.read(3);
    rule(temp.equals("MSH") || temp.equals("FHS"), "Found '" + temp + "' looking for 'MSH' or 'FHS'");
    readDelimiters(reader);
    // readVersion(message); - probably don't need to do that? 
    // readCharacterSet();
    reader.reset(); // ready to read message now
  }

  private void rule(boolean test, String msg) throws FHIRException {
    if (!test)
      throw new FHIRException(msg);    
  }

  private void readDelimiters(VerticalBarParserReader reader) throws FHIRException {
    delimiters.setFieldDelimiter(reader.read());
    if (!(reader.peek() == delimiters.getFieldDelimiter()))
      delimiters.setComponentDelimiter(reader.read());
    if (!(reader.peek() == delimiters.getFieldDelimiter()))
      delimiters.setRepetitionDelimiter(reader.read());
    if (!(reader.peek() == delimiters.getFieldDelimiter()))
      delimiters.setEscapeCharacter(reader.read());
    if (!(reader.peek() == delimiters.getFieldDelimiter()))
      delimiters.setSubComponentDelimiter(reader.read());
    delimiters.check();
  }

  private void readSegment(Element message, VerticalBarParserReader reader) throws FHIRException {
    Element segment = new Element("segment", message.getProperty().getChild("segment"));
    message.getChildren().add(segment);
    Element segmentCode = new Element("code", segment.getProperty().getChild("code"));
    segment.getChildren().add(segmentCode);
    segmentCode.setValue(reader.read(3));
    
    int index = 0;
    while (!reader.isFinished() && !reader.IsEOL()) {
      index++;
      readField(reader, segment, index);
      if (!reader.isFinished() && !reader.IsEOL())
        rule(reader.read() == delimiters.getFieldDelimiter(), "Expected to find field delimiter");
    }
    if (!reader.isFinished())
      reader.skipEOL();    
  }


  
  private void readField(VerticalBarParserReader reader, Element segment, int index) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void compose(Element e, OutputStream destination, OutputStyle style, String base) {
    // TODO Auto-generated method stub

  }

}
