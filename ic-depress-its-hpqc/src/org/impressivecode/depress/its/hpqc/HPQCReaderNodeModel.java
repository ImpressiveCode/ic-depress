/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 *
 * History
 *   Apr 8, 2009 (ohl): created
 */
package org.impressivecode.depress.its.hpqc;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeCreationContext;
import org.knime.ext.poi.node.read2.XLSReaderNodeModel;
import org.knime.ext.poi.node.read2.XLSUserSettings;

/**
 *
 * @author £ukasz Leœniczek, Wroc³aw, Poland
 * @author Mariusz Mulka, Wroc³aw, Poland
 */
public class HPQCReaderNodeModel extends XLSReaderNodeModel {

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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	DataTableSpec[] oldSpec = super.configure(inSpecs);
    	return new DataTableSpec[] {getNewSpec(oldSpec[0])};
        
    }
    
    private DataTableSpec getNewSpec(final DataTableSpec in)
            throws InvalidSettingsException {
            Pattern searchPattern = Pattern.compile("^[Cc]{1}[Qq]{1}[ ]{0,1}[Ii]{1}[Dd]{1}$");
            final String rawReplace = "ID";
            DataColumnSpec[] cols = new DataColumnSpec[in.getNumColumns()];
            boolean hasConflicts = false;
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
                while (!nameHash.add(newNameUnique)) {
                    hasConflicts = true;
                    newNameUnique = newName + " (#" + (unifier++) + ")";
                }
                creator.setName(newNameUnique);
                cols[i] = creator.createSpec();
            }
            if (cols.length == 0) {
                // don't bother if input is empty
            } else if (hasConflicts) {
                setWarningMessage("Pattern replace resulted in duplicate column "
                        + "names; resolved conflicts using \"(#index)\" suffix");
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
        return new BufferedDataTable[] {result};
    }
    

}
