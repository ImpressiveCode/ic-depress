package org.impressivecode.depress.metric.judy;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.impressivecode.depress.metric.judy.JudyXmlResult.Classes.Class;

class JudyEntriesParser {

	static List<Class> unmarshalResults(final String path) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(JudyXmlResult.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		JudyXmlResult result = (JudyXmlResult) unmarshaller.unmarshal(new File(path));
		return result.getClasses().getClazz();
	}

}
