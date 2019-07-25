package org.hl7.fhir.utilities;

import java.io.*;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

/**
 * Utiltities for working with Saxon
 */
public class SaxonUtilities {

  /*
   * Note: this functionality was removed from Utilities.java in order to prevent users of that
   * class from needing to import unneeded libraries (sound, saxon, etc.)
   */

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
  
}
