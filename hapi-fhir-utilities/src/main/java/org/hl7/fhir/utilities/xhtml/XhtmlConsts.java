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
package org.hl7.fhir.utilities.xhtml;

public class XhtmlConsts {
  public static final String ELE_HTML = "html";
  public static final String ELE_HEAD = "head";
  public static final String ELE_BODY = "body";
  public static final String ELE_TITLE = "title";
  public static final String ELE_STYLE = "style";

  public static final String ATTR_ALIGN = "align";
  public static final String ATTR_BORDER = "border";
  public static final String ATTR_CELLPADDING = "cellpadding";
  public static final String ATTR_CELLSPACING = "cellspacing";
  public static final String ATTR_CELL_COLSPAN = "colspan";
  public static final String ATTR_CELL_ROWSPAN = "rowspan";
  public static final String ATTR_CLASS = "class";
  public static final String ATTR_COLOR = "color";
  public static final String ATTR_SIZE = "size";
  public static final String ATTR_FACE = "face";
  public static final String ATTR_HEIGHT = "height";
  public static final String ATTR_HREF = "href";
  public static final String ATTR_ID = "id";
  public static final String ATTR_LISTSTARTVALUE = "start";  
  public static final String ATTR_SOURCE = "src";
  public static final String ATTR_STYLE = "style";
  public static final String ATTR_TITLE = "title";
  public static final String ATTR_TYPE = "type";
  public static final String ATTR_WIDTH = "width";

  // extended CSS attribute name
  //  CSS_ATTR_NAME = 'wpx-Name';
  //  CSS_ATTR_DISPLAYNAME = 'wpx-DisplayName';
  public static final String CSS_ATTR_READONLY = "wpx-ReadOnly";
  public static final String CSS_ATTR_DELETABLE = "wpx-Deletable";
  public static final String CSS_ATTR_ISFIELD = "wpx-IsField";
  public static final String CSS_ATTR_DATA = "wpx-Data";
  public static final String CSS_ATTR_MASK = "wpx-Mask";
  public static final String CSS_ATTR_FIXEDFORMAT = "wpx-FixedFormat";
  public static final String CSS_ATTR_VOCABULARY = "wpx-Vocabulary";
  public static final String CSS_ATTR_URL = "wpx-URL";
  public static final String CSS_ATTR_KEY = "wpx-Key";
  public static final String CSS_ATTR_LINKCOLOUR = "wpx-LinkColour";
  public static final String CSS_ATTR_HOVERCOLOUR = "wpx-HoverColour";
  //  CSS_ATTR_STYLE = 'wpx-Style';
  public static final String CSS_ATTR_FONTNAME = "font-family";
  public static final String CSS_ATTR_FONTSIZE = "font-size";
  public static final String CSS_ATTR_FONTWEIGHT = "font-weight";
  public static final String CSS_ATTR_FONTSTYLE = "font-style";
  //  CSS_ATTR_SIZE = 'wpx-Size';
  //  CSS_ATTR_BOLD = 'wpx-Bold';
  //  CSS_ATTR_ITALIC = 'wpx-Italic';
  //  CSS_ATTR_UNDERLINE = 'wpx-Underline';
  //  CSS_ATTR_STRIKETHROUGH = 'wpx-Strikethrough';
  //  CSS_ATTR_FONTSTATE = 'wpx-FontState';
  public static final String CSS_ATTR_FOREGROUND = "color";
  public static final String CSS_ATTR_BACKGROUND = "background-color";
  public static final String CSS_ATTR_ALIGN = "text-align";
  //  CSS_ATTR_ALIGNMENT = 'wpx-Alignment';
  //  CSS_ATTR_LEFTINDENT = 'wpx-LeftIndent';
  //  CSS_ATTR_RIGHTINDENT = 'wpx-RightIndent';
  public static final String CSS_ATTR_LISTTYPE = "list-style-type";
  //  CSS_ATTR_LISTTYPE = 'wpx-ListType';
  //  CSS_ATTR_NUMBERTYPE = 'wpx-NumberType';
  public static final String CSS_ATTR_NUMBERFORMAT = "wpx-NumberFormat";
  //  CSS_ATTR_FIXEDNUMBER = 'wpx-FixedNumber';
  //  CSS_ATTR_DOCUMENT = 'wpx-Document';
  //  CSS_ATTR_IMAGEREF = 'wpx-Reference';
  //  CSS_ATTR_IMAGEREF_SELECTION = 'wpx-SelectionReference';
  //  CSS_ATTR_BORDERWIDTH = 'wpx-BorderWidth';
  //  CSS_ATTR_BORDERCOLOR = 'wpx-BorderColor';
  //  CSS_ATTR_TRANSPARENTCOLOR = 'wpx-TransparentColor';
  public static final String CSS_ATTR_HEIGHT = "height";
  public static final String CSS_ATTR_WIDTH = "width";

  public static final String CSS_ATTR_BORDER = "border";
  public static final String CSS_ATTR_BORDERWIDTH = "border-width";
  public static final String CSS_ATTR_BORDERCOLOR = "border-color";
  public static final String CSS_ATTR_BORDERLEFT = "border-left";
  public static final String CSS_ATTR_BORDERLEFT_EXT = "wpx-border-left";
  public static final String CSS_ATTR_BORDERRIGHT = "border-right";
  public static final String CSS_ATTR_BORDERRIGHT_EXT = "wpx-border-right";
  public static final String CSS_ATTR_BORDERTOP = "border-top";
  public static final String CSS_ATTR_BORDERTOP_EXT = "wpx-border-top";
  public static final String CSS_ATTR_BORDERBOTTOM = "border-bottom";
  public static final String CSS_ATTR_BORDERBOTTOM_EXT = "wpx-border-bottom";
  public static final String CSS_ATTR_BORDER_VERTICAL_CENTRE = "wpx-border-vertical-centre";
  public static final String CSS_ATTR_BORDER_VERTICAL_CENTRE_EXT = "wpx-border-vertical-centre-ext";
  public static final String CSS_ATTR_BORDER_HORIZONTAL_CENTRE = "wpx-border-horizontal-centre";
  public static final String CSS_ATTR_BORDER_HORIZONTAL_CENTRE_EXT = "wpx-border-horizontal-centre-ext";
  //  CSS_ATTR_DEFINED = 'wpx-Defined';
  //  CSS_ATTR_FANCY = 'wpx-Fancy';
  //  CSS_ATTR_COLOUR = 'wpx-Colour';
  //  CSS_ATTR_OUTERCOLOUR = 'wpx-OuterColour';
  //  CSS_ATTR_OUTERCOLOUR2 = 'wpx-OuterColour2';
  public static final String CSS_ATTR_HEADER = "wpx-Header";
  public static final String CSS_ATTR_LOWER_PADDING_SIZE = "wpx-LowerPaddingSize";
  public static final String CSS_ATTR_LOWER_PADDING_COLOUR = "wpx-LowerPaddingColour";
  //public static final String CSS_ATTR_SPAN = "wpx-Span";
  public static final String CSS_ATTR_BORDERPOLICY = "wpx-BorderPolicy";
  public static final String CSS_ATTR_BREAKTYPE = "wpx-BreakType";
  //public static final String CSS_ATTR_PENSTYLE = "wpx-PenStyle";
  public static final String CSS_ATTR_PENENDSTYLE = "wpx-EndStyle";
  public static final String CSS_ATTR_MARGIN_LEFT = "margin-left";
  public static final String CSS_ATTR_MARGIN_RIGHT = "margin-right";
  public static final String CSS_ATTR_MARGIN_TOP = "margin-top";
  public static final String CSS_ATTR_MARGIN_BOTTOM = "margin-bottom";
  public static final String CSS_ATTR_DISPLAYTYPE = "wpx-DisplayType";
  public static final String CSS_ATTR_VERTICALALIGNMENT = "wpx-VerticalAlignment";
  //  CSS_ATTR_FORMAT = 'wpx-Format';
  public static final String CSS_ATTR_HORIZONTAL_MARGIN = "wpx-HorizMargin";
  public static final String CSS_ATTR_VERTICAL_MARGIN = "wpx-VertMargin";
  public static final String CSS_ATTR_TITLE = "wpx-Title";
  public static final String CSS_ATTR_TEXT_TRANSFORM = "text-transform";
  public static final String CSS_ATTR_TEXT_DECORATION = "text-decoration";
  //  CSS_ATTR_X = 'wpx-x';
  //  CSS_ATTR_Y = 'wpx-y';
  //  CSS_ATTR_LOW_OUTER = 'wpx-LowOuter';
  //  CSS_ATTR_HIGH_OUTER = 'wpx-HighOuter';
  public static final String CSS_ATTR_VERTICAL_ALIGN = "vertical-align";

  // CSS value
  public static final String CSS_VALUE_BOLD = "bold";
  public static final String CSS_VALUE_ITALIC = "italic";
  public static final String CSS_VALUE_UNDERLINE = "underline";
  public static final String CSS_VALUE_LINE_THROUGH = "line-through";
  public static final String CSS_VALUE_UPPER_CASE = "uppercase";
  public static final String CSS_VALUE_LOWER_CASE = "lowercase";
  public static final String CSS_VALUE_NONE = "none";
  public static final String CSS_VALUE_ALIGN_SUPER = "super";
  public static final String CSS_VALUE_ALIGN_SUB = "sub";

  public static final String CSS_VALUE_DECIMAL = "decimal";
  public static final String CSS_VALUE_LOWERALPHA = "lower-alpha";
  public static final String CSS_VALUE_UPPERALPHA = "upper-alpha";
  public static final String CSS_VALUE_LOWERROMAN = "lower-roman";
  public static final String CSS_VALUE_UPPERROMAN = "upper-roman";

}
