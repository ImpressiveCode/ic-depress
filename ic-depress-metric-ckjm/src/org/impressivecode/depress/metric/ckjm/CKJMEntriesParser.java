package org.impressivecode.depress.metric.ckjm;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.impressivecode.depress.metric.ckjm.CKJMXmlResult.Classes.Class;

class CKJMEntriesParser {

    static List<Class> unmarshalResults(final String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CKJMXmlResult.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        CKJMXmlResult result = (CKJMXmlResult) unmarshaller.unmarshal(new File(path));
        return result.getClasses().getClazz();
    }

}
