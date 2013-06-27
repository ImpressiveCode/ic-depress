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
package org.impressivecode.depress.data.source;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class DataSourceAdapterClassDataEntriesParserTest {

    @Test
    public void T001_AddToMethodTable_MockClass_Method_IsPublic_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsPublic());
    }

    @Test
    public void T002_AddToMethodTable_MockClass_Method_IsPublic_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPrivate());
    }

    @Test
    public void T003_AddToMethodTable_MockClass_Method_IsStatic_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsStatic());
    }

    @Test
    public void T004_AddToMethodTable_MockClass_Method_IsStatic_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsInterface());
    }

    @Test
    public void T005_AddToMethodTable_MockClass_Method_IsPublic_and_IsStatic_Return_true_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsPublic());
        assertTrue(output.get(0).getIsStatic());
    }

    @Test
    public void T006_AddToMethodTable_MockClass_Method_IsPublic_and_IsStatic_Return_true_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsPublic());
        assertFalse(output.get(0).getIsPrivate());
    }

    @Test
    public void T007_AddToMethodTable_MockClass_Method_IsPublic_and_IsStatic_Return_false_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPrivate());
        assertTrue(output.get(0).getIsStatic());
    }

    @Test
    public void T008_AddToMethodTable_MockClass_Method_IsPublic_and_IsStatic_Return_false_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPrivate());
        assertFalse(output.get(0).getIsInterface());
    }

    @Test
    public void T009_AddToMethodTable_MockClass_Method_IsProtected_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsProtected());
    }

    @Test
    public void T010_AddToMethodTable_MockClass_Method_IsProtected_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPublic());
    }

    @Test
    public void T011_AddToMethodTable_MockClass_Method_IsAbstract_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsAbstract());
    }

    @Test
    public void T012_AddToMethodTable_MockClass_Method_IsAbstract_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsInterface());
    }

    @Test
    public void T013_AddToMethodTable_MockClass_Method_IsProtected_And_IsAbstract_Return_true_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsProtected());
        assertTrue(output.get(0).getIsAbstract());
    }

    @Test
    public void T014_AddToMethodTable_MockClass_Method_IsProtected_And_IsAbstract_Return_false_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPublic());
        assertTrue(output.get(0).getIsAbstract());
    }

    @Test
    public void T015_AddToMethodTable_MockClass_Method_IsProtected_And_IsAbstract_Return_true_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsProtected());
        assertFalse(output.get(0).getIsStatic());
    }

    @Test
    public void T016_AddToMethodTable_MockClass_Method_IsProtected_And_IsAbstract_Return_false_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassProtectedAbstract.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsPublic());
        assertFalse(output.get(0).getIsStatic());
    }

    @Test
    public void T017_AddToMethodTable_MockClass_Method_IsPrivate_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassPrivate.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsPrivate());
    }

    @Test
    public void T018_AddToMethodTable_MockClass_Method_IsPrivate_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassPrivate.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsProtected());
    }

    @Test
    public void T019_AddToMethodTable_MockClass_Method_Throws_IOException_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassException.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getExpStr().equals("IOException, "));
    }

    @Test
    public void T020_AddToMethodTable_MockClass_Method_Throws_IOException_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockClassException.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getExpStr().equals(""));
    }

    @Test
    public void T021_AddToMethodTable_MockClass_Method_IsFinal_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";
        Class<?> cl = DataSourceAdapterMockClassFinal.class;
        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsFinal());
    }

    @Test
    public void T022_AddToMethodTable_MockClass_Method_IsFinal_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";
        Class<?> cl = DataSourceAdapterMockClassFinal.class;
        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsStatic());
    }

    @Test
    public void T023_AddToMethodTable_MockClass_Method_IsInterface_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";
        Class<?> cl = DataSourceAdapterMockClassInterface.class;
        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getIsInterface());
    }

    @Test
    public void T024_AddToMethodTable_MockClass_Method_IsInterface_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";
        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;
        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getIsInterface());
    }

    @Test
    public void T025_AddToMethodTable_MockClass_Method_ReturnProperPath_Return_true() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertTrue(output.get(0).getLocation().equals(path));

    }

    @Test
    public void T026_AddToMethodTable_MockClass_Method_ReturnProperPath_Return_false() {
        DataSourceAdapterClassDataEntriesParser parser = new DataSourceAdapterClassDataEntriesParser();
        ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
        String path = "test";

        Class<?> cl = DataSourceAdapterMockMethodClassStatic.class;

        parser.AddToMethodTable(output, cl, path);
        assertFalse(output.get(0).getLocation().equals(cl));

    }
}
