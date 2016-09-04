/*******************************************************************************
 * Copyright (c) 2016, TekstoSense and/or its affiliates. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *******************************************************************************/
package com.tekstosense.segmenter;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tekstosense.segmenter.StructurePdf.PdfSections;
import com.tekstosense.segmenter.commandline.Params;
import com.tekstosense.segmenter.model.Structure;

public class SegmenterTest {

	@Test
	public void testSegmenter() throws IOException {
		String[] args = { "-input", "/home/rishi/Desktop/testDoc/To check/paper 2009-Semantic interpretation and knowledge extraction.pdf",
				"-format", "STDOUT" };
		
		String heading = "Semantic interpretation and knowledge extraction ";

		Params cliParams = Params.getParams(args);
		ObjectMapper mapper = new ObjectMapper();

		String path = cliParams.getInput();
		PdfSections pdf2Xml = new PdfSections(cliParams);
		pdf2Xml.processFile(path);
		List<Structure> structure = mapper.readValue(pdf2Xml.generateOutput().get(0), TypeFactory.defaultInstance().constructCollectionType(List.class, Structure.class));
		Assert.assertTrue(structure.get(0).getHeading().equals(heading));
		
	}
}
