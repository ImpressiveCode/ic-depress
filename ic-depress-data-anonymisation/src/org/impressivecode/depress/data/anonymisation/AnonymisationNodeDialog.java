package org.impressivecode.depress.data.anonymisation;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "Anonymisation" Node.
 * Encrypts and decrypts selected input data using symmetric algorithm (DES), using provided encryption key.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrzej Dudek
 */
public class AnonymisationNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Anonymisation node.
     */
    protected AnonymisationNodeDialog() {

    }
}

