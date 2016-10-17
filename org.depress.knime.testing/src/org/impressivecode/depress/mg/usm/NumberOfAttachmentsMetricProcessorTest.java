package org.impressivecode.depress.mg.usm;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.impressivecode.depress.its.common.ITSDataType;
import org.junit.Before;
import org.junit.Test;

public class NumberOfAttachmentsMetricProcessorTest {
	
	@Before
    public void setUp() {

    }

	@Test
	public void shouldNotCrashWhenNonAttachments() {
		//when
		Object attachment = new NumberOfAttachmentsMetricProcessor().computeMetric(new ITSDataType());
		
		//then
		assertThat(attachment).isEqualTo(0);
		
	}
	
	@Test
	public void shouldReturnNumberOfAttachments() {
	
		// given
		ITSDataType itsDataType = new ITSDataType();
		Set<String> attachments = new HashSet();
		attachments.add("Attachment 1");
		attachments.add("Attachment 2");
		attachments.add("Attachment 3");
		itsDataType.setAttachments(attachments);
		
		//when
		Object attachmentsCount = new NumberOfAttachmentsMetricProcessor().computeMetric(itsDataType);
		
		//then
		assertThat(attachmentsCount).isEqualTo(3);
	}

}
