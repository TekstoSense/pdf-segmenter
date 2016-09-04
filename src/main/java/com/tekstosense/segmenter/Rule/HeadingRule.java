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
package com.tekstosense.segmenter.Rule;

import static com.google.common.base.CharMatcher.*;
import static com.tekstosense.segmenter.config.Config.*;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.base.CharMatcher;
import com.tekstosense.segmenter.model.LineNode;
import com.tekstosense.segmenter.model.Node;
import com.tekstosense.segmenter.model.Section;

/**
 * The Class HeadingRule. It contains basic Header Rules
 *
 * @author TekstoSense
 */
public class HeadingRule {

	/** The Constant MAX_RANGE. */
	private static final String MAX_RANGE = "MAX_RANGE";

	/** The Constant SPECIAL_CHAR. */
	private static final CharMatcher SPECIAL_CHAR = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.inRange('0', '9'))
			.or(CharMatcher.is('.')).or(CharMatcher.is('-')).or(CharMatcher.is(')')).or(CharMatcher.is('(')).or(CharMatcher.WHITESPACE).negate()
			.precomputed();

	private static final CharMatcher ONLY_SPECIAL = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).precomputed();
	
	/** The Constant END_WITH. */
	private static final CharMatcher END_WITH = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.inRange('0', '9'))
			.or(CharMatcher.is(')')).or(CharMatcher.WHITESPACE).negate().precomputed();

	/** The Constant CLOSING_BRACKET. */
	private static final CharMatcher CLOSING_BRACKET = CharMatcher.is(')').precomputed();

	/** The Constant OPEN_BRACKET. */
	private static final CharMatcher OPEN_BRACKET = CharMatcher.is('(').precomputed();

	/**
	 * Checks for more numbers.
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean hasMoreNumbers(String text) {
		return inRange('0', '9').retainFrom(text).length() >= getConfiguration().getInt(MAX_RANGE);
	}

	/**
	 * Checks if is current OR after reference.
	 *
	 * @param sections
	 *            the sections
	 * @param section
	 *            the section
	 * @return true, if is current OR after reference
	 */
	public static boolean isCurrentORAfterReference(List<Section> sections, Section section) {
		if (section != null) {
			if (section.getHeading() != null && section.getHeading().getText().toLowerCase().contains("REFERENCES".toLowerCase())) {
				return true;
			}
			int index = sections.indexOf(section);
			if (index > 0) {
				return sections.get(index - 1).isReference();
			}
		}
		return false;
	}

	/**
	 * Checks for more special chars. Can be Improved Further
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean hasMoreSpecialChars(String text) {
		return SPECIAL_CHAR.retainFrom(text).length() > 0;
	}
	
	public static boolean hasOnlySpecialChar(String text){
		return ONLY_SPECIAL.retainFrom(text).length() <= 2;
	}

	/**
	 * Checks for only number.
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean hasOnlyNumber(String text) {
		return NumberUtils.isNumber(text.trim());
	}

	/**
	 * Ends with special character.
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean endsWithSpecialChar(String text) {
		String trimText = text.trim();
		return END_WITH.retainFrom(String.valueOf(trimText.charAt(trimText.length() - 1))).length() > 0;
	}

	/**
	 * Checks for bracket pair.
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean hasBracketPair(String text) {
		return (OPEN_BRACKET.retainFrom(text).length() == CLOSING_BRACKET.retainFrom(text).length());
	}

	/**
	 * Checks for single closing bracket.
	 *
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
	public static boolean hasSingleClosingBracket(String text) {
		if (CLOSING_BRACKET.retainFrom(text).length() == 0 || CLOSING_BRACKET.retainFrom(text).length() == 1)
			return true;
		return false;
	}

	/**
	 * Checks if is single Character.
	 *
	 * @param text
	 *            the text
	 * @return true, if is single char
	 */
	public static boolean isSingleChar(String text) {
		return text.length() == 1;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		System.out.println(hasOnlySpecialChar("1 X 2 X 3 XX X"));
	}

	/**
	 * Checks for special characters.
	 *
	 * @param lineNodeNode
	 *            the line node node
	 * @return true, if successful
	 */
	public static boolean hasSpecialCharacters(Node<LineNode> lineNodeNode) {
		if (lineNodeNode.getData().getFamily().equalsIgnoreCase("symbol") || lineNodeNode.getData().getFamily().equalsIgnoreCase("CMMI9")
				|| lineNodeNode.getData().getFamily().equalsIgnoreCase("CMMI6") || NumberUtils.isNumber(lineNodeNode.getData().getText())) {
			return true;
		}
		return false;
	}
}
