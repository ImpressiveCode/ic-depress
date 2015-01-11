package org.impressivecode.depress.its.oschangemanagement;

import static org.impressivecode.depress.its.ITSAdapterTableFactory.createDataColumnSpec;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import org.impressivecode.depress.its.ITSAdapterTransformer;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSMappingManager;
import org.impressivecode.depress.its.ITSOnlineNodeModel;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementJiraRationalAdapterUriBuilder;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.parser.OsChangeManagementRationalAdapterParser;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

public class OsChangeManagementAdapterNodeModel extends ITSOnlineNodeModel {

	private static final int NUMBER_OF_INPUT_PORTS = 0;
    private static final int NUMBER_OF_OUTPUT_PORTS = 1;
    private static final String CFG_ITS_PLUGIN = "plugin";
    private static final int THREAD_COUNT = 10;
    private final SettingsModelString pluginSettings = createPluginSettings();
    
    private OsChangeManagementRestClient client = new OsChangeManagementRestClient();
    private OsChangeManagementJiraRationalAdapterUriBuilder builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
    private ExecutorService executorService;
    private ExecutionContext exec;
    private ExecutionMonitor issueCountMonitor;
    private ExecutionMonitor issueListMonitor;
    private ExecutionMonitor issueHistoryMonitor;
    
	protected OsChangeManagementAdapterNodeModel() {
		super(NUMBER_OF_INPUT_PORTS, NUMBER_OF_OUTPUT_PORTS);
	}
	
	public static ITSMappingManager createMapping() {
        return new ITSMappingManager();
    }

	public static SettingsModelString createPluginSettings() {
        return new SettingsModelString(CFG_ITS_PLUGIN, DEFAULT_STRING_VALUE);
    }
	
	@Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[NUMBER_OF_OUTPUT_PORTS];
    }
	
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
		
		ArrayList<String> projectList = (ArrayList<String>) getProjectList();
		ArrayList<URI> uriList = new ArrayList<URI>();
		this.exec = exec;
		
		List<URI> issueLinks = new ArrayList<URI>();
		builder.setHostname(getURL());
		builder.setMode(Mode.CHANGE_REQUEST);
		
		for(String project : projectList){
			if(project==null){
				continue;
			}
			builder.setProject(project);
			builder.setStartIndex(0);
			
			//executorService = Executors.newFixedThreadPool(THREAD_COUNT);
			uriList.addAll(getIssueList());
		}
		 List<ITSDataType> issues = new ArrayList<ITSDataType>();
		 
		 for(URI uri : uriList){
			 
		 }
		 
		 
		 
		 BufferedDataTable out = transform(issues, exec);
		 return new BufferedDataTable[] { out};
	}
	
	private List<URI> getIssueList() {
		int totalIssues=0;
		List<URI> issueLinks = new ArrayList<>();
		try {
			totalIssues = getIssuesCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(totalIssues > builder.getPageSize()){
			while (totalIssues > 0){
				issueLinks.add(builder.build());
	            builder.prepareNextLink();
	            totalIssues -= builder.getPageSize();
			}
		}else {
			issueLinks.add(builder.build());
		}
		 
		
		return issueLinks;
	}
	
	 private int getIssuesCount() throws Exception {
		 String login = getLogin();
		 String password = getPassword();
		 String pluginName = "OSLCCM"; // zmieniæ trzeba póŸniej
		 String rawData = client.getJSON(builder.build(), login, password);
		 switch (pluginName){
	        case OsChangeManagementAdapterNodeDialog.OSLCCM:
	        	return new OsChangeManagementRationalAdapterParser().getIssueCount(rawData);
			default:
				return 0;
	        }
	    }

	protected List<String> getProjectList(){
		ArrayList<String> projectList = new ArrayList<String>();
		if(getProductName()!=null){
			projectList.add(getProductName());
		}
		else{
			List<OsChangeManagementProject> projects;
			try {
				projects = getList(Mode.PROJECT_LIST);
				for (OsChangeManagementProject item : projects) {
					projectList.add(getLastPathFragment(item.getUri()));
				}
			}catch (Exception e) {
					Logger.getLogger("Error").severe("Error during connection, list could not be downloaded");
				}
		}
		return projectList;
	}
	
	private String getLastPathFragment(String path){
		return path.substring(path.lastIndexOf('/') + 1);
	}
	
	private <T> List<T> getList(Mode mode) throws Exception {
    	String urlString = getURL();
        String login = getLogin();
        String password = getPassword();
        String pluginName = "OSLCCM"; // zmieniæ trzeba póŸniej
        builder.setHostname(urlString);
        builder.setMode(mode);
        String rawData = client.getJSON(builder.build(), login, password);
        switch (pluginName){
        case OsChangeManagementAdapterNodeDialog.OSLCCM:
        	return (List<T>) new OsChangeManagementRationalAdapterParser().getProjectList(rawData);
		default:
			return null;
        }
	}

	 private BufferedDataTable transform(final List<ITSDataType> entries, final ExecutionContext exec)
	            throws CanceledExecutionException {
	        ITSAdapterTransformer transformer = new ITSAdapterTransformer(createDataColumnSpec());
	        return transformer.transform(entries, exec);
	    }
	
	@Override
	protected void saveSpecificSettingsTo(NodeSettingsWO settings) {
		//pluginSettings.saveSettingsTo(settings);
	}

	@Override
	protected void validateSpecificSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		//pluginSettings.validateSettings(settings);
	}

	@Override
	protected void loadSpecificSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		//pluginSettings.loadSettingsFrom(settings);
	}
	
	

}
