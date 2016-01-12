package org.impressivecode.depress.test.scm.git;
import org.knime.testing.core.AbstractTestcaseCollector;

	/**
	 * KNIME TestCaseCollector.
	 * 
	 * @author aiche
	 */
	public class DepressTestcaseCollector extends AbstractTestcaseCollector {

	    /**
	     * C'tor
	     */
	    public DepressTestcaseCollector() {
	    }

	    /**
	     * C'tor.
	     * 
	     * @param excludedTestcases
	     */
	    public DepressTestcaseCollector(Class<?>... excludedTestcases) {
	        super(excludedTestcases);
	    }

	}

