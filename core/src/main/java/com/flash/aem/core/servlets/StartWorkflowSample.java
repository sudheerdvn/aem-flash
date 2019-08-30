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
package com.flash.aem.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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
 * The Class StartWorkflowSample.
 * 
 * @author sudheerdvn
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Start Workflow Sample Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "aem-flash/components/structure/page",
		"sling.servlet.extensions=" + "workflow" })
public class StartWorkflowSample extends SlingSafeMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5502559379309686714L;

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/** The workflow service. */
	@Reference
	private WorkflowService workflowService;

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
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		String assetPath = req.getParameter("assetPath");

		Map<String, Object> param = new HashMap<>();
		param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
		ResourceResolver resolver = null;

		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			Session session = resolver.adaptTo(Session.class);

			WorkflowSession wfSession = workflowService.getWorkflowSession(session);
			String workflowName = "/var/workflow/models/request_for_activation";

			WorkflowModel wfModel = wfSession.getModel(workflowName);

			final Map<String, Object> workflowMetadata = new HashMap<>();
			workflowMetadata.put("workflowTitle", "Something from servlet");

			WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", assetPath);
			wfSession.startWorkflow(wfModel, wfData, workflowMetadata);

			session.save();
			session.logout();

		} catch (LoginException e) {
			log.error("LoginException occurred while starting a workflow ", e);
		} catch (WorkflowException e) {
			log.error("WorkflowException occurred while starting a workflow ", e);
		} catch (RepositoryException e) {
			log.error("RepositoryException occurred while starting a workflow ", e);
		}
	}

}
