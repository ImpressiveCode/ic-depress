package org.impressivecode.depress.mg.usm;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.impressivecode.depress.its.common.ITSDataType;
import org.impressivecode.depress.mg.usm.NumberOfCommentsMetricProcessor;
import org.junit.Before;
import org.junit.Test;

public class NumberOfCommentsMetricProcessorTest {
	
	@Before
    public void setUp() {

    }

	@Test
	public void shouldReturnCommentsMetrics() {
	
		// given
		ITSDataType itsDataType = new ITSDataType();
		itsDataType.setComments(Arrays.asList("Commen 1 { return }", "Commen 2 http://dummy.com", "Commen 3 https://dummy.com @Overwrite public void func() {}"));
		
		//when
		List<Integer> comments = (List<Integer>) new NumberOfCommentsMetricProcessor().computeMetric(itsDataType);
		
		//then
		assertThat(comments.get(0)).isEqualTo(3);
		assertThat(comments.get(1)).isEqualTo(2);
		assertThat(comments.get(2)).isEqualTo(2);
	}
	
}
