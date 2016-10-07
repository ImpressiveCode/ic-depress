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
package org.impressivecode.depress.its.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class ITSDataHolder {

    private final ImmutableMap<String, ITSDataType> issues;

    ITSDataHolder(final List<ITSDataType> issueData) {
        this.issues = Maps.uniqueIndex(issueData, new Function<ITSDataType, String>() {
            @Override
            public String apply(final ITSDataType from) {
                return from.getIssueId();
            }
        });
    }

    public Set<ITSDataType> issues(final Set<String> ids) {
        HashSet<ITSDataType> items = Sets.newHashSet();
        for (String id : ids) {
            ITSDataType item = this.issues.get(id);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

}
