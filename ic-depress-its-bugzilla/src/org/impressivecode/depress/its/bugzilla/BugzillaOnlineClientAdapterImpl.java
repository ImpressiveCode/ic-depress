package org.impressivecode.depress.its.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.impressivecode.depress.its.ITSDataType;

import com.google.common.collect.Lists;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public class BugzillaOnlineClientAdapterImpl implements BugzillaOnlineClientAdapter {

	private BugzillaOnlineClient bugzillaClient;

	public BugzillaOnlineClientAdapterImpl(String urlAddress) {
		bugzillaClient = buildClient(urlAddress);
	}

	private BugzillaOnlineClient buildClient(String urlAddress) {
		URL url = null;
		try {
			url = new URL(urlAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace(); // TODO handle exception
		}

		return new BugzillaOnlineXmlRpcClient(url);
	}

	@Override
	public List<ITSDataType> listEntries() {
		return Lists.newArrayList(); // TODO implement fetching entries
	}

}
