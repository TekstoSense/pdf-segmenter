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

import java.util.ArrayList;
import java.util.List;

public class Structure {

	String heading = "";
	List<String> text;

	public Structure() {
		this.heading = "";
		this.text = new ArrayList<String>();
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("\n################## HEADING ################## \n");
		builder.append(this.heading);
		if (this.text != null) {
			builder.append("\n################## TEXT ################## \n");
			for (String line : text)
				builder.append(line + "\n");
		}
		return builder.toString();

	}

}
