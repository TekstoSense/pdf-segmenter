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
package com.tekstosense.segmenter.model;

/**
 * @author TekstoSense
 */
public class LineNode {

	private int page;
	private float top;
	private float left;
	private float width;
	private float height;
	private float size;
	private String family;
	private String face;
	private String colour;
	private String text;
	private float descent;
	private float ascent;

	public LineNode() {
	}

	public LineNode(int page, float top, float left, float width, float height, float size, float descent, float ascent, String family, String face,
			String colour, String text) {
		this.page = page;
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.size = size;
		this.family = family;
		this.face = face;
		this.colour = colour;
		this.text = text;
		this.descent = descent;
		this.ascent = ascent;
	}

	public float getDescent() {
		return descent;
	}

	public void setDescent(float descent) {
		this.descent = descent;
	}

	public float getAscent() {
		return ascent;
	}

	public void setAscent(float ascent) {
		this.ascent = ascent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	@Override
	public String toString() {
		return this.text + "\n";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LineNode) {
			return ((LineNode) o).text.equals(this.text);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.text.hashCode();
	}
}
