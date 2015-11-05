package org.impressivecode.depress.mg.po;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.impressivecode.depress.mg.po.ChangeData;
import org.impressivecode.depress.mg.po.PeopleOrganizationMetric;
import org.impressivecode.depress.mg.po.PeopleOrganizationMetricProcessor;
import org.impressivecode.depress.mg.po.TeamMemberData;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PeopleOrganizationMetricProcessorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void shouldSetProperClassName() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getClassName(), "className");
    }

    @Test
    public void shouldComputeEditFrequency() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getEF(), Integer.valueOf(8));
    }

    @Test
    public void shouldComputeNumberOfEngineers() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getNOE(), Integer.valueOf(5));
    }

    @Test
    public void shouldComputeNumberOfExEngineers() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getNOEE(), Integer.valueOf(2));
    }

    @Test
    public void shouldComputeEngineersSkills() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getNOE1(), Integer.valueOf(4));
        assertEquals(metric.get(0).getNOE2(), Integer.valueOf(1));
        assertEquals(metric.get(0).getNOE3(), Integer.valueOf(1));
        assertEquals(metric.get(0).getNOE4(), Integer.valueOf(1));
        assertEquals(metric.get(0).getNOE5(), Integer.valueOf(1));
    }

    @Test
    public void shouldComputeDepthOfMasterOwnership() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getDMO(), Integer.valueOf(3));
    }

    @Test
    public void shouldComputeEmtyDepthOfMasterOwnership() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessorWithoutMO();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getDMO(), null);
    }

    @Test
    public void shouldComputePercantageOfContributingToDevelopment() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getPO(), 4.0/5.0*100);
    }


    @Test
    public void shouldComputeEmptyPercantageOfContributingToDevelopment() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessorWithoutMO();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getPO(), null);
    }


    @Test
    public void shouldComputeLevelOfOrganizationCodeOfOwnership() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOCO(), 7.0/8.0);
    }



    @Test
    public void shouldComputeLevelOfOrganizationCodeOfOwnershipWhenNoMO() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessorWithoutMO();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOCO(), 2.0/3.0);
    }

    @Test
    public void shouldComputeLevelOfOverallOrganizationOwnership() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOOW(), 3.0/5.0);
    }

    @Test
    public void shouldComputeLevelOfOverallOrganizationOwnershipWhenNoMO() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessorWithoutMO();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOOW(), null);
    }

    @Test
    public void shouldComputeOrganizationIntersectionFactor() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessor();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOIF(), Integer.valueOf(2));
    }

    @Test
    public void shouldComputeOrganizationIntersectionFactorWhenBiggerChangeSet() {
        // given
        PeopleOrganizationMetricProcessor processor = createMetricProcessorWithManyChanges();
        // when
        List<PeopleOrganizationMetric> metric = processor.buildMetric();
        // then
        assertEquals(metric.get(0).getOIF(), Integer.valueOf(3));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoEngineersFound() {
        // given
        ChangeData changeData = createChangeData("e1", "e2", "e3");
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history,
                Maps.<String, TeamMemberData> newHashMap());
        // when
        processor.buildMetric();

        fail("Exception should be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoOrganizationFound() {
        // given
        ChangeData changeData = createChangeData("e1", "e2", "e3");
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        TeamMemberData teamMember = createTeamMember("e3", false, 2, null);
        Map<String, TeamMemberData> teamData = Maps.newHashMap();
        teamData.put(teamMember.getName(), teamMember);
        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history, teamData);
        // when
        processor.buildMetric();

        fail("Exception should be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenWrongLevel() {
        // given
        ChangeData changeData = createChangeData("e1", "e2", "e3");
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        TeamMemberData teamMember = createTeamMember("e3", false, null, "AB");
        Map<String, TeamMemberData> teamData = Maps.newHashMap();
        teamData.put(teamMember.getName(), teamMember);
        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history, teamData);
        // when
        processor.buildMetric();

        fail("Exception should be thrown");
    }

    private PeopleOrganizationMetricProcessor createMetricProcessor() {
        ChangeData changeData = createChangeData("e1", "e1", "e1", "e1", "e2", "e3", "e4", "e5");
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        TeamMemberData teamMember1 = createTeamMember("e1", true, 1, "A;AB;ABC");
        TeamMemberData teamMember2 = createTeamMember("e2", true, 3, "A;AB;ABC");
        TeamMemberData teamMember3 = createTeamMember("e3", false, 2, "A;AB");
        TeamMemberData teamMember4 = createTeamMember("e4", false, 4, "D;DF");
        TeamMemberData teamMember5 = createTeamMember("e5", false, 5, "A;AB;ABC");
        TeamMemberData teamMember6 = createTeamMember("e6", false, 5, "A;AB;ABC");
        Map<String, TeamMemberData> teamData = Maps.newHashMap();
        teamData.put(teamMember1.getName(), teamMember1);
        teamData.put(teamMember2.getName(), teamMember2);
        teamData.put(teamMember3.getName(), teamMember3);
        teamData.put(teamMember4.getName(), teamMember4);
        teamData.put(teamMember5.getName(), teamMember5);
        teamData.put(teamMember6.getName(), teamMember6);
        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history, teamData);
        return processor;
    }

    private PeopleOrganizationMetricProcessor createMetricProcessorWithoutMO() {
        ChangeData changeData = createChangeData("e1", "e3", "e2");
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        TeamMemberData teamMember1 = createTeamMember("e1", true, 1, "A;AB;ABC");
        TeamMemberData teamMember2 = createTeamMember("e2", true, 1, "A;AB;ABC");
        TeamMemberData teamMember3 = createTeamMember("e3", true, 1, "D;AB");
        Map<String, TeamMemberData> teamData = Maps.newHashMap();
        teamData.put(teamMember1.getName(), teamMember1);
        teamData.put(teamMember2.getName(), teamMember2);
        teamData.put(teamMember3.getName(), teamMember3);

        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history, teamData);
        return processor;
    }

    private PeopleOrganizationMetricProcessor createMetricProcessorWithManyChanges() {
        ChangeData changeData = createChangeData("e1", "e1", "e1", "e1", "e1", "e1", "e1", "e1", "e1", "e1", "e1",
                "e2", "e2", "e2", "e2", "e2", "e2", "e2", "e2", 
                "e3", "e3", "e3", "e3",
                "e4");//x24
        Map<String, ChangeData> history = new HashMap<>();
        history.put(changeData.getClassName(), changeData);
        TeamMemberData teamMember1 = createTeamMember("e1", true, 1, "A");
        TeamMemberData teamMember2 = createTeamMember("e2", true, 1, "B");
        TeamMemberData teamMember3 = createTeamMember("e3", true, 1, "C");
        TeamMemberData teamMember4 = createTeamMember("e4", true, 1, "D");

        Map<String, TeamMemberData> teamData = Maps.newHashMap();
        teamData.put(teamMember1.getName(), teamMember1);
        teamData.put(teamMember2.getName(), teamMember2);
        teamData.put(teamMember3.getName(), teamMember3);
        teamData.put(teamMember4.getName(), teamMember4);

        PeopleOrganizationMetricProcessor processor = new PeopleOrganizationMetricProcessor(history, teamData);
        return processor;
    }

    private TeamMemberData createTeamMember(final String name, final boolean external, final Integer level,
            final String organization) {
        TeamMemberData member = new TeamMemberData();
        member.setName(name);
        member.setExEngineer(external);
        member.setExLevel(level);
        member.setOrganizationPath(organization);
        return member;
    }

    private ChangeData createChangeData(final String... engineers) {
        ChangeData change = new ChangeData();
        change.setClassName("className");
        change.setInvolvedEngineers(Lists.newArrayList(engineers));
        return change;
    }
}
