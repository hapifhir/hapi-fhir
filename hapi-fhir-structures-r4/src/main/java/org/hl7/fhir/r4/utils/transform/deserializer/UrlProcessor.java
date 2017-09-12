//
// Translated by Java2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.DebugParseListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.ThrowExceptionErrorListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaLexer;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.UrlJavaLexer;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.UrlJavaParser;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
* Process Adl Language data.
*/
public class UrlProcessor
{
    /**
    * If true, output debug info during parsing.
    */
    private boolean __DebugFlag;
    public boolean getDebugFlag() {
        return __DebugFlag;
    }

    public void setDebugFlag(boolean value) {
        __DebugFlag = value;
    }

    /**
    * Constructor.
    *
    */
    public UrlProcessor() throws Exception {
        setDebugFlag(false);
    }

    /**
    * Method to load string grammar.
    *
    *  @param text Adl text
    *  @return ANTLR parser
    */
    public UrlJavaParser loadGrammar(String text) throws Exception {
      CharBuffer buffer = CharBuffer.allocate(text.length());
      buffer.append(text);
      buffer.position(0);

      UrlJavaLexer lexer = new UrlJavaLexer(CodePointCharStream.fromBuffer(CodePointBuffer.withChars(CharBuffer.wrap(text.toCharArray()))));
      lexer.addErrorListener(new ThrowExceptionErrorListener(text));
      CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
      UrlJavaParser grammar = new UrlJavaParser(commonTokenStream);
        if (this.getDebugFlag() == true)
        {
            DebugParseListener parseListener = new DebugParseListener(grammar, (s) ->
            {
                System.err.println(s);
            });
            grammar.addParseListener(parseListener);
        }

        grammar.removeErrorListeners();
        grammar.addErrorListener(new ThrowExceptionErrorListener(text));
        return grammar;
    }

    /**
    * Parse Adl Definition text.
    *
    *  @param UrlText Adl Archetype Text to process
    */
    public UrlData parseUrl(String UrlText) throws Exception {
        UrlJavaParser grammar = this.loadGrammar(UrlText);
        ParseTree parseTree = grammar.url();
        UrlVisitor visitor = new UrlVisitor();
        UrlData retVal = (UrlData)visitor.visit(parseTree);
        return retVal;
    }

}


