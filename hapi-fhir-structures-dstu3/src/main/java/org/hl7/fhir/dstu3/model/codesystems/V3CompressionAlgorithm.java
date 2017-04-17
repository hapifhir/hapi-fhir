package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum V3CompressionAlgorithm {

        /**
         * bzip-2 compression format. See [http://www.bzip.org/] for more information.
         */
        BZ, 
        /**
         * The deflate compressed data format as specified in RFC 1951 [http://www.ietf.org/rfc/rfc1951.txt].
         */
        DF, 
        /**
         * A compressed data format that is compatible with the widely used GZIP utility as specified in RFC 1952 [http://www.ietf.org/rfc/rfc1952.txt] (uses the deflate algorithm).
         */
        GZ, 
        /**
         * Original UNIX compress algorithm and file format using the LZC algorithm (a variant of LZW).  Patent encumbered and less efficient than deflate.
         */
        Z, 
        /**
         * 7z compression file format. See [http://www.7-zip.org/7z.html] for more information.
         */
        Z7, 
        /**
         * A compressed data format that also uses the deflate algorithm.  Specified as RFC 1950 [http://www.ietf.org/rfc/rfc1952.txt]
         */
        ZL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3CompressionAlgorithm fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BZ".equals(codeString))
          return BZ;
        if ("DF".equals(codeString))
          return DF;
        if ("GZ".equals(codeString))
          return GZ;
        if ("Z".equals(codeString))
          return Z;
        if ("Z7".equals(codeString))
          return Z7;
        if ("ZL".equals(codeString))
          return ZL;
        throw new FHIRException("Unknown V3CompressionAlgorithm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BZ: return "BZ";
            case DF: return "DF";
            case GZ: return "GZ";
            case Z: return "Z";
            case Z7: return "Z7";
            case ZL: return "ZL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/CompressionAlgorithm";
        }
        public String getDefinition() {
          switch (this) {
            case BZ: return "bzip-2 compression format. See [http://www.bzip.org/] for more information.";
            case DF: return "The deflate compressed data format as specified in RFC 1951 [http://www.ietf.org/rfc/rfc1951.txt].";
            case GZ: return "A compressed data format that is compatible with the widely used GZIP utility as specified in RFC 1952 [http://www.ietf.org/rfc/rfc1952.txt] (uses the deflate algorithm).";
            case Z: return "Original UNIX compress algorithm and file format using the LZC algorithm (a variant of LZW).  Patent encumbered and less efficient than deflate.";
            case Z7: return "7z compression file format. See [http://www.7-zip.org/7z.html] for more information.";
            case ZL: return "A compressed data format that also uses the deflate algorithm.  Specified as RFC 1950 [http://www.ietf.org/rfc/rfc1952.txt]";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BZ: return "bzip";
            case DF: return "deflate";
            case GZ: return "gzip";
            case Z: return "compress";
            case Z7: return "Z7";
            case ZL: return "zlib";
            default: return "?";
          }
    }


}

