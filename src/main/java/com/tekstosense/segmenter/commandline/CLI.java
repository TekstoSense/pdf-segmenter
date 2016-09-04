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
package com.tekstosense.segmenter.commandline;

import java.io.IOException;

import com.tekstosense.segmenter.StructurePdf.PdfSections;

public class CLI {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Path is required...");
			System.exit(1);
		}

		Params cliParams = Params.getParams(args);

		String path = cliParams.getInput();
		PdfSections pdf2Xml = new PdfSections(cliParams);
		pdf2Xml.processFile(path);
		System.out.println(pdf2Xml.generateOutput());
	}
}
