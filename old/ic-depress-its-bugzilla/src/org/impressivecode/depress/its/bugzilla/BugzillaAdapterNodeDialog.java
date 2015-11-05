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
package org.impressivecode.depress.its.bugzilla;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.impressivecode.depress.its.FileParser;
import org.impressivecode.depress.its.ITSOfflineNodeDialog;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public class BugzillaAdapterNodeDialog extends ITSOfflineNodeDialog {
    private final static String BUG = "bug";

    protected BugzillaAdapterNodeDialog() {
        super();
    }
    
    @Override
    protected void createMappingManager() {
        mappingManager.createFilterPriority(new RefreshCaller("bug_severity"));
        mappingManager.createFilterType(new RefreshCaller(BUG));
        mappingManager.createFilterResolution(new RefreshCaller("resolution"));
        mappingManager.createFilterStatus(new RefreshCaller("bug_status"));
    }

    private class RefreshCaller implements Callable<List<String>> {
        private final String property;
        
        RefreshCaller(final String property) {
            this.property = property;
        }
        
        @Override
        public List<String> call() throws Exception {
            if(property.equals(BUG)) {
                List<String> list = new ArrayList<String>();
                list.add(BUG);
                return list;
            }
            FileParser parser = new FileParser();
            File file = new File(((SettingsModelString) (chooser.getModel())).getStringValue());
            String expression = "/rss/channel/item/" + property + "[not(preceding::" + property + "/. = .)]";
            return parser.parseXPath(file, expression);
        }
    }

}
