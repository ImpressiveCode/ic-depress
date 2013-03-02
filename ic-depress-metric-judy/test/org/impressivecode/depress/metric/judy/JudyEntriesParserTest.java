package org.impressivecode.depress.metric.judy;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.impressivecode.depress.metric.judy.JudyXmlResult.Classes.Class;
import org.junit.Test;

public class JudyEntriesParserTest {

    @Test
    public void shouldUnmarshalResults() throws JAXBException {
        List<Class> results = JudyEntriesParser.unmarshalResults(getClass().getResource("judy.xml").getPath());
        assertEquals(results.size(), 2);
    }

}
