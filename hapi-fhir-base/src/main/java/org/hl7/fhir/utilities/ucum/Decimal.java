package org.hl7.fhir.utilities.ucum;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.hl7.fhir.exceptions.UcumException;
import org.hl7.fhir.utilities.Utilities;

/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation (pascal)
 *    Health Intersections P/L - port to Java
 *******************************************************************************/

/**
    Precision aware Decimal implementation. Any size number with any number of significant digits is supported.

    Note that operations are precision aware operations. Note that whole numbers are assumed to have
    unlimited precision. For example:
      2 x 2 = 4
      2.0 x 2.0 = 4.0
      2.00 x 2.0 = 4.0
    and
     10 / 3 = 3.33333333333333333333333333333333333333333333333
     10.0 / 3 = 3.33
     10.00 / 3 = 3.333
     10.00 / 3.0 = 3.3
     10 / 3.0 = 3.3

    Addition
      2 + 0.001 = 2.001
      2.0 + 0.001 = 2.0

    Note that the string representation is precision limited, but the internal representation
    is not.

    
  * This class is defined to work around the limitations of Java Big Decimal
 * 
 * @author Grahame
 *
 */
public class Decimal {

	private int precision;
	private boolean scientific;
	private boolean negative;
	private String digits;
	private int decimal;

	private Decimal() {
		super();
	}
	
	public Decimal(String value) throws UcumException  {
		super();
		value = value.toLowerCase();
		if (value.contains("e"))
			setValueScientific(value);
		else
			setValueDecimal(value);
	}

	/**
	 * There are a few circumstances where a simple value is known to be correct to a high
	 * precision. For instance, the unit prefix milli is not ~0.001, it is precisely 0.001
	 * to whatever precision you want to specify. This constructor allows you to specify 
	 * an alternative precision than the one implied by the stated string
	 * 
	 * @param value - a string representation of the value
	 * @param precision - a 
	 * @throws UcumException 
	 */
	public Decimal(String value, int precision) throws UcumException  {
		super();
		value = value.toLowerCase();
		if (value.contains("e"))
			setValueScientific(value);
		else
			setValueDecimal(value);
		this.precision = precision;
	}

	public Decimal(int i) {
		super();
		try {
	    setValueDecimal(Integer.toString(i));
    } catch (Exception e) {
    }
  }

	private void setValueDecimal(String value) throws UcumException  {
		//	var
		//	  dec : integer;
		//	  i : integer;
		scientific = false;
		int dec = -1;
		negative = value.startsWith("-");
		if (negative)
			value = value.substring(1);

		while (value.startsWith("0") && value.length() > 1)
			value = value.substring(1);

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '.' && dec == -1)
				dec = i;
			else if (!Character.isDigit(value.charAt(i)))
				throw new UcumException("'"+value+"' is not a valid decimal");
		}

		if (dec == -1) {
			precision = value.length();
			decimal = value.length();
			digits = value;
		} else if (dec == value.length() -1)
			throw new UcumException("'"+value+"' is not a valid decimal");
		else {
			decimal = dec;
			if (allZeros(value, 1))
				precision = value.length() - 1;
			else
				precision = countSignificants(value);
			digits = delete(value, decimal, 1);
			if (allZeros(digits, 0))
				precision++;
			else
				while (digits.charAt(0) == '0') {
					digits = digits.substring(1);
					decimal--;
				}
		}
	}

	private boolean allZeros(String s, int start) {
		boolean result = true;
		for (int i = start; i < s.length(); i++) {
			if (s.charAt(i) != '0')
				result = false;
		}
		return result;
	}

	private int countSignificants(String value) {
		int i = value.indexOf(".");
		if (i > -1)
			value = delete(value, i, 1);
		while (value.charAt(0) == '0')
			value = value.substring(1);
		return value.length();
	}

	private String delete(String value, int offset, int length) {
		if (offset == 0){
			return value.substring(length);
		}
		return value.substring(0,  offset)+value.substring(offset+length);
	}
	
	private void setValueScientific(String value) throws UcumException  {
		int i = value.indexOf("e");
		String s = value.substring(0, i);
		String e = value.substring(i+1);
				
	  if (Utilities.noString(s) || s.equals("-") || !Utilities.isDecimal(s))
	    throw new UcumException("'"+value+"' is not a valid decimal (numeric)");
	  if (Utilities.noString(e) || e.equals("-") || !Utilities.isInteger(e))
	    throw new UcumException("'"+value+"' is not a valid decimal (exponent)");

	  setValueDecimal(s);
	  scientific = true;

	  // now adjust for exponent

	  if (e.charAt(0) == '-')
	    i = 1;
	  else
	    i = 0;
	  while (i < e.length()) {
	    if (!Character.isDigit(e.charAt(i)))
	      throw new UcumException(""+value+"' is not a valid decimal");
	    i++;
	  }
	  i = Integer.parseInt(e);
	  decimal = decimal + i;
	}

	private String stringMultiply(char c, int i) {
	  return Utilities.padLeft("", c, i);
  }

	private String insert(String ins, String value, int offset) {
		if (offset == 0) {
			return ins+value;
		}
		return value.substring(0, offset)+ins+value.substring(offset);
  }

	@Override
  public String toString() {
	  return asDecimal();
  }

	public Decimal copy() {
		Decimal result = new Decimal();
		result.precision = precision;
		result.scientific = scientific;
		result.negative = negative;
		result.digits = digits;
		result.decimal = decimal;
		return result;
	}

	public static Decimal zero()  {
		try {
			return new Decimal("0");
		} catch (Exception e) {
			return null; // won't happen
		}
	}

	public boolean isZero() {
	  return allZeros(digits, 0);
	}
	
	public static Decimal one() {
		try {
			return new Decimal("1");
		} catch (Exception e) {
			return null; // won't happen
		}
	}

	public boolean isOne() {
	  Decimal one = one();
	  return comparesTo(one) == 0;
	}

	public boolean equals(Decimal other) {
		return comparesTo(other) == 0;
	}
	
	public int comparesTo(Decimal other) {
		//	  s1, s2 : AnsiString;
		if (other == null)
			return 0;

		if (this.negative && !other.negative)
			return -1;
		else if (!this.negative && other.negative)
			return 1;
		else {
			int max = Math.max(this.decimal, other.decimal);
			String s1 = stringMultiply('0', max - this.decimal+1) + this.digits;
			String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
			if (s1.length() < s2.length()) 
				s1 = s1 + stringMultiply('0', s2.length() - s1.length());
				else if (s2.length() < s1.length()) 
					s2 = s2 + stringMultiply('0', s1.length() - s2.length());
			int result = s1.compareTo(s2);
			if (this.negative)
				result = -result;
			return result;
		}
	}

	public boolean isWholeNumber() {
	  return !asDecimal().contains(".");
	}

	public String asDecimal() {
		String result = digits;
		if (decimal != digits.length())
			if (decimal < 0)
				result = "0."+stringMultiply('0', 0-decimal)+digits;
			else if (decimal < result.length())
				if (decimal == 0) 
					result = "0."+result;
				else
					result = insert(".", result, decimal);
			else
				result = result + stringMultiply('0', decimal - result.length());
		if (negative && !allZeros(result, 0))
			result = "-" + result;
		return result;
	}

	public int asInteger() throws UcumException  {
	  if (!isWholeNumber())
	    throw new UcumException("Unable to represent "+toString()+" as an integer");
	  if (comparesTo(new Decimal(Integer.MIN_VALUE)) < 0)
	  	throw new UcumException("Unable to represent "+toString()+" as a signed 8 byte integer");
	  if (comparesTo(new Decimal(Integer.MAX_VALUE)) > 0)
	  	throw new UcumException("Unable to represent "+toString()+" as a signed 8 byte integer");
	  return Integer.parseInt(asDecimal());
  }

	public String asScientific() {
	  String result = digits;
	  boolean zero = allZeros(result, 0);
	  if (zero) {
	    if (precision < 2)
	      result = "0e0";
	    else
	      result = "0."+stringMultiply('0', precision-1)+"e0";
	  } else {
	    if (digits.length() > 1)
	      result = insert(".", result, 1);
	    result = result + 'e'+Integer.toString(decimal - 1);
	  }
	  if (negative && !zero)
	    result = '-' + result;
	  return result;
  }

	public Decimal trunc() {
	  if (decimal < 0)
    return zero();

    Decimal result = copy();
    if (result.digits.length() >= result.decimal)
    	result.digits = result.digits.substring(0, result.decimal);
    if (Utilities.noString(result.digits)) {
      result.digits = "0";
      result.decimal = 1;
      result.negative = false;
    }
	  return result;
  }


	public Decimal add(Decimal other) {
	  if (other == null) 
	    return null;
	  
	  if (negative == other.negative) {
	    Decimal result = doAdd(other);
	    result.negative = negative;
	    return result;
	  } else if (negative) 
	    return other.doSubtract(this);
	  else
	  	return doSubtract(other);
	}

	public Decimal subtract(Decimal other) {
	  if (other == null) 
	    return null;

	  Decimal result;
    if (negative && !other.negative) {
	    result = doAdd(other);
	    result.negative = true;
    } else if (!negative && other.negative) {
	    result = doAdd(other);
    } else if (negative && other.negative) {
	    result = doSubtract(other);
	    result.negative = !result.negative;
    } else { 
	    result = other.doSubtract(this);
	    result.negative = !result.negative;
    }
    return result;
	}

	
	private Decimal doAdd(Decimal other) {
	  int max = Math.max(decimal, other.decimal);
	  String s1 = stringMultiply('0', max - decimal+1) + digits;
	  String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
	  if (s1.length() < s2.length())
	    s1 = s1 + stringMultiply('0', s2.length() - s1.length());
	  else if (s2.length() < s1.length())
	    s2 = s2 + stringMultiply('0', s1.length() - s2.length());

	  String s3 = stringAddition(s1, s2);

	  if (s3.charAt(0) == '1')
	    max++;
	  else
	    s3 = delete(s3, 0, 1);
	  
	  if (max != s3.length()) {
	    if (max < 0)
	      throw new Error("Unhandled");
	    else if (max < s3.length())
	      s3 = insert(".", s3, max);
	    else
	      throw new Error("Unhandled");
	  }
	  
	  Decimal result = new Decimal();
	  try {
	    result.setValueDecimal(s3);
    } catch (Exception e) {
    	// won't happen
    }
	  result.scientific = scientific || other.scientific;
	  // todo: the problem with this is you have to figure out the absolute precision and take the lower of the two, not the relative one
	  if (decimal < other.decimal)
	    result.precision = precision;
	  else if (other.decimal < decimal)
	    result.precision = other.precision;
	  else
	    result.precision = Math.min(precision, other.precision);
	  return result;
	}

	private int dig(char c) {
	  return (c) - ('0');
  }

	private char cdig(int i) {
	  return (char) (i + ('0'));
  }

	private Decimal doSubtract(Decimal other)  {
	  int max = Math.max(decimal, other.decimal);
	  String s1 = stringMultiply('0', max - decimal+1) + digits;
	  String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
	  if (s1.length() < s2.length())
	    s1 = s1 + stringMultiply('0', s2.length() - s1.length());
	  else if (s2.length() < s1.length())
	    s2 = s2 + stringMultiply('0', s1.length() - s2.length());

	  String s3;
	  boolean neg = (s1.compareTo(s2) <  0);
	  if (neg) {
	    s3 = s2;
	    s2 = s1;
	    s1 = s3;
	  }

	  s3 = stringSubtraction(s1, s2);

	  if (s3.charAt(0) == '1')
	    max++;
	  else
	    s3 = delete(s3, 0, 1);
	  if (max != s3.length()) {
	    if (max < 0)
	      throw new Error("Unhandled");
	    else if (max < s3.length())
	      s3 = insert(".", s3, max);
	    else
	      throw new Error("Unhandled");
	  }

	  Decimal result = new Decimal();
	  try {
	    result.setValueDecimal(s3);
    } catch (Exception e) {
    	// won't happen
    }
	  result.negative = neg;
	  result.scientific = scientific || other.scientific;
	  if (decimal < other.decimal)
	  	result.precision = precision;
	  else if (other.decimal < decimal)
	  	result.precision = other.precision;
	  else
	  	result.precision = Math.min(precision, other.precision);
	  return result;
	}

	private String stringAddition(String s1, String s2) {
	  assert(s1.length() == s2.length());
	  char[] result = new char[s2.length()];
	  for (int i = 0; i < s2.length(); i++)
	  	result[i] = '0';
	  int c = 0;
	  for (int i = s1.length() - 1; i >= 0; i--) {
	    int t = c + dig(s1.charAt(i)) + dig(s2.charAt(i));
	    result[i] = cdig(t % 10);
	    c = t / 10;
	  }
	  assert(c == 0);
	  return new String(result);
	}

	private String stringSubtraction(String s1, String s2) {
//	  i, t, c : integer;
	  assert(s1.length() == s2.length());

	  char[] result = new char[s2.length()];
	  for (int i = 0; i < s2.length(); i++)
	  	result[i] = '0';

	  int c = 0;
	  for (int i = s1.length() - 1; i >= 0; i--) {
	    int t = c + (dig(s1.charAt(i)) - dig(s2.charAt(i)));
	    if (t < 0) {
	      t = t + 10;
	      if (i == 0) {
	        throw new Error("internal logic error");
	      }
	      s1 = replaceChar(s1, i-1, cdig(dig(s1.charAt(i-1))-1));
	    }
	    result[i] = cdig(t);
	  }
	  assert(c == 0);
	  return new String(result);
	}

	private String replaceChar(String s, int offset, char c) {
	  if (offset == 0){
	  	return String.valueOf(c)+s.substring(1);
	  }
	  return s.substring(0, offset)+c+s.substring(offset+1); 
  }


	public Decimal multiply(Decimal other) {
		if (other == null)
			return null;

		if (isZero() || other.isZero())
			return zero();

		int max = Math.max(decimal, other.decimal);
		String s1 = stringMultiply('0', max - decimal+1) + digits;
		String s2 = stringMultiply('0', max - other.decimal+1) + other.digits;
		if (s1.length() < s2.length())
			s1 = s1 + stringMultiply('0', s2.length() - s1.length());
		else if (s2.length() < s1.length())
			s2 = s2 + stringMultiply('0', s1.length() - s2.length());

		if (s2.compareTo(s1) > 0) {
			String s3 = s1;
			s1 = s2;
			s2 = s3;
		}
		String[] s = new String[s2.length()];

		int t = 0;
		for (int i = s2.length()-1; i >= 0; i--) {
			s[i] = stringMultiply('0', s2.length()-(i+1));
			int c = 0;
			for (int j = s1.length() - 1; j >= 0; j--) {
				t = c + (dig(s1.charAt(j)) * dig(s2.charAt(i)));
				s[i] = insert(String.valueOf(cdig(t % 10)), s[i], 0);
				c = t / 10;
			}
			while (c > 0) {
				s[i] = insert(String.valueOf(cdig(t % 10)), s[i], 0);
				c = t / 10;
			}
		}

		t = 0;
		for (String sv : s)
			t = Math.max(t, sv.length());
		for (int i = 0; i < s.length; i++) 
			s[i] = stringMultiply('0', t-s[i].length())+s[i];

		String res = "";
		int c = 0;
		for (int i = t - 1; i>= 0; i--) {
			for (int j = 0; j < s.length; j++) 
				c = c + dig(s[j].charAt(i));
			res = insert(String.valueOf(cdig(c %10)), res, 0);
			c = c / 10;
		}

		if (c > 0) {
			throw new Error("internal logic error");
			//        while..
			//        s[i-1] = s[i-1] + cdig(t mod 10);
			//        c = t div 10;
		}

		int dec = res.length() - ((s1.length() - (max+1))*2);

		while (!Utilities.noString(res) && !res.equals("0") && res.startsWith("0")) {
			res = res.substring(1);
			dec--;
		}

		int prec = 0;
		if (isWholeNumber() && other.isWholeNumber())
			// at least the specified precision, and possibly more
			prec = Math.max(Math.max(digits.length(), other.digits.length()), Math.min(precision, other.precision));
		else if (isWholeNumber())
			prec = other.precision;
		else if (other.isWholeNumber())
			prec = precision;
		else
			prec = Math.min(precision, other.precision);
		while (res.length() > prec && res.charAt(res.length()-1) == '0')
			res = delete(res, res.length()-1, 1);

		Decimal result = new Decimal();
		try {
	    result.setValueDecimal(res);
    } catch (Exception e) {
	    // won't happen
    }
		result.precision = prec;
		result.decimal = dec;
		result.negative = negative != other.negative;
		result.scientific = scientific || other.scientific;
		return result;
	}

	public Decimal divide(Decimal other) throws UcumException  {
		if (other == null)
			return null;

		if (isZero())
			return zero();

		if (other.isZero())
			throw new UcumException("Attempt to divide "+toString()+" by zero");

		String s = "0"+other.digits;
		int m = Math.max(digits.length(), other.digits.length()) + 40; // max loops we'll do
		String[] tens = new String[10];
		tens[0] = stringAddition(stringMultiply('0', s.length()), s);
		for (int i = 1; i < 10; i++)
			tens[i] = stringAddition(tens[i-1], s);
		String v = digits;
		String r = "";
		int l = 0;
		int d = (digits.length() - decimal + 1) - (other.digits.length() - other.decimal + 1);

		while (v.length() < tens[0].length()) {
			v = v + "0";
			d++;
		}

		String w;
		int vi;
		if (v.substring(0, other.digits.length()).compareTo(other.digits) < 0) {
			if (v.length() == tens[0].length()) {
				v = v + '0';
				d++;
			}
			w = v.substring(0, other.digits.length()+1);
			vi = w.length();
		} else {
			w = "0"+v.substring(0, other.digits.length());
			vi = w.length()-1;
		}

		boolean handled = false;
		boolean proc;

		while (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
			l++;
			handled = true;
			proc = false;
			for (int i = 8; i >= 0; i--) {
				if (tens[i].compareTo(w) <= 0) {
					proc = true;
					r = r + cdig(i+1);
					w = trimLeadingZeros(stringSubtraction(w, tens[i]));
					if (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
						if (vi < v.length()) {
							w = w + v.charAt(vi);
							vi++;
							handled = false;
						} else {
							w = w + '0';
							d++;
						}
						while (w.length() < tens[0].length()) 
							w = '0'+w;
					}
					break;
				}
			}
			if (!proc) {
				assert(w.charAt(0) == '0');
				w = delete(w, 0, 1);
				r = r + "0";
				if (!(handled && ((l > m) || ((vi >= v.length()) && ((Utilities.noString(w) || allZeros(w, 0))))))) {
					if (vi < v.length()) {
						w = w + v.charAt(vi);
						vi++;
						handled = false;
					} else {
						w = w + '0';
						d++;
					}
					while (w.length() < tens[0].length()) 
						w = '0'+w;
				}
			}
		}

		int prec;

		if (isWholeNumber() && other.isWholeNumber() && (l < m)) {
			for (int i = 0; i < d; i++) { 
				if (r.charAt(r.length()-1) == '0') { 
					r = delete(r, r.length()-1, 1);
					d--;
				}
			}
			prec = 100;
		} else {
			if (isWholeNumber() && other.isWholeNumber())
				prec = Math.max(digits.length(), other.digits.length());
			else if (isWholeNumber())
				prec = Math.max(other.precision, r.length() - d);
			else if (other.isWholeNumber())
				prec = Math.max(precision, r.length() - d);
			else
				prec = Math.max(Math.min(precision, other.precision), r.length() - d);
			while (r.length() > prec) {
				boolean up = (r.charAt(r.length()-1) > '5');
				r = delete(r, r.length()-1, 1);
				if (up) {
					char[] rs = r.toCharArray();
					int i = r.length()-1;
					while (up && i > 0) {
						up = rs[i] == '9';
						if (up)
							rs[i] = '0';
						else
							rs[i] = cdig(dig(rs[i])+1);
						i--;
					}
					if (up) {
						r = '1'+new String(rs);
						d++;
					} else
						r = new String(rs);
				}
				d--;
			}
		}

		Decimal result = new Decimal();
		result.setValueDecimal(r);
		result.decimal = r.length() - d;
		result.negative = negative != other.negative;
		result.precision = prec;
		result.scientific = scientific || other.scientific;
		return result;
	}


	private String trimLeadingZeros(String s) {
		if (s == null) 
			return null;

		int i = 0;
		while (i < s.length() && s.charAt(i) == '0') 
			i++;
		if (i == s.length()){
			return "0";
		}
		return s.substring(i);
	}

	public Decimal divInt(Decimal other) throws UcumException  {
	  if (other == null)
	  	return null;
	  Decimal t = divide(other);
	  return t.trunc();
	}

	public Decimal modulo(Decimal other) throws UcumException  {
	  if (other == null)  
      return null;
	  Decimal t = divInt(other);
	  Decimal t2 = t.multiply(other);
	  return subtract(t2);
	}

	public boolean equals(Decimal value, Decimal maxDifference) {
	  Decimal diff = this.subtract(value).absolute();
	  return diff.comparesTo(maxDifference) <= 0;
  }

	private Decimal absolute() {
	  Decimal d = copy();
	  d.negative = false;
	  return d;
  }


}
