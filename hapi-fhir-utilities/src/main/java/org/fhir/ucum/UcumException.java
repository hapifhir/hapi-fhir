
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

public class UcumException extends Exception {

  public UcumException() {
    super();
  }

  public UcumException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public UcumException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public UcumException(String arg0) {
    super(arg0);
  }

  public UcumException(Throwable arg0) {
    super(arg0);
  }

 

}
