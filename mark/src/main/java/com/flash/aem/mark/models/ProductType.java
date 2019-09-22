/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.flash.aem.mark.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flash.common.core.services.GranolaBar;

/**
 * The Class ProductType.
 * 
 * @author sudheerdvn
 */
@Model(adaptables = Resource.class)
public class ProductType {

	/** The granola bar. */
	@Inject
	@Required
	@OSGiService
	private GranolaBar granolaBar;

	private static final Logger log = LoggerFactory.getLogger(ProductType.class);
			
	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		log.debug("In init Method");
	}

	/**
	 * Gets the granola type.
	 *
	 * @return the granola type
	 */
	public String getGranolaType() {
		log.debug("gronalBar is {}", granolaBar);
		log.debug("gronalBar type is {}", granolaBar.getType());
		return granolaBar.getType();
	}
}
