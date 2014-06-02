package org.impressivecode.depress.metric.astcompare;

import static org.impressivecode.depress.metric.astcompare.utils.Utils.DATE_FROM;
import static org.impressivecode.depress.metric.astcompare.utils.Utils.DATE_TO;
import static org.impressivecode.depress.metric.astcompare.utils.Utils.PROJECTS_NAMES;
import static org.impressivecode.depress.metric.astcompare.utils.Utils.WEEKS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.impressivecode.depress.metric.astcompare.utils.Utils;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "AstComparePlugin" Node. Plugin is checking
 * project repository and generating metrics by ast compare.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Piotr Mitka
 */
public class AstComparePluginNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring AstComparePlugin node dialog. This is just a
     * suggestion to demonstrate possible default dialog components.
     */
    protected AstComparePluginNodeDialog() {
        super();

        List<String> projects = new ArrayList<String>();
        IWorkspace root = ResourcesPlugin.getWorkspace();

        for (IProject project : root.getRoot().getProjects()) {
            projects.add(project.getName());
        }

        createNewGroup("Select project");
        addDialogComponent(new DialogComponentStringSelection(new SettingsModelString(PROJECTS_NAMES, ""), "Project:",
                projects));
        createNewGroup("Set revision's date range (DD-MM-YYYY)");
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(new SettingsModelString(DATE_FROM, Utils.getCurrentDayPlus(
                Calendar.MONTH, -2)), "From:"));
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(new SettingsModelString(DATE_TO, Utils.getCurrentDate()), "To:"));
        createNewGroup("Choose sample's timeframe");
        addDialogComponent(new DialogComponentStringSelection(new SettingsModelString(WEEKS, "All"), "Weeks:",
                new String[] { "2", "4", "8", "All" }));
    }
}
