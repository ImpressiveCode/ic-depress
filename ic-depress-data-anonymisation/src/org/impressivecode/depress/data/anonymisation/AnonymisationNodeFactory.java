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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */
package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Anonymisation" Node. Encrypts and decrypts selected 
 * columns from input data set using DES cryptographic algorithm.
 * 
 * @author Andrzej Dudek
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * 
 */
public class AnonymisationNodeFactory extends NodeFactory<AnonymisationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnonymisationNodeModel createNodeModel() {
        return new AnonymisationNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<AnonymisationNodeModel> createNodeView(final int viewIndex, final AnonymisationNodeModel nodeModel) {
        return new AnonymisationNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new AnonymisationNodeDialog();
    }

}
