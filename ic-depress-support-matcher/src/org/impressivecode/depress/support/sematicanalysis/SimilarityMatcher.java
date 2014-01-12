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

import uk.ac.shef.wit.simmetrics.similaritymetrics.ChapmanLengthDeviation;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.OverlapCoefficient;

/**
 * 
 * @author Michal Jawulski, Piotr Lewicki, Maciej Luzniak, ImpressiveCode
 * 
 */
public class SimilarityMatcher {

    private static double doLevenstheinTest(String string1, String string2) {
        Levenshtein levenstheinTest = new Levenshtein();
        return levenstheinTest.getSimilarity(string1, string2);
    }

    private static double doJaroWinklerTest(String string1, String string2) {
        JaroWinkler jaroWinklerTest = new JaroWinkler();
        return jaroWinklerTest.getSimilarity(string1, string2);
    }

    private static double doChapmanTest(String string1, String string2) {
        ChapmanLengthDeviation chapmanTest = new ChapmanLengthDeviation();
        return chapmanTest.getSimilarity(string1, string2);
    }

    private static double doOverlapTest(String string1, String string2) {
        OverlapCoefficient overlapTest = new OverlapCoefficient();
        return overlapTest.getSimilarity(string1, string2);
    }

    public static double doSimilarityTest(String string1, String string2, String selectedAlgorithm) throws Exception {
        if (selectedAlgorithm.equals(Configuration.JARO_WINKLER_ALGHORITM)) {
            return doJaroWinklerTest(string1, string2);
        } else if (selectedAlgorithm.equals(Configuration.LEVENSTHEIN_ALGHORITM)) {
            return doLevenstheinTest(string1, string2);
        } else if (selectedAlgorithm.equals(Configuration.CHAPMAN_ALGHORITM)) {
            return doChapmanTest(string1, string2);
        } else if (selectedAlgorithm.equals(Configuration.OVERLAP_ALGHORITM)) {
            return doOverlapTest(string1, string2);
        } else {
            throw new Exception("Unsupported Algorithm!");
        }
    }
}
