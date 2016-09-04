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
 * The Class Section.
 *
 * @author TekstoSense
 */
public class Section {
	
	/** The heading. */
	LineNode heading;
	
	/** The is reference. */
	boolean isReference;
	
	/** The lines. */
	DoublyLinkedList<LineNode> lines;

	/**
	 * Gets the heading.
	 *
	 * @return the heading
	 */
	public LineNode getHeading() {
		return heading;
	}

	/**
	 * Sets the heading.
	 *
	 * @param heading the new heading
	 */
	public void setHeading(LineNode heading) {
		this.heading = heading;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public DoublyLinkedList<LineNode> getLines() {
		return lines;
	}

	/**
	 * Sets the lines.
	 *
	 * @param lines the new lines
	 */
	public void setLines(DoublyLinkedList<LineNode> lines) {
		this.lines = lines;
	}

	/**
	 * Adds the line.
	 *
	 * @param lineNode the line node
	 */
	public void addLine(LineNode lineNode) {
		if (lines == null)
			lines = new DoublyLinkedList<>();
		lines.addToLast(lineNode);
	}

	/**
	 * Checks if is reference.
	 *
	 * @return true, if is reference
	 */
	public boolean isReference() {
		return isReference;
	}

	/**
	 * Sets the reference.
	 *
	 * @param isReference the new reference
	 */
	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("\n################## HEADING ################## \n");
		builder.append(this.heading);
		if (this.lines != null) {
			builder.append("################## TEXT ################## \n");
			builder.append(this.lines.toString());
		}
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Section) {
			if (((Section) o).getHeading() != null)
				return ((Section) o).getHeading().getText().equals(this.getHeading().getText());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHeading().getText().hashCode();
	}
}
