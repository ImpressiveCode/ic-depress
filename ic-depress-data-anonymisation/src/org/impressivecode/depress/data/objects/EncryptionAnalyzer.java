package org.impressivecode.depress.data.objects;

import jsc.distributions.Normal;
import jsc.goodnessfit.KolmogorovTest;

public abstract class EncryptionAnalyzer
{
    public static Boolean isThisEncrypted(String stringToAnalyze)
    {
        byte[] stringBytes = stringToAnalyze.getBytes();
        double[] testInput = new double[stringBytes.length];
        
        for (int i = 0; i < stringBytes.length; i++)
        {
            testInput[i] = (double)testInput[i];
        }
        
        KolmogorovTest kt = new KolmogorovTest(testInput, new Normal());
        // hipoteza zerowa tego testu jest to, ze testInput ma rozklad normalny
        // ale jak wyciagnac ta informacje z obiektu kt - nie mam pojecia...
        
        return false;
    }
}
