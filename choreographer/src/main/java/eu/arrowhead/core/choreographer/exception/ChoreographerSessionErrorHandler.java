/********************************************************************************
 * Copyright (c) 2020 AITIA
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   AITIA - implementation
 *   Arrowhead Consortia - conceptualization
 ********************************************************************************/

package eu.arrowhead.core.choreographer.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import eu.arrowhead.core.choreographer.service.ChoreographerService;

@Component
public class ChoreographerSessionErrorHandler implements ErrorHandler {
	
	//=================================================================================================
	// members
	
	@Autowired
	private ChoreographerService service;
	
    private final Logger logger = LogManager.getLogger(ChoreographerService.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	public void handleError(final Throwable t) {
		logger.debug("handleError started...");
		
		//TODO: log cause message too
		logger.warn("Exception occurs during executing a plan - " + t.getClass().getSimpleName() + ": " + getMessage(t));
		logger.debug(t);
		
		if (t instanceof ChoreographerSessionException) {
			final ChoreographerSessionException ex = (ChoreographerSessionException) t;
			service.abortSession(ex.getSessionId(), ex.getSessionStepId(), ex.getDetailedMessage());
		}
	}
	
	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	private String getMessage(final Throwable t) {
		logger.debug("getMessage started...");

		String result = t.getCause() != null ? t.getCause().getMessage() : "";
		result += " " + t.getMessage();
		
		return result.trim();
	}
}