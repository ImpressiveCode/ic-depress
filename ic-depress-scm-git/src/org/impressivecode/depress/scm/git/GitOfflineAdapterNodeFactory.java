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
package org.impressivecode.depress.scm.git;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitOfflineAdapterNodeFactory extends NodeFactory<GitOfflineAdapterNodeModel> {

    @Override
    public GitOfflineAdapterNodeModel createNodeModel() {
        return new GitOfflineAdapterNodeModel();
    }

    @Override
    public int getNrNodeViews() {
        return 0;
    }

    @Override
    public NodeView<GitOfflineAdapterNodeModel> createNodeView(final int viewIndex, final GitOfflineAdapterNodeModel nodeModel) {
        throw new IllegalStateException("View not supported");
    }

    @Override
    public boolean hasDialog() {
        return true;
    }

    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new GitOfflineAdapterNodeDialog();
    }
}