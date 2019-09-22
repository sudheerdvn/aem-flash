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

/**
 * The listener interface for receiving simpleEvent events. The class that is
 * interested in processing a simpleEvent event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addSimpleEventListener<code> method. When the simpleEvent
 * event occurs, that object's appropriate method is invoked.
 *
 * @see SimpleEventEvent
 * 
 * @author sudheerdvn
 */
@Component(immediate = true, service = EventListener.class)

public class SimpleEventListener implements EventListener {

	/** The log. */
	Logger log = LoggerFactory.getLogger(this.getClass());

	/** The admin session. */
	private Session session;

	/** The repository. */
	@Reference
	org.apache.sling.jcr.api.SlingRepository repository;

	/**
	 * Activate.
	 *
	 * @param context
	 *            the context
	 * @throws Exception
	 *             the exception
	 */
	@Activate
	public void activate(ComponentContext context) {
		log.info("In Activate method");
		try {
			session = repository.loginService("datawrite", null);
			session.getWorkspace().getObservationManager().addEventListener(this, Event.NODE_ADDED | Event.NODE_REMOVED,
					"/content", true, null, null, false);
		} catch (RepositoryException e) {
			log.error("RepositoryException occurred in activate method", e);
		}
	}

	/**
	 * Deactivate.
	 */
	@Deactivate
	public void deactivate() {
		if (session != null) {
			session.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jcr.observation.EventListener#onEvent(javax.jcr.observation.
	 * EventIterator)
	 */
	public void onEvent(EventIterator eventIterator) {
		try {
			while (eventIterator.hasNext()) {
				Event event = eventIterator.nextEvent();
				log.info("{} has been {}", event.getPath(), event.getType());
			}
		} catch (RepositoryException e) {
			log.error("Error while treating events", e);
		}
	}
}