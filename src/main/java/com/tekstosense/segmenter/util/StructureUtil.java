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
package com.tekstosense.segmenter.util;

import java.util.ArrayList;
import java.util.List;

import com.tekstosense.segmenter.model.LineNode;
import com.tekstosense.segmenter.model.Node;
import com.tekstosense.segmenter.model.Section;
import com.tekstosense.segmenter.model.Structure;

public class StructureUtil {

	public static List<Structure> toStructure(List<Section> sections) {
		List<Structure> withOutAbstractStructures = new ArrayList<Structure>();
		List<Structure> withAbstractStructures = new ArrayList<Structure>();
		Structure headingStructure = new Structure();
		boolean foundAbstract = false;

		for (Section section : sections) {
			Structure structure = new Structure();
			if (section.getHeading() != null)
				structure.setHeading(section.getHeading().getText());
			List<String> lines = new ArrayList<String>();
			Node node = section.getLines().getFirst();
			lines.add(((LineNode) node.getData()).getText());
			while (node.getNext() != null) {
				lines.add(((LineNode) node.getNext().getData()).getText());
				node = node.getNext();
			}
			structure.setText(lines);
			withOutAbstractStructures.add(structure);

			if (structure.getHeading().toLowerCase().contains("abstract")) {
				foundAbstract = true;
				withAbstractStructures.add(headingStructure);
			}

			if (foundAbstract) {
				withAbstractStructures.add(structure);
			} else {
				headingStructure.getText().addAll(structure.getText());
			}
		}

		if (foundAbstract)
			return withAbstractStructures;
		return withOutAbstractStructures;
	}
}
