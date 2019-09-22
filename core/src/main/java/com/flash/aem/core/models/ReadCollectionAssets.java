package com.flash.aem.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.resource.collection.ResourceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ReadCollectionAssets.
 * 
 * @author sudheerdvn
 */
@Model(adaptables = Resource.class)
public class ReadCollectionAssets {

	/** The resource resolver. */
	@SlingObject
	private ResourceResolver resourceResolver;

	/** The image paths. */
	List<String> imagePaths = new ArrayList<>();

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		Resource resource = resourceResolver
				.getResource("/content/dam/collections/k/kXjI0j44sW4pq2mWc9fE/public collections");
		if (null != resource) {
			log.debug("resource path is {}", resource.getPath());
			ResourceCollection resourceCollection = resource.adaptTo(ResourceCollection.class);
			if (null != resourceCollection) {
				Iterator<Resource> resourceIterator = resourceCollection.getResources();
				while (resourceIterator.hasNext()) {
					Resource damResource = resourceIterator.next();
					log.debug("damResource path is {}", damResource.getPath());
					imagePaths.add(damResource.getPath());
				}
			}
		}
	}

	/**
	 * Gets the image paths.
	 *
	 * @return the image paths
	 */
	public List<String> getImagePaths() {
		return imagePaths;
	}

}
