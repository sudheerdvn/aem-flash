package com.flash.aem.core.servlets;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;

/**
 * The Class RequestForDeletionServlet.
 * 
 * @author sudheerdvn
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Request for Deletion Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/flash/deleteassets" })
public class RequestForDeletionServlet extends SlingAllMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4675612505863065427L;

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	private WorkflowService workflowService;

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
			String[] assetPathsArray = assetPaths.split(",");
			ResourceResolver resourceResolver = request.getResourceResolver();
	        Session session = resourceResolver.adaptTo(Session.class);
	        log.debug("session is {}", session);
			for (String assetPath : assetPathsArray) {
				log.debug("assetPath is {}", assetPath);
				triggerWorkflow(session, assetPath);
			}
			// Do required action with the asset paths
		}
	}

	/**
	 * Trigger workflow.
	 *
	 * @param session the session
	 * @param assetPath the asset path
	 */
	private void triggerWorkflow(Session session, String assetPath) {
		log.debug("In Trigger workflow");
		WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
		WorkflowModel workflowModel;
		try {
			workflowModel = workflowSession.getModel("/var/workflow/models/delete-asset");
			if (null != workflowModel) {
				log.debug("workflowModel is {}", workflowModel);
				WorkflowData wfData = workflowSession.newWorkflowData("JCR_PATH", assetPath);
				workflowSession.startWorkflow(workflowModel, wfData);
				log.debug("Started workflow");
			}
		} catch (WorkflowException e) {
			log.error("WorkflowException occurred while starting the workflow", e);
		}
	}

}
