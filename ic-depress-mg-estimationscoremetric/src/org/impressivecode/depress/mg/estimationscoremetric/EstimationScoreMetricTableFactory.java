package org.impressivecode.depress.mg.estimationscoremetric;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;

public class EstimationScoreMetricTableFactory {
	 public static final String OVER_ESTIMATED_HIGH = "Over Estimated High";
	 public static final String OVER_ESTIMATED_LOW = "Over Estimated Low";
	 public static final String CORRECTLY_ESTIMATED = "Correctly Estimated";
	 public static final String UNDER_ESTIMATED_LOW = "Under Estimated Low";
	 public static final String UNDER_ESTIMATED_HIGH = "Under Estimated High";
	 public static final String ESTIMATION_SCORE = "US_ESM";
	 public static final String WEIGHT_SCORE1 = "US_WESM1";
	 public static final String WEIGHT_SCORE2 = "US_WESM2";
	 public static final String WEIGHT_SCORE3 = "US_WESM3";
	 public static final String MEAN = "Mean";
	 public static final String VARIANCE = "Variance";
	 public static final String DEVATION = "Standart deviation";
	 public static final String SUM = "Sum";

	    private EstimationScoreMetricTableFactory() {

	    }

	    public static DataTableSpec[] createTableSpec() {
	        return new DataTableSpec[] { createDataColumnSpec() };
	    }

	    public static DataTableSpec createDataColumnSpec() {
	        DataColumnSpec[] allColSpecs = {
	                new DataColumnSpecCreator(OVER_ESTIMATED_HIGH, IntCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(OVER_ESTIMATED_LOW, IntCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(CORRECTLY_ESTIMATED, IntCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(UNDER_ESTIMATED_LOW, IntCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(UNDER_ESTIMATED_HIGH, IntCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(ESTIMATION_SCORE, DoubleCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(WEIGHT_SCORE1, DoubleCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(WEIGHT_SCORE2, DoubleCell.TYPE).createSpec(),
	                new DataColumnSpecCreator(WEIGHT_SCORE3, DoubleCell.TYPE).createSpec()};
	        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
	        return outputSpec;
	    }

	    public static DataRow  createTableRow(final EstimationScoreMetricType metric) {
	        DataRow row;
	        if(metric.getNumberOfIssues()==0){
	        	DataCell[] cells = { 
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?"),
		        		new MissingCell("?")
		                };
		        row = new DefaultRow(metric.getClassName(), cells);
	        }else
	        {
	        DataCell[] cells = { 
	        		new IntCell(metric.getHighOverEstimate()),
	        		new IntCell(metric.getLowOverEstimate()),
	        		new IntCell(metric.getCorrectEstimate()),
	        		new IntCell(metric.getLowUnderEstimate()),
	        		new IntCell(metric.getHighUnderEstimate()),
	        		new DoubleCell(metric.getEsm()),
	        		new DoubleCell(metric.getWesm1()),
	        		new DoubleCell(metric.getWesm2()),
	        		new DoubleCell(metric.getWesm3())
	                };
	        row = new DefaultRow(metric.getClassName(), cells);
	        }
	        return row;
	    }


}
