
/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Health Intersections - ongoing maintenance
 *******************************************************************************/

package org.fhir.ucum;

public class Utilities {
  public static boolean isWhitespace(String s) {
    boolean ok = true;
    for (int i = 0; i < s.length(); i++)
      ok = ok && Character.isWhitespace(s.charAt(i));
    return ok;
    
  }

  public static boolean isDecimal(String string) {
    if (Utilities.noString(string))
      return false;
    try {
      float r = Float.parseFloat(string);
      return r != r + 1; // just to suppress the hint
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isInteger(String string) {
    try {
      int i = Integer.parseInt(string);
      return i != i+1;
    } catch (Exception e) {
      return false;
    }
  }
  
  public static boolean isHex(String string) {
    try {
      int i = Integer.parseInt(string, 16);
      return i != i+1;
    } catch (Exception e) {
      return false;
    }
  }
  

  public static boolean noString(String v) {
    return v == null || v.equals("");
  }

  public static String padLeft(String src, char c, int len) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < len - src.length(); i++)
      s.append(c);
    s.append(src);
    return s.toString();
  }

  public static boolean isAsciiChar(char ch) {
    return ch >= ' ' && ch <= '~'; 
  }



}
