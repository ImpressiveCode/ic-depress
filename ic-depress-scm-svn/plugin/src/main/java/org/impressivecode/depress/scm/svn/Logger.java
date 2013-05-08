package org.impressivecode.depress.scm.svn;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;

/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public final class Logger {

	private NodeLogger logger;
	private static final Logger log;

	private Logger() {
		try {
			logger = NodeLogger.getLogger(Logger.class);
			NodeLogger.setLevel(LEVEL.ALL);
		} catch (NoClassDefFoundError e) {
		}
	}

	static {
		log = new Logger();
	}

	public static Logger instance() {
		return log;
	}

	public void error(Object info, Throwable e) {
		if (logger != null) {
			logger.error(info, e);
		}
	}

	public void warn(Object info) {
		if (logger != null) {
			logger.warn(info);
		}
	}

}
