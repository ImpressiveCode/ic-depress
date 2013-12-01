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

import org.knime.core.node.NodeCreationContext;
import org.knime.core.node.NodeDialogPane;
import org.knime.ext.poi.node.read2.XLSReaderNodeFactory;
import org.knime.ext.poi.node.read2.XLSReaderNodeModel;

/**
 *
 * @author Łukasz Leśniczek, Wrocław, Poland
 * 
 */
public class HPQCReaderNodeFactory extends XLSReaderNodeFactory {

	/**
     * {@inheritDoc}
     */
    @Override
    protected NodeDialogPane createNodeDialogPane() {
        return new HPQCReaderNodeDialog();
    }
	
	/**
     * {@inheritDoc}
     */
    @Override
    public XLSReaderNodeModel createNodeModel() {
        return new HPQCReaderNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XLSReaderNodeModel createNodeModel(final NodeCreationContext context) {
        return new HPQCReaderNodeModel(context);
    }
}
