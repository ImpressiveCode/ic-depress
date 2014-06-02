package org.impressivecode.depress.metric.astcompare.svn;

import java.util.Comparator;
import org.eclipse.team.core.history.IFileRevision;

/**
 * @author Piotr Mitka
 */
public class FileRevisionComparable implements Comparator<IFileRevision> {

    @Override
    public int compare(IFileRevision fr1, IFileRevision fr2) {
        if (fr1.getTimestamp() > fr2.getTimestamp()) {
            return 1;
        } else if (fr1.getTimestamp() < fr2.getTimestamp()) {
            return -1;
        } else {
            return 0;
        }
    }

}
