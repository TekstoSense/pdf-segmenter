/*******************************************************************************
 * Copyright (c) 2016, TekstoSense and/or its affiliates. All rights reserved.
 *
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
 *******************************************************************************/
package com.tekstosense.segmenter.data;

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;

public class Text implements Comparable<Text> {

	private float x, baseline, width, height, pointSize, 
	              descent, ascent, fontSize;
	private String run;
	private PDFont font;
	private PDColor strokeColor;
	private PDColor nonStrokeColor;
	private String tempRun = "";

	public static Text newFor(TextPosition tp, PDGraphicsState gs,String text) {
		Text t = new Text();
		t.x = tp.getXDirAdj();
		t.baseline = tp.getYDirAdj();
		t.font = tp.getFont();
		t.strokeColor = gs.getStrokingColor();
		t.nonStrokeColor = gs.getNonStrokingColor();
		t.run = tp.getUnicode();
		t.width = tp.getWidth();
		t.height = tp.getHeight();
		t.pointSize = tp.getFontSizeInPt();
		t.fontSize = tp.getYScale();
		t.tempRun = t.run;

		// Bump the width by the word spacing for each space in tp.
/*		for (int i=0; i<tp.getCharacter().length(); i++) {
		    Character c = tp.getCharacter().charAt(i);
		    if (c.equals(" ")) {
		      t.width -= tp.getWidthOfSpace();
		        t.width += tp.getWordSpacing();
		    }
		}
*/		
		return t;
	}
	
/*	private static float looseness() {
		return 1;
	}
	
	private static float looseness(TextPosition tp, PDFont font) {
		return tp.getWidth() / 2;
	}
*/	
	private static String getColorString(PDColor c) {
		String colorString = "";
	//ccheck suitable for getcomponents	
		float[] rgb = c.getComponents();
		if (rgb.length == 1) {
		    int grey = (int) rgb[0] * 255;
		    colorString = Integer.toHexString(grey);
		    if (colorString.length() == 1) {
		        colorString = "0" + colorString;
		    }
		    colorString = colorString + colorString + colorString;
		} else {
    		for (int colorIndex=0; colorIndex<3; colorIndex++) {
    		    int color = (int) rgb[colorIndex] * 255;
    		    String s = Integer.toHexString(color);
    		    if (s.length() == 1) {
    		        s = "0" + s;
    		    }
    		    colorString += s;
    		}
		}
		
		return "#" + colorString;
	}
	
	public String getForegroundColor() {
		return getColorString(nonStrokeColor);
	}
	
	public String getFontFamily() {
		
		//COSDictionary has getbasefont method check if doesn't work
		String fontName = font.getName();
		if (fontName.endsWith("MT")) {
		    fontName = fontName.substring(0, fontName.length() - 2);
		}
		String[] bits = fontName.split("\\+|-");
		if (bits.length > 1) {
			return bits[1];
		}
		return "";
	}
	
	public String getFontFace() {
		String fontName = font.getName();
		if (fontName.endsWith("MT")) {
            fontName = fontName.substring(0, fontName.length() - 2);
        }
		String[] bits = fontName.split("\\+|-");
		if (bits.length > 2) {
			return bits[2];
		}
		return "Normal";
	}
	
	public String getBaseFontName() {
		return font.getName();
	}
	
	public float getX() {
		return x;
	}
	
	public float getTop() {
	    if (ascent != 0) {
	        return baseline - ascent;
	    } else {
	        return baseline - getBoundingBoxAscent();
	    }
	}
	
	public float getBottom() {
	    if (descent != 0) {
	        return baseline - descent;
	    } else {
	        return baseline - getBoundingBoxDescent();
	    }
	}

	public float getBaseline() {
		return baseline;
	}
	
	public float getAscent() {
	    return getAscent(font, fontSize);
	}
	
	public float getDescent() {
	    return getDescent(font, fontSize);
	}
	
	public float getBoundingBoxDescent() {
	    return getBoundingBoxDescent(font, fontSize);
	}
	
	public float getBoundingBoxAscent() {
        return getBoundingBoxAscent(font, fontSize);
    }
	
	public static float getBoundingBoxDescent(PDFont font, float fontSize) {
	    try {
	        BoundingBox bBox = font.getBoundingBox();
	        float boxDescent = bBox.getLowerLeftY();
	        return (boxDescent / 1000) * fontSize;
	    } catch (IOException e) {
	        // fall through
	    }
	    return 0.0f;
	}
	
	public static float getBoundingBoxAscent(PDFont font, float fontSize) {
        try {
            BoundingBox bBox = font.getBoundingBox();
            float boxAscent = bBox.getUpperRightY();
            return (boxAscent / 1000) * fontSize;
        } catch (IOException e) {
            // fall through
        }
        return 0.0f;
    }
	
	private static float getAscent(PDFont font, float fontSize) {
	    try {
	        if (font instanceof PDSimpleFont) {
	            PDSimpleFont simpleFont = (PDSimpleFont) font;
	            return (simpleFont.getFontDescriptor().getAscent() 
	                        / 1000) * fontSize;
	        } else if (font instanceof PDType0Font) {
	        	PDType0Font cidFont = (PDType0Font) font;
	            return (cidFont.getFontDescriptor().getAscent() 
	                        / 1000) * fontSize;
	        }
	    } catch (Exception e) {
            // fall through
        }
        return 0.0f;
	}

	private static float getDescent(PDFont font, float fontSize) {
	    try {
	        if (font instanceof PDSimpleFont) {
	            PDSimpleFont simpleFont = (PDSimpleFont) font;
	            return (-Math.abs(simpleFont.getFontDescriptor().getDescent()) 
	                        / 1000) * fontSize;
	        } else if (font instanceof PDType0Font) {
	        	PDType0Font cidFont = (PDType0Font) font;
	            return (-Math.abs(cidFont.getFontDescriptor().getDescent()) 
	                        / 1000) * fontSize;
	        }
	    } catch (Exception e) {
            // fall through
        }
	    return 0.0f;
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return getBottom() - getTop();
	}

	public float getPointSize() {
		return pointSize;
	}

	public String getRun() {
		return run;
	}

/*	public Text addBefore(Text t) {
		run = t.run + run;
		width += x - t.x + t.width;
		height = Math.max(height, t.height);
		ascent = Math.max(ascent, t.getAscent());
        descent = Math.min(descent, t.getDescent());
		return this;
	}
*/

	public Text addAfter(Text t,String text) {

		run += t.run;
		tempRun += t.run;
		if(text.equalsIgnoreCase(tempRun)) {
			run += " ";
			tempRun = "";
		}

		width += t.x - (x + width) + t.width; 
		height = Math.max(height, t.height);
		ascent = Math.max(ascent, t.getAscent());
		descent = Math.min(descent, t.getDescent());
		return this;
	}
	
/*	public Text addBefore(TextPosition tp) {
		run = tp.getCharacter() + run;
		width += x - tp.getX() + tp.getWidth();
		height = Math.max(height, tp.getHeight());
		ascent = Math.max(ascent, getAscent(tp.getFont(), tp.getYScale()));
		descent = Math.min(descent, getDescent(tp.getFont(), tp.getYScale()));
		return this;
	}
	
	public Text addAfter(TextPosition tp) {
		run += tp.getCharacter();
		width += tp.getX() - (x + width) + tp.getWidth();
		height = Math.max(height, tp.getHeight());
		ascent = Math.max(ascent, getAscent(tp.getFont(), tp.getYScale()));
        descent = Math.min(descent, getDescent(tp.getFont(), tp.getYScale()));
		return this;
	}
*/	
/*	public boolean isIncidentToLeft(Text t) {
		final float runRightX = t.x + t.width;
		
		return baseline == t.baseline
				&& runRightX >= (x - looseness())
				&& runRightX <= x + looseness();
	}
	
	public boolean isIncidentToRight(Text t) {
		return baseline == t.baseline
				&& t.x >= (x + width - looseness())
				&& t.x <= (x + width + looseness());
	}
*/	
/*	public boolean isIncidentToLeft(TextPosition tp) {
		final float mostAcceptableLeft = x - looseness(tp, font);
		final float mostAcceptableRight = x;
		final float charRightX = tp.getX() + tp.getWidth();
		
		return baseline == tp.getY() 
				&& charRightX >= mostAcceptableLeft
				&& charRightX <= mostAcceptableRight;
	}
*/	
/*	public boolean isIncidentToRight(TextPosition tp) {
		final float mostAcceptableLeft = x + width;
		final float mostAcceptableRight = x + width + looseness(tp, font);
		
		return baseline == tp.getY()
				&& tp.getX() >= mostAcceptableLeft
				&& tp.getX() <= mostAcceptableRight;
	}
*/	
/*	public boolean hasMatchingStyle(TextPosition tp, PDGraphicsState gs) {
		return tp.getFont() == font
				&& gs.getStrokingColor() == strokeColor
				&& gs.getNonStrokingColor() == nonStrokeColor;
	}
*/	
	public boolean hasMatchingStyle(Text t) {
		return t.font == font
				&& t.strokeColor == strokeColor
				&& t.nonStrokeColor == nonStrokeColor;
	}
	
	@Override
	public int compareTo(Text other) {
		if (this.x < other.x) {
			return -1;
		} else if (this.x == other.x) {
			return 0;
		} else {
			return 1;
		}
	}
}