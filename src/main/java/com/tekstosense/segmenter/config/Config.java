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
package com.tekstosense.segmenter.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class Config.
 */
public class Config {

	/** The configuration. */
	private Configuration configuration;
	
	/** The Constant LOG. */
	private static final Logger LOG = LogManager.getLogger(Config.class);

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public static Configuration getConfiguration() {
		return ConfigLoader.INSTANCE.configuration;
	}

	/**
	 * The Class ConfigLoader.
	 */
	private static class ConfigLoader {
		
		/** The instance. */
		private static Config INSTANCE = new Config();
	}

	/**
	 * Instantiates a new config.
	 */
	private Config() {
		try {
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class);
			builder.configure(params.fileBased().setFileName("pdf2xml.properties").setLocationStrategy(new ClasspathLocationStrategy()));
			configuration = builder.getConfiguration();
		} catch (ConfigurationException e) {
			LOG.error(e, e);
		}
	}
}
