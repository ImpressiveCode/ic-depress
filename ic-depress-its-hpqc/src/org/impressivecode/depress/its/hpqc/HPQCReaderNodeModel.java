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
package org.impressivecode.depress.its.hpqc;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeCreationContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.ext.poi.node.read2.XLSReaderNodeModel;
import org.knime.ext.poi.node.read2.XLSUserSettings;
import org.xml.sax.SAXException;
import com.google.common.base.Preconditions;

/**
 *
 * @author Łukasz Leśniczek, Wrocław, Poland
 * @author Mariusz Mulka, Wrocław, Poland
 */
public class HPQCReaderNodeModel extends XLSReaderNodeModel {


    private static final NodeLogger LOGGER = NodeLogger.getLogger(HPQCReaderNodeModel.class);

	
	private XLSUserSettings m_settings = new XLSUserSettings();

    public HPQCReaderNodeModel() {
    	super();
    }

    HPQCReaderNodeModel(final NodeCreationContext context) {
        this();
        m_settings.setFileLocation(context.getUrl().toString());
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
		m_settings = XLSUserSettings.load(settings);
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	Preconditions.checkArgument(inSpecs.length == 0);
    	return new DataTableSpec[] { ITSAdapterTableFactory.createDataColumnSpec() };
        
    }
    
    private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec) throws CanceledExecutionException {
        ITSAdapterTransformer transformer = new ITSAdapterTransformer(ITSAdapterTableFactory.createDataColumnSpec());
        return transformer.transform(entries, exec);
    }
    private List<ITSDataType> parseEntries(final BufferedDataTable outData) throws ParserConfigurationException, SAXException,
    IOException, ParseException {
        return new HPQCEntriesParser().parseEntries(outData);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        BufferedDataTable in = super.execute(inData, exec)[0];
        DataTableSpec newSpec =in.getDataTableSpec();
        BufferedDataTable result = exec.createSpecReplacerTable(in, newSpec);
        
        LOGGER.info("Preparing to read hpqc entries."); 
        List<ITSDataType> entries = parseEntries(result);
        LOGGER.info("Transforming to hpqc entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("HPQC table created.");
        return new BufferedDataTable[] { out };
       
    }
}
