package org.impressivecode.depress.its.jiraonline;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class JqlDateFilterTest {
	private JqlDatesFilter filter;
	private Date startDate;
	private Date endDate;

	@Before
	public void setUp() throws ParseException {
		filter = new JqlDatesFilter();
		startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2008-12-01");
		endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2008-12-31");
	}

	@Test
	public void should_have_jira_date_format_pattern() {
		String actualPattern = filter.getDateFormatter().toPattern();
		String expectedPattern = "yyyy-MM-dd";

		assertThat(actualPattern, is(equalTo(expectedPattern)));
	}

	@Test
	public void should_create_start_and_end_dates_filter() {
		String actual = filter.createDatesFilter(startDate, endDate);
		String expected = createBothDatesExpectedFilterResult();

		assertThat(actual, is(notNullValue()));
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void should_create_start_only_date_filter() {
		String actual = filter.createDatesFilter(startDate, null);
		String expected = createStartDateOnlyExpectedFilterResult();

		assertThat(actual, is(notNullValue()));
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void should_create_end_only_date_filter() {
		String actual = filter.createDatesFilter(null, endDate);
		String expected = createEndDateOnlyExpectedFilterResult();

		assertThat(actual, is(notNullValue()));
		assertThat(actual, is(equalTo(expected)));
	}

	private String createBothDatesExpectedFilterResult() {
		StringBuilder expectedResult = new StringBuilder("created >= ");

		expectedResult.append(filter.formatDate(startDate));
		expectedResult.append(" AND created <= ");
		expectedResult.append(filter.formatDate(endDate));
		expectedResult.append(" OR resolutiondate >= ");
		expectedResult.append(filter.formatDate(startDate));
		expectedResult.append(" AND resolutiondate <= ");
		expectedResult.append(filter.formatDate(endDate));

		return expectedResult.toString();
	}

	private String createStartDateOnlyExpectedFilterResult() {
		StringBuilder expectedResult = new StringBuilder("created >= ");

		expectedResult.append(filter.formatDate(startDate));
		expectedResult.append(" OR resolutiondate >= ");
		expectedResult.append(filter.formatDate(startDate));

		return expectedResult.toString();
	}

	private String createEndDateOnlyExpectedFilterResult() {
		StringBuilder expectedResult = new StringBuilder("created <= ");

		expectedResult.append(filter.formatDate(endDate));
		expectedResult.append(" OR resolutiondate <= ");
		expectedResult.append(filter.formatDate(endDate));

		return expectedResult.toString();
	}
}
