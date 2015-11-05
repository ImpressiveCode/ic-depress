/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.mg.po;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterators.size;
import static com.google.common.collect.Range.atLeast;
import static java.util.Collections.unmodifiableMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class PeopleOrganizationMetricProcessor {

    private static final float MIN_CONTRIBUTION = 0.1f;
    private static final String ORG_STRUCTURE_SEPARATOR = ";";
    private static final double PERCENTAGE_OF_MAJOR_ORGANISATION_INVOLVED = 0.75;
    private final Map<String, ChangeData> changeData;
    private final Map<String, TeamMemberData> engineersData;

    public PeopleOrganizationMetricProcessor(final Map<String, ChangeData> changeData,
            final Map<String, TeamMemberData> engineersData) {
        Preconditions.checkNotNull(changeData, "Change history has to be set");
        Preconditions.checkNotNull(engineersData, "Engineers data has to be set");

        this.changeData = unmodifiableMap(changeData);
        this.engineersData = unmodifiableMap(engineersData);
    }

    public List<PeopleOrganizationMetric> buildMetric() {
        List<PeopleOrganizationMetric> metricData = Lists.newArrayListWithExpectedSize(changeData.size());
        for (Entry<String, ChangeData> change : changeData.entrySet()) {
            assertChangeData(change.getValue());
            metricData.add(computeMetric(change.getValue()));
        }
        return metricData;
    }

    private PeopleOrganizationMetric computeMetric(final ChangeData change) {
        PeopleOrganizationMetric metric = new PeopleOrganizationMetric();
        metric.setClassName(change.getClassName());
        metric.setEF(computeEditFrequency(change));
        metric.setNOE(computeNumberOfEngineers(change));
        metric.setNOEE(computeNumberOfExEngineers(change));
        metric.setNOE1(computeAbsoluteEngineersSkills(1, change));
        metric.setNOE2(computeAbsoluteEngineersSkills(2, change));
        metric.setNOE3(computeAbsoluteEngineersSkills(3, change));
        metric.setNOE4(computeAbsoluteEngineersSkills(4, change));
        metric.setNOE5(computeAbsoluteEngineersSkills(5, change));
        metric.setDMO(computeDepthOfMasterOwnership(change));
        metric.setPO(computePercantageOfOrgContributionToDevelopement(change));
        metric.setOCO(computeLevelOfOrganizationalCodeOwnership(change));
        metric.setOOW(computeOverallOrganizationOwnership(change));
        metric.setOIF(computeOrganizationIntersectionFactor(change));
        return metric;
    }

    private Integer computeOrganizationIntersectionFactor(final ChangeData change) {
        float numberOfRequiredEdits = change.getInvolvedEngineers().size() * MIN_CONTRIBUTION;
        Map<String, Integer> commitsProTopOrg = getNumberOfCommitsPerTopOrganization(change);
        int size = size(filter(commitsProTopOrg.values(), atLeast(Math.round(numberOfRequiredEdits))).iterator());
        return size == 0 ? null : size;
    }

    private Double computeOverallOrganizationOwnership(final ChangeData change) {
        final String mo = getMasterOwnership(change);
        if (mo == null) {
            return null;
        }
        Iterable<String> iterator = Iterables.filter(copyOf(change.getInvolvedEngineers()), new Predicate<String>() {
            @Override
            public boolean apply(final String engineer) {
                return engineersData.get(engineer).getOrganizationPath().equals(mo);
            }
        });
        return (double) size(iterator.iterator()) / (double) copyOf(change.getInvolvedEngineers()).size();
    }

    private Double computeLevelOfOrganizationalCodeOwnership(final ChangeData change) {
        String mo = getMasterOwnership(change);
        int orgCommits = 0;
        if (mo == null) {
            Map<String, Integer> commitsProTopOrg = getNumberOfCommitsPerTopOrganization(change);
            orgCommits = Ordering.<Integer> natural().max(commitsProTopOrg.values());
        } else {
            String topMoOrg = getTopOrganizationOfOrganization(mo);
            for (String engineer : change.getInvolvedEngineers()) {
                if (engineersData.get(engineer).getOrganizationPath().startsWith(topMoOrg)) {
                    orgCommits++;
                }
            }
        }
        int allCommits = change.getInvolvedEngineers().size();
        return (double) orgCommits / allCommits;
    }

    private Map<String, Integer> getNumberOfCommitsPerTopOrganization(final ChangeData change) {
        Map<String, Integer> commitsProTopOrg = Maps.newHashMap();
        for (String engineer : change.getInvolvedEngineers()) {
            String top = getTopOrganizationOfOrganization(engineersData.get(engineer).getOrganizationPath());
            Integer currCommits = commitsProTopOrg.get(top) != null ? commitsProTopOrg.get(top) : 0;
            commitsProTopOrg.put(top, 1 + currCommits);
        }
        return commitsProTopOrg;
    }

    private Double computePercantageOfOrgContributionToDevelopement(final ChangeData change) {
        String dmo = getMasterOwnership(change);
        if (dmo == null) {
            return null;
        }
        String topOrg = getTopOrganizationOfOrganization(dmo);

        double numberOfEngineersInTopOrg = 0;
        double numberOfEngineersInDMO = 0;
        for (TeamMemberData teamMember : engineersData.values()) {
            if (teamMember.getOrganizationPath().startsWith(topOrg)) {
                numberOfEngineersInTopOrg++;
            }

            if (teamMember.getOrganizationPath().equals(dmo)) {
                numberOfEngineersInDMO++;
            }
        }
        return numberOfEngineersInDMO / numberOfEngineersInTopOrg * 100.0;
    }

    private String getTopOrganizationOfOrganization(final String dmo) {
        return dmo.split(ORG_STRUCTURE_SEPARATOR)[0] + ORG_STRUCTURE_SEPARATOR;
    }

    private Integer computeAbsoluteEngineersSkills(final Integer level, final ChangeData change) {
        int skillsCounter = 0;
        for (String engineer : change.getInvolvedEngineers()) {
            TeamMemberData teamMemberData = engineersData.get(engineer);
            if (level.equals(teamMemberData.getExLevel())) {
                skillsCounter++;
            }
        }
        return skillsCounter;
    }

    private Integer computeDepthOfMasterOwnership(final ChangeData change) {
        String dmo = getMasterOwnership(change);
        return dmo == null ? null : dmo.split(ORG_STRUCTURE_SEPARATOR).length;
    }

    private String getMasterOwnership(final ChangeData change) {
        ImmutableListMultimap<String, String> indexed = groupByOrganization(change);
        String dmo = null;
        double majorChange = change.getInvolvedEngineers().size() * PERCENTAGE_OF_MAJOR_ORGANISATION_INVOLVED;
        for (String key : indexed.keySet()) {
            int editsCount = indexed.get(key).size();
            if (editsCount >= majorChange) {
                dmo = key;
            }
        }
        return dmo;
    }

    private ImmutableListMultimap<String, String> groupByOrganization(final ChangeData change) {
        ImmutableListMultimap<String, String> indexed = Multimaps.index(change.getInvolvedEngineers(),
                new Function<String, String>() {
            @Override
            public String apply(final String name) {
                return engineersData.get(name).getOrganizationPath();
            }
        });
        return indexed;
    }

    private Integer computeNumberOfExEngineers(final ChangeData change) {
        HashSet<String> uniqueEngineers = Sets.newHashSet(change.getInvolvedEngineers());
        int number = 0;
        for (String engineer : uniqueEngineers) {
            TeamMemberData teamMember = engineersData.get(engineer);
            if (teamMember.isExEngineer()) {
                number++;
            }
        }

        return number;
    }

    private Integer computeNumberOfEngineers(final ChangeData change) {
        return Sets.newHashSet(change.getInvolvedEngineers()).size();
    }

    private Integer computeEditFrequency(final ChangeData change) {
        return change.getInvolvedEngineers().size();
    }

    private void assertChangeData(final ChangeData changeData2) {
        for (String engineer : changeData2.getInvolvedEngineers()) {
            assertTeamMemberExists(engineer);
            assertSkillLevel(engineer);
            assertOrgData(engineer);
        }
    }

    private void assertOrgData(final String engineer) {
        if (engineersData.get(engineer).getOrganizationPath() == null) {
            throw new IllegalArgumentException("Engineer [" + engineer + "] has to have an organization data.");
        }
    }

    private void assertTeamMemberExists(final String engineer) {
        if (engineersData.get(engineer) == null) {
            throw new IllegalArgumentException("Unable to find engineer [" + engineer
                    + "] data. Please check coressponding engineers table");
        }
    }

    private void assertSkillLevel(final String engineer) {
        Integer level = engineersData.get(engineer).getExLevel();
        if (level == null || !(0 < level && level < 6)) {
            throw new IllegalArgumentException("Unsupported engineer [" + engineer + "] level [" + level + "]");
        }
    }
}
