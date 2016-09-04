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

import com.google.common.base.Preconditions;
import com.tekstosense.segmenter.model.DoublyLinkedList;
import com.tekstosense.segmenter.model.LineNode;
import com.tekstosense.segmenter.model.Node;
import com.tekstosense.segmenter.model.Section;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tekstosense.segmenter.Rule.HeadingRule.*;
import static com.tekstosense.segmenter.model.LinkedNGramUtil.buildNGram;

/**
 * @author TekstoSense
 */
public class SectionRules {

	private static final float LINE_SPACING = 5.0F;
	private static final float DESCENT_CONSTANT = 1F;
	private static final String REFERENCE = "Reference";

	private DoublyLinkedList<LineNode> lineNodeDoublyLinkedList;
	private List<Section> sections;
	private Section currentSection;

	public SectionRules(DoublyLinkedList<LineNode> lineNodeDoublyLinkedList) {
		this.lineNodeDoublyLinkedList = lineNodeDoublyLinkedList;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void applyDefaultRule() {
		Preconditions.checkNotNull(this.lineNodeDoublyLinkedList, "LinkedList can not be null");
		// mergerLines(this.lineNodeDoublyLinkedList);

		iterateLinkedList(nGramMerge(mergerLines(this.lineNodeDoublyLinkedList)));
	}

	private DoublyLinkedList<LineNode> nGramMerge(DoublyLinkedList<LineNode> lineNodeDoublyLinkedList) {
		Node<LineNode> startNode = lineNodeDoublyLinkedList.getFirst();
		DoublyLinkedList<LineNode> finalLinkedList = new DoublyLinkedList<>();
		nGramM(startNode, finalLinkedList, 0);
		return finalLinkedList;
	}

	private void nGramM(Node<LineNode> startNode, DoublyLinkedList<LineNode> finalLinkedList, int mergeItr) {
		Node<LineNode>[] nGrams = buildNGram(startNode, 3);
		if (startNode != null) {
			if (isInCorrectOrder(nGrams)) {
				if (mergeItr > 0) {
					Node<LineNode> newNode = mergeNGram(mergeItr, startNode);
					finalLinkedList.addToLast(newNode.getData());
				} else {
					finalLinkedList.addToLast(startNode.getData());
				}
				nGramM(startNode.getNext(), finalLinkedList, 0);
			} else {
				nGramM(startNode.getNext(), finalLinkedList, ++mergeItr);
			}
		}
	}

	private Node<LineNode> mergeNGram(int mergeItr, Node<LineNode> currentNode) {

		String[] strings = new String[mergeItr + 1];
		int count = strings.length;
		StringBuilder stringBuilder = new StringBuilder();
		if (currentNode != null) {

			while (mergeItr > 0) {
				strings[count - 1] = currentNode.getData().getText();
				currentNode = currentNode.getPrev();
				count--;
				mergeItr--;
			}

			for (int i = 1; i < strings.length; i++) {
				stringBuilder.append(" ").append(strings[i]);
			}
			currentNode.getData().setText(currentNode.getData() + " " + stringBuilder.toString());
		}
		return currentNode;
	}

	private boolean isInCorrectOrder(Node<LineNode>[] nGrams) {
		for (int i = 1; i < nGrams.length; i++) {
			if (nGrams[i - 1] != null && nGrams[i] != null) {
				if ((nGrams[i - 1].getData().getPage() == nGrams[i].getData().getPage() && (nGrams[i - 1].getData().getTop() > nGrams[i].getData()
						.getTop())))
					return false;
			}
		}
		return true;
	}

	private DoublyLinkedList<LineNode> mergerLines(DoublyLinkedList<LineNode> lineNodeDoublyLinkedList) {
		Node<LineNode> lineNodeNode = lineNodeDoublyLinkedList.getFirst();
		DoublyLinkedList<LineNode> finalLinkedList = new DoublyLinkedList<>();
		merge(lineNodeNode, finalLinkedList, null);
		return finalLinkedList;
	}

	private void merge(Node<LineNode> lineNodeNode, DoublyLinkedList<LineNode> finalLinkedList, Node<LineNode> currentNode) {
		if (lineNodeNode != null) {
			if (isPartOfPreviousLine(lineNodeNode)) {
				if (currentNode == null) {
					currentNode = lineNodeNode;
					merge(lineNodeNode.getNext(), finalLinkedList, currentNode);
				} else {
					currentNode.getData().setText(currentNode.getData().getText() + " " + ((LineNode) lineNodeNode.getData()).getText());
					merge(lineNodeNode.getNext(), finalLinkedList, currentNode);
				}
			} else {
				finalLinkedList.addToLast(lineNodeNode.getData());
				merge(lineNodeNode.getNext(), finalLinkedList, finalLinkedList.getLast());
			}
		}
	}

	private void iterateLinkedList(DoublyLinkedList<LineNode> lineNodeDoublyLinkedList) {
		Node<LineNode> lineNodeNode = lineNodeDoublyLinkedList.getFirst();
		parseNode(lineNodeNode);
	}

	private void parseNode(Node<LineNode> lineNodeNode) {
		if (isHeader(lineNodeNode)) {
			if (isReallyAHeader(lineNodeNode))
				createSection(lineNodeNode);
			else
				addToCurrentSection(lineNodeNode);
		} else {
			addToCurrentSection(lineNodeNode);
		}
		if (lineNodeNode.getNext() != null)
			parseNode(lineNodeNode.getNext());

	}

	private boolean isReallyAHeader(Node<LineNode> lineNodeNode) {
		if (isSingleChar(lineNodeNode.getData().getText()) || hasSpecialCharacters(lineNodeNode) || hasMoreNumbers(lineNodeNode.getData().getText())
				|| hasMoreSpecialChars(lineNodeNode.getData().getText()) || hasOnlyNumber(lineNodeNode.getData().getText())
				|| endsWithSpecialChar(lineNodeNode.getData().getText()) || !hasBracketPair(lineNodeNode.getData().getText())
				|| !hasSingleClosingBracket(lineNodeNode.getData().getText()) || hasOnlySpecialChar(lineNodeNode.getData().getText())) {
			return false;
		}

		if (isPartOfPreviousLine(lineNodeNode) && currentSection != null) {
			if (currentSection.getLines() != null && currentSection.getLines().getSize() > 0) {
				addToCurrentSection(lineNodeNode);
				return false;
			} else if (!currentSection.getHeading().getText().isEmpty()) {
				createSection(lineNodeNode);
				return false;
			}
		}
		return true;
	}

	private boolean hasSpecialCharacters(Node<LineNode> lineNodeNode) {

		if (lineNodeNode.getData().getFamily().equalsIgnoreCase("symbol") || lineNodeNode.getData().getFamily().equalsIgnoreCase("CMMI9")
				|| lineNodeNode.getData().getFamily().equalsIgnoreCase("CMMI6") || NumberUtils.isNumber(lineNodeNode.getData().getText())) {
			return true;
		}
		return false;
	}

	private void addToCurrentSection(Node<LineNode> lineNodeNode) {
		if (sections == null) {
			sections = new ArrayList<>();
		}
		if (currentSection == null) {
			currentSection = new Section();
			sections.add(currentSection);
		}
		if (isPartOfPreviousLine(lineNodeNode)) {
			if (currentSection.getLines() != null && currentSection.getLines().getLast() != null) {
				String updatedText = ((LineNode) currentSection.getLines().getLast().getData()).getText() + lineNodeNode.getData().getText();
				((LineNode) currentSection.getLines().getLast().getData()).setText(updatedText);
				((LineNode) currentSection.getLines().getLast().getData()).setTop(lineNodeNode.getData().getTop());
			} else {
				currentSection.addLine(lineNodeNode.getData());
			}
		} else {
			currentSection.addLine(lineNodeNode.getData());
		}
	}

	private boolean isPartOfPreviousLine(Node<LineNode> lineNodeNode) {
		if (lineNodeNode.getPrev() != null)
			return (Math.abs((lineNodeNode.getData().getTop() - ((LineNode) lineNodeNode.getPrev().getData()).getTop())) < LINE_SPACING);
		return false;
	}

	private boolean isHeader(Node<LineNode> lineNodeNode) {
		float currentSize = lineNodeNode.getData().getSize();
		float currentDesent = lineNodeNode.getData().getDescent();
		float prevSize = 0;
		float nextSize = 0;
		float nextDesent = 0;
		if (lineNodeNode.getPrev() != null)
			prevSize = ((LineNode) lineNodeNode.getPrev().getData()).getSize();
		if (lineNodeNode.getNext() != null) {
			nextSize = ((LineNode) lineNodeNode.getNext().getData()).getSize();
			nextDesent = ((LineNode) lineNodeNode.getNext().getData()).getDescent();
		}

		if (isFirstPage(lineNodeNode)) {
			return (prevSize <= currentSize && currentSize > nextSize && hasCorrectDescent(lineNodeNode));
		} else {
			String currentFamily = lineNodeNode.getData().getFamily();
			String prevFamily = "";
			String nextFamily = "";
			if (lineNodeNode.getPrev() != null)
				prevFamily = ((LineNode) lineNodeNode.getPrev().getData()).getFamily();
			if (lineNodeNode.getNext() != null)
				nextFamily = ((LineNode) lineNodeNode.getNext().getData()).getFamily();
			return ((prevSize < currentSize && currentSize > nextSize && hasCorrectDescent(lineNodeNode)) || ((!currentFamily
					.equalsIgnoreCase(prevFamily) && !currentFamily.equalsIgnoreCase(nextFamily))
					&& !NumberUtils.isNumber(lineNodeNode.getData().getText()) && hasCorrectDescent(lineNodeNode)));
		}
	}

	private boolean hasCorrectDescent(Node<LineNode> lineNodeNode) {
		float currentDesent = lineNodeNode.getData().getDescent();
		float nextDesent = 0;
		if (lineNodeNode.getNext() != null)
			nextDesent = ((LineNode) lineNodeNode.getNext().getData()).getDescent();

		float diff = Math.abs(currentDesent - nextDesent);
		return diff < DESCENT_CONSTANT;
	}

	private boolean isFirstPage(Node<LineNode> lineNodeNode) {
		return lineNodeNode.getData().getPage() == 1;
	}

	private void createSection(Node<LineNode> lineNodeNode) {
		if (sections == null) {
			sections = new ArrayList<>();
		}

		if (!isPartOfPreviousLine(lineNodeNode) && !isCurrentORAfterReference(sections, currentSection)) {
			Section section = new Section();
			section.setHeading(lineNodeNode.getData());
			if (lineNodeNode.getData().getText().toLowerCase().indexOf(REFERENCE.toLowerCase()) > -1) {
				section.setReference(true);
			}
			currentSection = section;
			sections.add(currentSection);
		} else {
			String updatedText = ((LineNode) currentSection.getHeading()).getText() + lineNodeNode.getData().getText();
			if (!isCurrentORAfterReference(sections, currentSection))
				currentSection.getHeading().setText(updatedText);
			currentSection.getHeading().setTop(lineNodeNode.getData().getTop());
		}

		/*
		 * if (isPartOfPreviousLine(lineNodeNode) || isAfterReference(sections,
		 * currentSection)) { String updatedText = ((LineNode)
		 * currentSection.getHeading()).getText() +
		 * lineNodeNode.getData().getText();
		 * currentSection.getHeading().setText(updatedText);
		 * currentSection.getHeading().setTop(lineNodeNode.getData().getTop());
		 * } else { Section section = new Section();
		 * section.setHeading(lineNodeNode.getData()); if
		 * (lineNodeNode.getData()
		 * .getText().toLowerCase().indexOf(REFERENCE.toLowerCase()) > -1) {
		 * section.setReference(true); } currentSection = section;
		 * sections.add(currentSection); }
		 */
	}
}
