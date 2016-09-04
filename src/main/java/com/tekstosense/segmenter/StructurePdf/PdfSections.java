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
package com.tekstosense.segmenter.StructurePdf;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.tekstosense.segmenter.TextExtractor;
import com.tekstosense.segmenter.Rule.SectionRules;
import com.tekstosense.segmenter.commandline.Params;
import com.tekstosense.segmenter.model.DoublyLinkedList;
import com.tekstosense.segmenter.model.LineNode;
import com.tekstosense.segmenter.model.Section;
import com.tekstosense.segmenter.model.Structure;
import com.tekstosense.segmenter.util.StructureUtil;

/**
 * @author TekstoSense
 */
public class PdfSections implements Pdf2Structure {

	private List<File> files;
	private Map<File, List<Section>> pdfSections;
	private Params params;

	public PdfSections(Params params) {
		this.files = new ArrayList<>();
		this.pdfSections = new HashMap<>();
		this.params = params;
	}

	public PdfSections() {
		this(new Params());
	}

	@Override
	public void processFiles(List<String> files) throws IOException {
		this.files = checkNotNull(files, "Files can not be Null").stream().map(s -> new File(s)).collect(Collectors.toList());
		processPdf();
	}

	@Override
	public void processFile(String file) throws IOException {
		processFiles(Arrays.asList(file));
	}

	private void processPdf() throws IOException {
		for (File file : files) {
			TextExtractor te = parsePdf(file);
			DoublyLinkedList<LineNode> sentenceList = te.toLinkedList();
			SectionRules sectionRules = new SectionRules(sentenceList);
			sectionRules.applyDefaultRule();
			pdfSections.put(file, sectionRules.getSections());
		}
	}

	private TextExtractor parsePdf(File f) throws IOException {
		PDDocument doc = PDDocument.load(f);

		if (doc.isEncrypted()) {
			// Some documents are encrypted with the empty password. Try
			// to decrypt with this password, or the one passed in on the
			// command line (if any), and fail if we can't.
			try {
				doc.setAllSecurityToBeRemoved(false);
				// doc.decrypt(password); // Defaults to the empty string.
			} catch (Exception e) {
				throw new IOException("Can't decrypt document: ", e);
			}
		}
		TextExtractor te = new TextExtractor();
		te.writeText(doc, new OutputStreamWriter(new ByteArrayOutputStream()));

		return te;
	}

	public List<String> generateOutput() throws IOException {
		List<String> finalJson = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (Entry<File, List<Section>> entry : pdfSections.entrySet()) {
			List<Structure> structures = StructureUtil.toStructure(entry.getValue());

			if (this.params.getOutputDir() != null) {
				File toFile = new File(this.params.getOutputDir(), entry.getKey().getName() + ".txt");
				Files.write(structures.toString(), toFile, Charsets.UTF_8);
			} else if (params.getFormat().equalsIgnoreCase("STDOUT")) {
				System.out.println(structures.toString());
			}
			finalJson.add(mapper.writeValueAsString(structures));
		}
		return finalJson;
	}

	@Override
	public Map<File, List<Section>> getStructuredPdf() {
		return pdfSections;
	}
}
