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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
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

    /**
     *
     */
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
    	return new DataTableSpec[] { ITSAdapterTableFactory
				.createDataColumnSpec() };
        
    }
    
    private DataTableSpec getNewSpec(final DataTableSpec in)
            throws InvalidSettingsException {
            Pattern searchPattern = Pattern.compile("^[Cc]{1}[Qq]{1}[ ]{0,1}[Ii]{1}[Dd]{1}$");
            final String rawReplace = "ID";
            DataColumnSpec[] cols = new DataColumnSpec[in.getNumColumns()];
            Set<String> nameHash = new HashSet<String>();
           
            for (int i = 0; i < cols.length; i++) {
                String replace = getReplaceStringWithIndex(rawReplace, i);
                final DataColumnSpec oldCol = in.getColumnSpec(i);
                final String oldName = oldCol.getName();
                DataColumnSpecCreator creator = new DataColumnSpecCreator(oldCol);
                Matcher m = searchPattern.matcher(oldName);
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    try {
                        m.appendReplacement(sb, replace);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new InvalidSettingsException(
                                "Error in replacement string: " + ex.getMessage(),
                                ex);
                    }
                }
                m.appendTail(sb);
                final String newName = sb.toString();

                if (newName.length() == 0) {
                    throw new InvalidSettingsException("Replacement in column '"
                            + oldName + "' leads to an empty column name.");
                }
                String newNameUnique = newName;
                int unifier = 1;
                while (!nameHash.add(newNameUnique)) 
                    newNameUnique = newName + " (#" + (unifier++) + ")";
                
                creator.setName(newNameUnique);
                cols[i] = creator.createSpec();
            }
            return new DataTableSpec(in.getName(), cols);
        }
    
    private static String getReplaceStringWithIndex(
            final String replace, final int index) {
        if (!replace.contains("$i")) {
            return replace;
        }
        /* replace every $i by index .. unless it is escaped */
        // check starts with $i
        String result = replace.replaceAll("^\\$i", Integer.toString(index));
        // any subsequent occurrence, which is not escaped
        return result.replaceAll("([^\\\\])\\$i", "$1" + index);
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
        DataTableSpec oldSpec = in.getDataTableSpec();
        DataTableSpec newSpec = getNewSpec(oldSpec);
        BufferedDataTable result = exec.createSpecReplacerTable(in, newSpec);
        
        LOGGER.info("Preparing to read hpqc entries."); 
        List<ITSDataType> entries = parseEntries(result);
        LOGGER.info("Transforming to hpqc entries.");
        BufferedDataTable out = transform(entries, exec);
        LOGGER.info("HPQC table created.");
        return new BufferedDataTable[] { out };
       
    }
}
