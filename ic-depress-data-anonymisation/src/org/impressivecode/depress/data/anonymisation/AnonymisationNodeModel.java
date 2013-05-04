package org.impressivecode.depress.data.anonymisation;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.impressivecode.depress.data.objects.PropertiesValidator;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.ContainerTable;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.workflow.BufferedDataTableView;

import sun.io.Converters;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * This is the model implementation of Anonymisation. Encrypts and decrypts selected 
 * columns from input data set using Blowfish cryptographic algorithm.
 * 
 * @author Andrzej Dudek
 * @author Marcin Bogusz
 * @author Konrad Kocik
 * @author Artur Bilski
 */
public class AnonymisationNodeModel extends NodeModel {

    static final String COLUMNS = "columns";
    static final String KEY = "key";
    static final int INPUT_PORT = 0;
    public static final int KEY_LENGTH = 128;

    /**
     * Constructor for the node model.
     */
    protected AnonymisationNodeModel() {

        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        
        // TODO: Return a BufferedDataTable for each output port
        DataTableSpec spec = new DataTableSpec();
        BufferedDataContainer container = exec.createDataContainer(spec);
        
        //create a list to hold DataColumnsSpec 
         DataColumnSpec[] tableDataColumnsSpec = new DataColumnSpec[inData.length];
         //variable to get number of iteration(columns and rows)
        int i=0;
        int j=0;
        for(BufferedDataTable table : inData){
            
            Iterator<DataColumnSpec> columnsItarator = table.getDataTableSpec().iterator(); 
          //Create a DataTableSpec and BufferedDataContainer, there are two columns (it's a demo) ??  
           

            while(columnsItarator.hasNext())
            {   
                //adding the specyfication of aktual columns that is iterate
               // tableDataColumnsSpec.add(new DataColumnSpecCreator(columnsItarator.toString(), StringCell.TYPE).createSpec());
                
              //TODO
                //iterating every column. Should iterate only those from NodeDialog.ColumnFilter.
                //Other just add to output. (but how?)
                DataColumnSpec columnSpec = columnsItarator.next();
                for(DataCell cell : columnSpec.getDomain().getValues())
                {
                    j++;
                    //adding the custom columns specyfication to the list of colums for output
                    tableDataColumnsSpec[i] = new DataColumnSpecCreator(columnsItarator.toString(), StringCell.TYPE).createSpec();
                    if(!cell.isMissing())
                    {
                        //TODO
                        //iterating every cell. Here should be anonymisation for that column
                        String value = cell.toString();
                        //adding the value to the container
                        container.addRowToTable(new DefaultRow(new RowKey(new String()+j), new DataCell[]{
                            new StringCell(value.toString())
                        }));
                        value.toString();
                    }
                }
            }
            i++;
        }
        //add all spec columns to the output 
      
        spec = new DataTableSpec(tableDataColumnsSpec);
        
        //close the contaier and return the output 
        container.close();
        
        //marge all bufferedatatable to one output 
        
        BufferedDataTable[] output = {container.getTable()};
        
        return output;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

        // TODO: generated method stub
        return new DataTableSpec[] { null };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // TODO: generated method stub
        if (settings.keySet().contains(KEY)) {
            String path = settings.getRowKey(KEY).getString();
            PropertiesValidator.isKeyFileCorrect(path);
        }

        if (settings.keySet().contains(COLUMNS)) {
            PropertiesValidator.columnsCheck(settings.getConfig(COLUMNS));
        }

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}
