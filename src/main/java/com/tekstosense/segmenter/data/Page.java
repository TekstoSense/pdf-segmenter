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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class Page {
	private static final ArrayList<Text> NOTHING = new ArrayList<Text>();
	
	private ArrayList<Text> texts;
	private HashMap<Float, ArrayList<Text>> yPosMap;
	private PDRectangle clipBox;
	private int number;
	
	public Page(PDRectangle newClipBox, int newNumber) {
		texts = new ArrayList<Text>();
		yPosMap = new HashMap<Float, ArrayList<Text>>();
		clipBox = newClipBox;
		number = newNumber;
	}
	
	public void addText(Text t) {
		texts.add(t);
		
		Float yPos = new Float(t.getBaseline());
		if (yPosMap.containsKey(yPos)) {
			ArrayList<Text> l = yPosMap.get(yPos);
			l.add(t);
		} else {
			ArrayList<Text> l = new ArrayList<Text>();
			l.add(t);
			yPosMap.put(yPos, l);
		}
	}
	
	public void removeText(Text t) {
		if (texts.contains(t)) {
			texts.remove(t);
			yPosMap.get(t.getBaseline()).remove(t);
		}
	}
	
	public PDRectangle getClipBox() {
		return clipBox;
	}
	
	public List<Text> getText() {
		return texts;
	}
	
	public int getNumber() {
	    return number;
	}
	
	public List<Text> getTextAtY(float y) {
		Float fObj = new Float(y);
		if (yPosMap.containsKey(fObj)) {
			return yPosMap.get(fObj);
		}
		return NOTHING;
	}
	
	/** 
	 * @return Answers a float for every y position that is incident with
	 * the start of a Text.
	 */
	public Set<Float> getYPosWithText() {
		return yPosMap.keySet();
	}


	@Override
	public boolean equals(Object o){
		if(o instanceof Page){
			if(((Page)o).number == this.number)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode(){
		return Integer.hashCode(this.number);
	}
}