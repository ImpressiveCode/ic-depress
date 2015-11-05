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

package org.impressivecode.depress.mr.ckjm;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ChidamberKemererJavaMetrics" Node.
 * 
 *
 * @author 
 */
public class ChidamberKemererJavaMetricsNodeView extends NodeView<ChidamberKemererJavaMetricsNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ChidamberKemererJavaMetricsNodeModel})
     */
    protected ChidamberKemererJavaMetricsNodeView(final ChidamberKemererJavaMetricsNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        ChidamberKemererJavaMetricsNodeModel nodeModel = 
            (ChidamberKemererJavaMetricsNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

