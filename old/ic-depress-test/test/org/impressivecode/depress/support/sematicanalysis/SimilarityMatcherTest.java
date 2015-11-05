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
package org.impressivecode.depress.support.sematicanalysis;

import static org.fest.assertions.Assertions.assertThat;

import org.impressivecode.depress.support.sematicanalysis.Configuration;
import org.impressivecode.depress.support.sematicanalysis.SimilarityMatcher;
import org.junit.Test;

/**
 * 
 * @author Michal Jawulski, Piotr Lewicki, Maciej Luzniak, ImpressiveCode
 * 
 */
public class SimilarityMatcherTest {

	@Test
    public void comparsionLevelCheck() throws Exception {
        // given
        String str1 = "Some sample text";
        String str2 = "Very similar sample text";
        
        // when
        double comparsionResult = SimilarityMatcher.doSimilarityTest(str1, str2, Configuration.LEVENSTHEIN_ALGHORITM);

        // then
        assertThat(comparsionResult).isGreaterThanOrEqualTo(0);
        assertThat(comparsionResult).isLessThanOrEqualTo(1.0);
    }
	
}
