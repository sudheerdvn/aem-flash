package com.flash.aem.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sudheerdvn
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Request for Deletion Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/flash/deleteassets" })
public class RequestForDeletionServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 4675612505863065427L;

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.
	 * api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String assetPaths = request.getParameter("assetPaths");
		if (null != assetPaths) {
			log.debug("assetPaths are {}", assetPaths);
			// Do required action with the asset paths
		}
	}

}
