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
package org.impressivecode.depress.scm.common;

import org.impressivecode.depress.scm.common.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.common.SCMInputTransformer;
import org.junit.Test;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class SCMInputTransformerPluginTest {

    @Test(expected = InvalidSettingsException.class)
    public void shouldValidateSpec() throws InvalidSettingsException {
    	
        // given
        DataTableSpec spec = new DataTableSpec(new DataColumnSpecCreator(SCMAdapterTableFactory.RESOURCE_NAME, StringCell.TYPE).createSpec());
        DataTableSpec minSpec = new DataTableSpec(new DataColumnSpecCreator(SCMAdapterTableFactory.RESOURCE_NAME, IntCell.TYPE).createSpec());
        // when
        new SCMInputTransformer().setInputSpec(spec).setMinimalSpec(minSpec).validate();
    }

  
}
