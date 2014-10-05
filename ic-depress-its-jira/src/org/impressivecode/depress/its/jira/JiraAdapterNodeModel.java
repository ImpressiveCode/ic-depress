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
package org.impressivecode.depress.its.jira;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSOfflineNodeModel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

/**
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class JiraAdapterNodeModel extends ITSOfflineNodeModel {

    protected JiraAdapterNodeModel() {
        super();
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        LOGGER.info("Preparing to read jira entries.");
        String filePath = fileSettings.getStringValue();
        List<ITSDataType> entries = parseEntries(filePath);
        LOGGER.info("Transforming to jira entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("Jira table created.");
        return new BufferedDataTable[] { out };
    }

    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec)
            throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }

    private List<ITSDataType> parseEntries(final String filePath) throws ParserConfigurationException, SAXException,
            IOException, ParseException {
        return new JiraEntriesParser(mappingManager.getPriorityModel().getIncluded(), mappingManager.getTypeModel()
                .getIncluded(), mappingManager.getResolutionModel().getIncluded(), mappingManager.getStatusModel()
                .getIncluded()).parseEntries(filePath);
    }

    // FIXME: refactorize this function
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return JiraAdapterTableFactory.createTableSpec();
    }

}
