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

import com.tekstosense.segmenter.model.Section;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface Pdf2Structure.
 *
 * @author TekstoSense
 */
public interface Pdf2Structure {

	/**
	 * Process files.
	 *
	 * @param files the files
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void processFiles(List<String> files) throws IOException;

	/**
	 * Process file.
	 *
	 * @param files the files
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void processFile(String files) throws IOException;

	/**
	 * Gets the structured pdf.
	 *
	 * @return the structured pdf
	 */
	Map<File, List<Section>> getStructuredPdf();

}
