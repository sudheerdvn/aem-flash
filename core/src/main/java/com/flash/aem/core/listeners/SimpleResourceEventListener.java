package com.flash.aem.core.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = EventListener.class)

public class SimpleResourceEventListener implements EventListener {

	Logger log = LoggerFactory.getLogger(this.getClass());
	private Session adminSession;

	@Reference
	org.apache.sling.jcr.api.SlingRepository repository;

	@Activate
	public void activate(ComponentContext context) {
		log.info("activating SimpleResourceEventListener");
		try {
			adminSession = repository.loginService("datawrite", null);
			adminSession.getWorkspace().getObservationManager().addEventListener(this, // handler
					Event.PROPERTY_ADDED | Event.NODE_ADDED, // binary combination of event types
					"/apps/example", // path
					true, // is Deep?
					null, // uuids filter
					null, // nodetypes filter
					false);

		} catch (RepositoryException e) {
			log.error("unable to register session", e);
		}
	}

	@Deactivate
	public void deactivate() {
		if (adminSession != null) {
			adminSession.logout();
		}
	}

	public void onEvent(EventIterator eventIterator) {
		try {
			while (eventIterator.hasNext()) {
				log.info("something has been added : {}", eventIterator.nextEvent().getPath());
			}
		} catch (RepositoryException e) {
			log.error("Error while treating events", e);
		}
	}
}