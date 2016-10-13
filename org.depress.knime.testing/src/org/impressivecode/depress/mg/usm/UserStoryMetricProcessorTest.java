package org.impressivecode.depress.mg.usm;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import org.impressivecode.depress.its.common.ITSDataType;
import org.impressivecode.depress.mg.usm.UserStoryMetricProcessorImplement;
import org.junit.Before;
import org.junit.Test;

public class UserStoryMetricProcessorTest {
	
	@Before
    public void setUp() {

    }

	@Test
	public void shouldReturnNumberOfComments() {
	
		// given
		ITSDataType itsDataType = new ITSDataType();
		itsDataType.setComments(Arrays.asList("Commen 1", "Commen 2", "Commen 3"));
		
		//when
		Object comments = new UserStoryMetricProcessorImplement().computeMetric(itsDataType);
		
		//then
		assertThat(comments).isEqualTo(3);
	}
	
}
