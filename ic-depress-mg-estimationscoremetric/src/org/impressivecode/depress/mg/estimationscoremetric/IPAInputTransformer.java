package org.impressivecode.depress.mg.estimationscoremetric;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.impressivecode.depress.common.DataTableSpecUtils;
import org.impressivecode.depress.common.InputTransformer;
import org.impressivecode.depress.common.TableCellReader;
import org.impressivecode.depress.its.ITSAdapterTableFactory;
import org.impressivecode.depress.its.ITSDataHolder;
import org.impressivecode.depress.its.ITSDataType;
import org.impressivecode.depress.its.ITSResolution;
import org.impressivecode.depress.mg.ipa.IssuesMetricTableFactory;
import org.impressivecode.depress.mg.ipa.IssuesMetricType;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowIterator;
import org.knime.core.node.InvalidSettingsException;

import com.google.common.collect.Iterables;

public class IPAInputTransformer implements InputTransformer<IssuesMetricType>{

	private DataTableSpec minimalTableSpec;
    private DataTableSpec inputTableSpec;
    private static final String ISSUES = "Issues";
    public IPAInputTransformer() {
    }

    @Override
    public List<IssuesMetricType> transform(final DataTable inTable) {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");
        checkNotNull(inTable, "InTable has to be set");
        List<IssuesMetricType> issueData = newArrayListWithExpectedSize(1000);
        RowIterator iterator = inTable.iterator();
        while (iterator.hasNext()) {
            issueData.add(transformRow(iterator.next()));
        }
        return issueData;
    }

   /* public ITSDataHolder transformToDataHolder(final DataTable inTable) {
        return new ITSDataHolder(transform(inTable));
    }*/

    @Override
    public IssuesMetricType transformRow(final DataRow row) {
        TableCellReader reader = new TableCellReader(this.inputTableSpec, row);
        IssuesMetricType its = new IssuesMetricType();
        //add additional if required
        List<ITSDataType> ids = new ArrayList<ITSDataType>();
        its.setResourceName(reader.key());
        DataCell cell = row.getCell(this.inputTableSpec.findColumnIndex(ISSUES));
        if(cell.isMissing()){
        	
        }
        else{
        	List<String> stringListCellSet = reader.stringListCellSet(ISSUES);            
            for (String bugId : stringListCellSet) {
            	ITSDataType issue = new ITSDataType();
            	issue.setIssueId(bugId);
            	ids.add(issue);
    		} 
        }
               
        its.setIssues(ids);
        //add check if minimaldata set requires column for large data e.g. comments or description
        return its;
    }

    private ITSResolution readResolution(final TableCellReader reader) {
        String resolution = reader.stringOptional(ITSAdapterTableFactory.RESOLUTION);
        if(resolution == null){
            return null;
        }else {
            return ITSResolution.valueOf(resolution);
        }
    }

    @Override
    public InputTransformer<IssuesMetricType> validate() throws InvalidSettingsException {
        checkNotNull(this.minimalTableSpec, "Minimal DataTableSpec hat to be set");
        checkNotNull(this.inputTableSpec, "Input DataTableSpec hat to be set");

        Set<String> missing = DataTableSpecUtils.findMissingColumnSubset(this.inputTableSpec, this.minimalTableSpec);
        if (!missing.isEmpty()) {
            throw new InvalidSettingsException("History data table does not contain required columns. Missing: "
                    + Iterables.toString(missing));
        }
        return this;
    }

    @Override
    public InputTransformer<IssuesMetricType> setMinimalSpec(final DataTableSpec spec) {
        this.minimalTableSpec = spec;
        return this;
    }

    @Override
    public InputTransformer<IssuesMetricType> setInputSpec(final DataTableSpec spec) {
        this.inputTableSpec = spec;
        return this;
    }

    @Override
    public InputTransformer<IssuesMetricType> setMinimalOrSpec(final DataColumnSpec[] orSpec) {
        throw new IllegalStateException("Unsupported operation");
    }
	
}
