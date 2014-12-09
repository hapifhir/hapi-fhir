package org.hl7.fhir.instance.model;

/*
Copyright (c) 2011+, HL7, Inc.
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

/**
 * This class is created to help implementers deal with a change to 
 * the API that was made between versions 0.81 and 0.9
 * 
 *  The change is the behaviour of the .getX() where the cardinality of 
 *  x is 0..1 or 1..1. Before the change, these routines would return 
 *  null if the object had not previously been assigned, and after the '
 *  change, they will automatically create the object if it had not 
 *  been assigned (unless the object is polymorphic, in which case, 
 *  only the type specific getters create the object)
 *  
 *  When making the transition from the old style to the new style, 
 *  the main change is that when testing for presence or abssense
 *  of the element, instead of doing one of these two: 
 *  
 *    if (obj.getProperty() == null)
 *    if (obj.getProperty() != null)
 *    
 *  you instead do 
 *  
 *    if (!obj.hasProperty())
 *    if (obj.hasProperty())
 *    
 * or else one of these two:
 *    
 *    if (obj.getProperty().isEmpty())
 *    if (!obj.getProperty().isEmpty())
 * 
 * The only way to sort this out is by finding all these things 
 * in the code, and changing them. 
 * 
 * To help with that, you can set the status field of this class
 * to change how this API behaves. Note: the status value is tied
 * to the way that you program. The intent of this class is to 
 * help make developers the transiition to status = 0. The old
 * behaviour is status = 2. To make the transition, set the 
 * status code to 1. This way, any time a .getX routine needs
 * to automatically create an object, an exception will be 
 * raised instead. The expected use of this is:
 *   - set status = 1
 *   - test your code (all paths) 
 *   - when an exception happens, change the code to use .hasX() or .isEmpty()
 *   - when all execution paths don't raise an exception, set status = 0
 *   - start programming to the new style. 
 * 
 * You can set status = 2 and leave it like that, but this is not 
 * compatible with the utilities and validation code, nor with the
 * HAPI code. So everyone shoul make this transition
 *  
 * This is a difficult change to make to an API. Most users should engage with it
 * as they migrate from DSTU1 to DSTU2, at the same time as they encounter 
 * other changes. This change was made in order to align the two java reference 
 * implementations on a common object model, which is an important outcome that
 * justifies making this change to implementers (sorry for any pain caused)
 *  
 * @author Grahame
 *
 */
public class Configuration {

	private static int status = 0;
	  // 0: auto-create  
	  // 1: error
	  // 2: return null

	public static boolean errorOnAutoCreate() {
	  return status == 1;
  }


	public static boolean doAutoCreate() {
	  return status == 0;
  }

}
