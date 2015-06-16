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
package org.impressivecode.depress.mg.astmetrics;

import org.knime.core.node.NodeView;

/**
 * @author Mateusz Kutyba, Wroclaw University of Technology
 */
public class AstMetricsPluginNodeView extends NodeView<AstMetricsPluginNodeModel> {

    protected AstMetricsPluginNodeView(final AstMetricsPluginNodeModel nodeModel) {
        super(nodeModel);
    }

    @Override
    protected void modelChanged() {
        AstMetricsPluginNodeModel nodeModel = (AstMetricsPluginNodeModel) getNodeModel();
        assert nodeModel != null;
    }

    @Override
    protected void onClose() {
    }

    @Override
    protected void onOpen() {
    }
}
