package org.impressivecode.depress.common;

import java.util.List;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

public interface OutputTransformer<T> {

    public abstract BufferedDataTable transform(final List<T> data, final ExecutionContext exec)
            throws CanceledExecutionException;

}