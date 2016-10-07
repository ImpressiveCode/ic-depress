package org.impressivecode.depress.mr.googlemetrics;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.impressivecode.depress.mr.googlemetrics.GoogleMetricsXmlResult;
import org.impressivecode.depress.mr.googlemetrics.GoogleMetricsXmlResult.MetricResultScope;

import com.google.common.base.Preconditions;

public class GoogleMetricsEntriesParser {
	   public static List<MetricResultScope> unmarshalResults(final String path) throws JAXBException {
	        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");
	        JAXBContext jaxbContext = JAXBContext.newInstance(GoogleMetricsXmlResult.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

	        GoogleMetricsXmlResult result = (GoogleMetricsXmlResult) unmarshaller.unmarshal(new File(path));
	        return result.getMetricResultScope();
	    }

}
