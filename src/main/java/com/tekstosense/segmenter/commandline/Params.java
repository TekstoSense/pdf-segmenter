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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Params {

	@Parameter(names = "-input", description = "Input File Path")
	private String input;

	@Parameter(names = "-inputDir", description = "Input File Path")
	private String inputDir;

	@Parameter(names = "-outputDir", description = "Output File Path")
	private String outputDir;

	@Parameter(names = "-format", description = "Output Format")
	private String format = "JSON";

	public static Params getParams(String[] args) {
		Params cliParams = new Params();
		new JCommander(cliParams, args);
		return cliParams;
	}

	public String getInput() {
		return input;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public String getFormat() {
		return format;
	}

}
