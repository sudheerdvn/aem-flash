package com.flash.aem.core.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * The Class AssetValidationProcess.
 * 
 * @author sudheerdvn
 */
@Component(service = WorkflowProcess.class, property = { "process.label=Asset Validation Proces" })
public class AssetValidationProcess implements WorkflowProcess {

	/** The log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/** The resolver factory. */
	@Reference
	private ResourceResolverFactory resolverFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.adobe.granite.workflow.exec.WorkflowProcess#execute(com.adobe.granite.
	 * workflow.exec.WorkItem, com.adobe.granite.workflow.WorkflowSession,
	 * com.adobe.granite.workflow.metadata.MetaDataMap)
	 */
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap args) throws WorkflowException {
		log.info("In Execute");
		String payload = item.getWorkflowData().getPayload().toString();
		log.debug("payload is {}", payload);

		/* Adding a condition to restrict it for only dam assets */
		if (payload.startsWith("/content/dam")) {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
			ResourceResolver resolver;
			Session session = null;
			try {
				resolver = resolverFactory.getServiceResourceResolver(param);
				session = resolver.adaptTo(Session.class);
				if (null != session) {
					Node assetNode = session.getNode(payload);
					if (null != assetNode) {
						//TODO: Validate asset references before deleting
						log.debug("Deleting asset node {}", assetNode.getPath());
						assetNode.remove();
						session.save();
					}
				}
			} catch (LoginException e) {
				log.error("LoginException occurred while executing the workflow", e);
			} catch (RepositoryException e) {
				log.error("RepositoryException occurred while deleting the asset", e);
			} finally {
				if (null != session) {
					session.logout();
				}
			}
		}

	}
	

}