package com.flash.aem.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.json.JSONObject;
import org.json.JSONException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ModifyContentServlet.
 * 
 * @author sudheerdvn
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Modify Content Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=POST",
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/modifyContent",
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json" })
public class ModifyContentServlet extends SlingAllMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8741323225964269884L;

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/** The resolver factory. */
	@Reference
	private ResourceResolverFactory resolverFactory;

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
		doPost(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.
	 * api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> param = new HashMap<>();
		param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
		ResourceResolver resolver = null;
		Session session = null;
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			String message = "Date Update";
			if (null != resolver) {
				session = resolver.adaptTo(Session.class);
				if (null != session) {
					Node node = session.getNode("/content/aem-flash/en");
					if (null != node) {
						log.debug("node path is {}", node.getPath());
						if (node.hasNode("jcr:content")) {
							Node jcrContentNode = node.getNode("jcr:content");
							jcrContentNode.setProperty("customTitle", "setFromServlet");
							message = message + " for " + node.getPath() + " is successful";
							session.save();
						}
					}
				}
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", message);
			response.setContentType("application/json");
			response.getWriter().write(jsonObject.toString());

		} catch (JSONException e) {
			log.error("JSONException occurred while modifying the content", e);
		} catch (LoginException e) {
			log.error("LoginException occurred while modifying the content", e);
		} catch (RepositoryException e) {
			log.error("RepositoryException occurred while modifying the content", e);
		} finally {
			if (null != session) {
				session.logout();
			}
		}
	}

}
