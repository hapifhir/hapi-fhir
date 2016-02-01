/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

 */
package org.hl7.fhir.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.exceptions.FHIRException;

public class Utilities {

//	 private static final String TOKEN_REGEX = "^a-z[A-Za-z0-9]*$";


  private static final String OID_REGEX = "[0-2](\\.(0|[1-9]([0-9])*))*";

  /**
     * Returns the plural form of the word in the string.
     * 
     * Examples:
     * 
     * <pre>
     *   inflector.pluralize(&quot;post&quot;)               #=&gt; &quot;posts&quot;
     *   inflector.pluralize(&quot;octopus&quot;)            #=&gt; &quot;octopi&quot;
     *   inflector.pluralize(&quot;sheep&quot;)              #=&gt; &quot;sheep&quot;
     *   inflector.pluralize(&quot;words&quot;)              #=&gt; &quot;words&quot;
     *   inflector.pluralize(&quot;the blue mailman&quot;)   #=&gt; &quot;the blue mailmen&quot;
     *   inflector.pluralize(&quot;CamelOctopus&quot;)       #=&gt; &quot;CamelOctopi&quot;
     * </pre>
     * 
     * 
     * 
     * Note that if the {@link Object#toString()} is called on the supplied object, so this method works for non-strings, too.
     * 
     * 
     * @param word the word that is to be pluralized.
     * @return the pluralized form of the word, or the word itself if it could not be pluralized
     * @see #singularize(Object)
     */
    public static String pluralizeMe( String word ) {
    	Inflector inf = new Inflector();
    	return inf.pluralize(word);
    }
    
  
  	public static boolean isInteger(String string) {
  		try {
  			int i = Integer.parseInt(string);
  			return i != i+1;
  		} catch (Exception e) {
  			return false;
  		}
  	}
  	
  	public static boolean isDecimal(String string) {
  		try {
  			float r = Float.parseFloat(string);
  			return r != r + 1; // just to suppress the hint
  		} catch (Exception e) {
  			return false;
  		}
  	}
  	
	public static String camelCase(String value) {
	  return new Inflector().camelCase(value.trim().replace(" ", "_"), false);
	}
	
	public static String escapeXml(String doco) {
		if (doco == null)
			return "";
		
		StringBuilder b = new StringBuilder();
		for (char c : doco.toCharArray()) {
		  if (c == '<')
			  b.append("&lt;");
		  else if (c == '>')
			  b.append("&gt;");
		  else if (c == '&')
			  b.append("&amp;");
      else if (c == '"')
        b.append("&quot;");
		  else 
			  b.append(c);
		}		
		return b.toString();
	}

	
	public static String capitalize(String s)
	{
		if( s == null ) return null;
		if( s.length() == 0 ) return s;
		if( s.length() == 1 ) return s.toUpperCase();
		
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
  public static void copyDirectory(String sourceFolder, String destFolder, FileNotifier notifier) throws IOException, FHIRException  {
    CSFile src = new CSFile(sourceFolder);
    if (!src.exists())
      throw new FHIRException("Folder " +sourceFolder+" not found");
    createDirectory(destFolder);
    
   String[] files = src.list();
   for (String f : files) {
     if (new CSFile(sourceFolder+File.separator+f).isDirectory()) {
       if (!f.startsWith(".")) // ignore .svn...
         copyDirectory(sourceFolder+File.separator+f, destFolder+File.separator+f, notifier);
     } else {
       if (notifier != null)
         notifier.copyFile(sourceFolder+File.separator+f, destFolder+File.separator+f);
       copyFile(new CSFile(sourceFolder+File.separator+f), new CSFile(destFolder+File.separator+f));
     }
   }
  }
	
  public static void copyFile(String source, String dest) throws IOException {
    copyFile(new File(source), new File(dest));
  }

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()) {
			if (!new CSFile(destFile.getParent()).exists()) {
				createDirectory(destFile.getParent());
			}
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}

  public static boolean checkFolder(String dir, List<String> errors)
  	throws IOException
  {
	  if (!new CSFile(dir).exists()) {
      errors.add("Unable to find directory "+dir);
      return false;
    } else {
      return true;
    }
  }

  public static boolean checkFile(String purpose, String dir, String file, List<String> errors) 
  	throws IOException
  {
    if (!new CSFile(dir+file).exists()) {
      errors.add("Unable to find "+purpose+" file "+file+" in "+dir);
      return false;
    } else {
      return true;
    }
  }

  public static String asCSV(List<String> strings) {
    StringBuilder s = new StringBuilder();
    boolean first = true;
    for (String n : strings) {
      if (!first)
        s.append(",");
      s.append(n);
      first = false;
    }
    return s.toString();
  }

  public static String asHtmlBr(String prefix, List<String> strings) {
    StringBuilder s = new StringBuilder();
    boolean first = true;
    for (String n : strings) {
      if (!first)
        s.append("<br/>");
      s.append(prefix);
      s.append(n);
      first = false;
    }
    return s.toString();
  }

  public static void clearDirectory(String folder) throws IOException {
    File dir = new File(folder);
    if (dir.exists())
      FileUtils.cleanDirectory(dir);
//	  String[] files = new CSFile(folder).list();
//	  if (files != null) {
//		  for (String f : files) {
//			  File fh = new CSFile(folder+File.separatorChar+f);
//			  if (fh.isDirectory()) 
//				  clearDirectory(fh.getAbsolutePath());
//			  fh.delete();
//		  }
//	  }
  }

  public static void createDirectory(String path) throws IOException{
    new CSFile(path).mkdirs();    
  }

  public static String changeFileExt(String name, String ext) {
    if (name.lastIndexOf('.') > -1)
      return name.substring(0, name.lastIndexOf('.')) + ext;
    else
      return name+ext;
  }
  
  public static String cleanupTextString( String contents )
  {
	  if( contents == null || contents.trim().equals("") )
		  return null;
	  else
		  return contents.trim();
  }


  public static boolean noString(String v) {
    return v == null || v.equals("");
  }


  public static byte[] saxonTransform(Map<String, byte[]> files, byte[] source, byte[] xslt) throws TransformerException  {
    TransformerFactory f = new net.sf.saxon.TransformerFactoryImpl();
    f.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
    StreamSource xsrc = new StreamSource(new ByteArrayInputStream(xslt));
    f.setURIResolver(new ZipURIResolver(files));
    Transformer t = f.newTransformer(xsrc);
 
    t.setURIResolver(new ZipURIResolver(files));
    StreamSource src = new StreamSource(new ByteArrayInputStream(source));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamResult res = new StreamResult(out);
    t.transform(src, res);
    return out.toByteArray();    
  }
  
  public static byte[] transform(Map<String, byte[]> files, byte[] source, byte[] xslt) throws TransformerException  {
    TransformerFactory f = TransformerFactory.newInstance();
    f.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
    StreamSource xsrc = new StreamSource(new ByteArrayInputStream(xslt));
    f.setURIResolver(new ZipURIResolver(files));
    Transformer t = f.newTransformer(xsrc);

    t.setURIResolver(new ZipURIResolver(files));
    StreamSource src = new StreamSource(new ByteArrayInputStream(source));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamResult res = new StreamResult(out);
    t.transform(src, res);
    return out.toByteArray();    
  }
  
  public static void bytesToFile(byte[] content, String filename) throws IOException  {
    FileOutputStream out = new FileOutputStream(filename);
    out.write(content);
    out.close();
    
  }

  public static String saxonTransform(String source, String xslt) throws TransformerException, FileNotFoundException  {
    TransformerFactoryImpl f = new net.sf.saxon.TransformerFactoryImpl();
    f.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
    StreamSource xsrc = new StreamSource(new FileInputStream(xslt));
    Transformer t = f.newTransformer(xsrc);
    StreamSource src = new StreamSource(new FileInputStream(source));
    StreamResult res = new StreamResult(new ByteArrayOutputStream());
    t.transform(src, res);
    return res.getOutputStream().toString();   
  }

  public static void saxonTransform(String xsltDir, String source, String xslt, String dest, URIResolver alt) throws FileNotFoundException, TransformerException  {
  	saxonTransform(xsltDir, source, xslt, dest, alt, null);
  }

  public static void saxonTransform(String xsltDir, String source, String xslt, String dest, URIResolver alt, Map<String, String> params) throws FileNotFoundException, TransformerException  {
    TransformerFactoryImpl f = new net.sf.saxon.TransformerFactoryImpl();
    f.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
    StreamSource xsrc = new StreamSource(new FileInputStream(xslt));
    f.setURIResolver(new MyURIResolver(xsltDir, alt));
    Transformer t = f.newTransformer(xsrc);
 		if (params != null) {
 			for (Map.Entry<String, String> entry : params.entrySet()) {
 				t.setParameter(entry.getKey(), entry.getValue());
 			}
  	}
    
    t.setURIResolver(new MyURIResolver(xsltDir, alt));
    StreamSource src = new StreamSource(new FileInputStream(source));
    StreamResult res = new StreamResult(new FileOutputStream(dest));
    t.transform(src, res);    
  }
  
  public static void transform(String xsltDir, String source, String xslt, String dest, URIResolver alt) throws FileNotFoundException, TransformerException  {

    TransformerFactory f = TransformerFactory.newInstance();
    StreamSource xsrc = new StreamSource(new FileInputStream(xslt));
    f.setURIResolver(new MyURIResolver(xsltDir, alt));
    Transformer t = f.newTransformer(xsrc);

    t.setURIResolver(new MyURIResolver(xsltDir, alt));
    StreamSource src = new StreamSource(new FileInputStream(source));
    StreamResult res = new StreamResult(new FileOutputStream(dest));
    t.transform(src, res);
    
  }


  public static String appendSlash(String definitions) {
	    return definitions.endsWith(File.separator) ? definitions : definitions+File.separator;
	  }

  public static String appendForwardSlash(String definitions) {
	    return definitions.endsWith("/") ? definitions : definitions+"/";
	  }


  public static String fileTitle(String file) {
    if (file == null)
      return null;
    String s = new File(file).getName();
    return s.indexOf(".") == -1? s : s.substring(0, s.indexOf("."));
  }


  public static String systemEol()
  {
	  return System.getProperty("line.separator");
  }

  public static String normaliseEolns(String value) {
    return value.replace("\r\n", "\r").replace("\n", "\r").replace("\r", "\r\n");
  }


  public static String unescapeXml(String xml) throws FHIRException  {
    if (xml == null)
      return null;
    
    StringBuilder b = new StringBuilder();
    int i = 0;
    while (i < xml.length()) {
      if (xml.charAt(i) == '&') {
        StringBuilder e = new StringBuilder();
        i++;
        while (xml.charAt(i) != ';') {
          e.append(xml.charAt(i));
          i++;
        }
        if (e.toString().equals("lt")) 
          b.append("<");
        else if (e.toString().equals("gt")) 
          b.append(">");
        else if (e.toString().equals("amp")) 
          b.append("&");
        else if (e.toString().equals("quot")) 
          b.append("\"");
        else if (e.toString().equals("mu"))
          b.append((char)956);          
        else
          throw new FHIRException("unknown XML entity \""+e.toString()+"\"");
      }  else
        b.append(xml.charAt(i));
      i++;
    }   
    return b.toString();
  }


  public static boolean isPlural(String word) {
    word = word.toLowerCase();
    if ("restricts".equals(word) || "contains".equals(word) || "data".equals(word) || "specimen".equals(word))
      return false;
    Inflector inf = new Inflector();
    return !inf.singularize(word).equals(word);
  }


  public static String padLeft(String src, char c, int len) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < len - src.length(); i++)
      s.append(c);
    s.append(src);
    return s.toString();
    
  }


  public static String path(String... args) {
    StringBuilder s = new StringBuilder();
    boolean d = false;
    for(String arg: args) {
      if (!d)
        d = !noString(arg);
      else if (!s.toString().endsWith(File.separator))
        s.append(File.separator);
      String a = arg;
      a = a.replace("\\", File.separator);
      if (s.length() > 0 && a.startsWith(File.separator))
        a = a.substring(File.separator.length());
        
      if ("..".equals(a)) {
        int i = s.substring(0, s.length()-1).lastIndexOf(File.separator);
        s = new StringBuilder(s.substring(0, i+1));
      } else
        s.append(a);
    }
    return s.toString();
  }

  public static String pathReverse(String... args) {
    StringBuilder s = new StringBuilder();
    boolean d = false;
    for(String arg: args) {
      if (!d)
        d = !noString(arg);
      else if (!s.toString().endsWith("/"))
        s.append("/");
      s.append(arg);
    }
    return s.toString();
  }



//  public static void checkCase(String filename) {
//    File f = new CSFile(filename);
//    if (!f.getName().equals(filename))
//      throw new FHIRException("Filename  ")
//    
//  }

  public static String nmtokenize(String cs) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < cs.length(); i++) {
      char c = cs.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_')
        s.append(c);
      else if (c != ' ')
        s.append("."+Integer.toString(c));
    }
    return s.toString();
  }


  public static boolean isToken(String tail) {
    if (tail == null || tail.length() == 0)
      return false;
    boolean result = isAlphabetic(tail.charAt(0));
    for (int i = 1; i < tail.length(); i++) {
    	result = result && (isAlphabetic(tail.charAt(i)) || isDigit(tail.charAt(i)) || (tail.charAt(i) == '_')  || (tail.charAt(i) == '[') || (tail.charAt(i) == ']'));
    }
    return result;
  }


  private static boolean isDigit(char c) {
    return (c >= '0') && (c <= '9');
  }


  private static boolean isAlphabetic(char c) {
    return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
  }


  public static String getDirectoryForFile(String filepath) {
    File f = new File(filepath);
    return f.getParent();
  }

  public static String appendPeriod(String s) {
    if (Utilities.noString(s))
      return s;
    s = s.trim();
    if (s.endsWith(".") || s.endsWith("?"))
      return s;
    return s+".";
  }


  public static String removePeriod(String s) {
    if (Utilities.noString(s))
      return s;
    if (s.endsWith("."))
      return s.substring(0, s.length()-1);
    return s;
  }


	public static String stripBOM(String string) {
	  return string.replace("\uFEFF", "");
  }


  public static String oidTail(String id) {
    if (id == null || !id.contains("."))
      return id;
    return id.substring(id.lastIndexOf(".")+1);
  }


  public static String oidRoot(String id) {
    if (id == null || !id.contains("."))
      return id;
    return id.substring(0, id.indexOf("."));
  }

  public static String escapeJava(String doco) {
    if (doco == null)
      return "";
    
    StringBuilder b = new StringBuilder();
    for (char c : doco.toCharArray()) {
      if (c == '\r')
        b.append("\\r");
      else if (c == '\n')
        b.append("\\n");
      else if (c == '"')
        b.append("\\\"");
      else if (c == '\\')
        b.append("\\\\");
      else 
        b.append(c);
    }   
    return b.toString();
  }


  public static String[] splitByCamelCase(String name) {
    List<String> parts = new ArrayList<String>();
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < name.length(); i++) {
      if (i > 0 && Character.isUpperCase(name.charAt(i))) {
        parts.add(b.toString());
        b = new StringBuilder();
      }
      b.append(Character.toLowerCase(name.charAt(i)));
    }
    parts.add(b.toString());
    return parts.toArray(new String[] {} );
  }


  public static String encodeUri(String v) {
    return v.replace(" ", "%20").replace("?", "%3F").replace("=", "%3D");
  }
  


	public static String normalize(String s) {
		if (noString(s))
			return null;
	  StringBuilder b = new StringBuilder();
	  boolean isWhitespace = false;
	  for (int i = 0; i < s.length(); i++) {
	  	char c = s.charAt(i);
	  	if (!Character.isWhitespace(c)) { 
	  		b.append(Character.toLowerCase(c));
	  		isWhitespace = false;
	  	} else if (!isWhitespace) {
	  		b.append(' ');
	  		isWhitespace = true;	  		
	  	} 
	  }
	  return b.toString().trim();
  }

  public static String normalizeSameCase(String s) {
    if (noString(s))
      return null;
    StringBuilder b = new StringBuilder();
    boolean isWhitespace = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!Character.isWhitespace(c)) { 
        b.append(c);
        isWhitespace = false;
      } else if (!isWhitespace) {
        b.append(' ');
        isWhitespace = true;        
      } 
    }
    return b.toString().trim();
  }


  public static void copyFileToDirectory(File source, File destDir) throws IOException {
  	copyFile(source, new File(path(destDir.getAbsolutePath(), source.getName())));
  }


	public static boolean isWhitespace(String s) {
	  boolean ok = true;
	  for (int i = 0; i < s.length(); i++)
	  	ok = ok && Character.isWhitespace(s.charAt(i));
	  return ok;
	  
  }


  public static String URLEncode(String string) {
    try {
      return URLEncoder.encode(string, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new Error(e.getMessage());
    }
  }


  public static boolean existsInList(String value, String... array) {
    for (String s : array)
      if (value.equals(s))
          return true;
    return false;
  }

  public static boolean existsInListNC(String value, String... array) {
    for (String s : array)
      if (value.equalsIgnoreCase(s))
          return true;
    return false;
  }


  public static String getFileNameForName(String name) {
    return name.toLowerCase();
  }

  public static void deleteTempFiles() throws IOException {
    File file = createTempFile("test", "test");
    String folder = getDirectoryForFile(file.getAbsolutePath());
    String[] list = new File(folder).list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.startsWith("ohfu-");
      }
    });
    if (list != null) {
      for (String n : list) {
        new File(path(folder, n)).delete();
      }
    }
  }

  public static File createTempFile(String prefix, String suffix) throws IOException {
 // this allows use to eaily identify all our dtemp files and delete them, since delete on Exit doesn't really work.
    File file = File.createTempFile("ohfu-"+prefix, suffix);  
    file.deleteOnExit();
    return file;
  }


	public static boolean isAsciiChar(char ch) {
		return ch >= ' ' && ch <= '~'; 
  }


  public static String makeUuidUrn() {
    return "urn:uuid:"+UUID.randomUUID().toString().toLowerCase();
  }

  public static boolean isURL(String s) {
    boolean ok = s.matches("^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*");
    return ok;
 }


  public static String escapeJson(String value) {
    if (value == null)
      return "";
    
    StringBuilder b = new StringBuilder();
    for (char c : value.toCharArray()) {
      if (c == '\r')
        b.append("\\r");
      else if (c == '\n')
        b.append("\\n");
      else if (c == '"')
        b.append("\\\"");
      else if (c == '\'')
        b.append("\\'");
      else if (c == '\\')
        b.append("\\\\");
      else 
        b.append(c);
    }   
    return b.toString();
  }

  public static String humanize(String code) {
    StringBuilder b = new StringBuilder();
    boolean lastBreak = true;
    for (char c : code.toCharArray()) {
      if (Character.isLetter(c)) {
        if (lastBreak)
          b.append(Character.toUpperCase(c));
        else { 
          if (Character.isUpperCase(c))
            b.append(" ");          
          b.append(c);
        }
        lastBreak = false;
      } else {
        b.append(" ");
        lastBreak = true;
      }
    }
    if (b.length() == 0)
      return code;
    else 
      return b.toString();
  }


  public static String uncapitalize(String s) {
    if( s == null ) return null;
    if( s.length() == 0 ) return s;
    if( s.length() == 1 ) return s.toLowerCase();
    
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }


  public static int charCount(String s, char c) {
	  int res = 0;
	  for (char ch : s.toCharArray())
		if (ch == c)
		  res++;
	  return res;
  }


  // http://stackoverflow.com/questions/3780406/how-to-play-a-sound-alert-in-a-java-application
  public static float SAMPLE_RATE = 8000f;
  
  public static void tone(int hz, int msecs) {
      tone(hz, msecs, 1.0);
   }

  public static void tone(int hz, int msecs, double vol) {
    try {
      byte[] buf = new byte[1];
      AudioFormat af = 
          new AudioFormat(
              SAMPLE_RATE, // sampleRate
              8,           // sampleSizeInBits
              1,           // channels
              true,        // signed
              false);      // bigEndian
      SourceDataLine sdl;
      sdl = AudioSystem.getSourceDataLine(af);
      sdl.open(af);
      sdl.start();
      for (int i=0; i < msecs*8; i++) {
        double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
        buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
        sdl.write(buf,0,1);
      }
      sdl.drain();
      sdl.stop();
      sdl.close();
    } catch (Exception e) {
    }
  }


  public static boolean isOid(String cc) {
    return cc.matches(OID_REGEX) && cc.lastIndexOf('.') > 5;
  }


  public static boolean equals(String one, String two) {
    if (one == null && two == null)
      return true;
    if (one == null || two == null)
      return false;
    return one.equals(two);
  }


  public static void deleteAllFiles(String folder, String type) {
    File src = new File(folder);
    String[] files = src.list();
    for (String f : files) {
      if (new File(folder+File.separator+f).isDirectory()) {
        deleteAllFiles(folder+File.separator+f, type);
      } else if (f.endsWith(type)) {
        new File(folder+File.separator+f).delete();
      }
    }
    
  }

  public static boolean compareIgnoreWhitespace(File f1, File f2) throws IOException {
    InputStream in1 = null;
    InputStream in2 = null;
    try {
      in1 = new BufferedInputStream(new FileInputStream(f1));
      in2 = new BufferedInputStream(new FileInputStream(f2));

      int expectedByte = in1.read();
      while (expectedByte != -1) {
        boolean w1 = isWhitespace(expectedByte);
        if (w1)
          while (isWhitespace(expectedByte))
            expectedByte = in1.read();
        int foundByte = in2.read();
        if (w1) {
          if (!isWhitespace(foundByte))
            return false;
          while (isWhitespace(foundByte))
            foundByte = in2.read();
        }
        if (expectedByte != foundByte) 
          return false;
        expectedByte = in1.read();
      }
      if (in2.read() != -1) {
        return false;
      }
      return true;
    } finally {
      if (in1 != null) {
        try {
          in1.close();
        } catch (IOException e) {}
      }
      if (in2 != null) {
        try {
          in2.close();
        } catch (IOException e) {}
      }
    }
  }
  
  private static boolean isWhitespace(int b) {
    return b == 9 || b == 10 || b == 13 || b == 32;
  }


  public static boolean compareIgnoreWhitespace(String fn1, String fn2) throws IOException {
    return compareIgnoreWhitespace(new File(fn1), new File(fn2));
  }


  public static boolean isAbsoluteUrl(String ref) {
    return ref.startsWith("http:") || ref.startsWith("https:") || ref.startsWith("urn:uuid:") || ref.startsWith("urn:oid:") ;
  }


}
