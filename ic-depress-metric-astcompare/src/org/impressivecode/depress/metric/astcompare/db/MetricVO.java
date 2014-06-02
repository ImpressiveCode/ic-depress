package org.impressivecode.depress.metric.astcompare.db;

/**
 * @author Piotr Mitka
 */
public class MetricVO {

    private String methodName;
    private double allMethodHistories;
    private double methodHistories;
    private double authors;
    private double stmtAdded;
    private double maxStmtAdded;
    private double avgStmtAdded;
    private double stmtUpdated;
    private double maxStmtUpdated;
    private double avgStmtUpdated;
    private double stmtDeleted;
    private double maxStmtDeleted;
    private double avgStmtDeleted;
    private double stmtParentChanged;
    private double churn;
    private double maxChurn;
    private double avgChurn;
    private double decl;
    private double cond;
    private double elseAdded;
    private double elseDeleted;
    private double bugCount;

    public static final int ALL_METHOD_HISTORIES = 0;
    public static final int METHOD_HISTORIES = 1;
    public static final int AUTHORS = 2;
    public static final int STMT_ADDED = 3;
    public static final int MAX_STMT_ADDED = 4;
    public static final int AVG_STMT_ADDED = 5;
    public static final int STMT_UPDATED = 6;
    public static final int MAX_STMT_UPDATED = 7;
    public static final int AVG_STMT_UPDATED = 8;
    public static final int STMT_DELETED = 9;
    public static final int MAX_STMT_DELETED = 10;
    public static final int AVG_STMT_DELETED = 11;
    public static final int STATEMENT_PARENT_CHANGE = 12;
    public static final int CHURN = 13;
    public static final int MAX_CHURN = 14;
    public static final int AVG_CHURN = 15;
    public static final int DECL = 16;
    public static final int COND = 17;
    public static final int ELSE_ADDED = 18;
    public static final int ELSE_DELETED = 19;
    public static final int BUG_COUNT = 20;

    public void setValue(String methodName, int metricName, double value) {
        setMethodName(methodName);
        setValue(metricName, value);
    }

    public void setValue(int metricName, double value) {
        switch (metricName) {
        case ALL_METHOD_HISTORIES:
            setAllMethodHistories(value);
            break;
        case METHOD_HISTORIES:
            setMethodHistories(value);
            break;
        case AUTHORS:
            setAuthors(value);
            break;
        case STMT_ADDED:
            setStmtAdded(value);
            break;
        case MAX_STMT_ADDED:
            setMaxStmtAdded(value);
            break;
        case AVG_STMT_ADDED:
            setAvgStmtAdded(value);
            break;
        case STMT_UPDATED:
            setStmtUpdated(value);
            break;
        case MAX_STMT_UPDATED:
            setMaxStmtUpdated(value);
            break;
        case AVG_STMT_UPDATED:
            setAvgStmtUpdated(value);
            break;
        case STMT_DELETED:
            setStmtDeleted(value);
            break;
        case MAX_STMT_DELETED:
            setMaxStmtDeleted(value);
            break;
        case AVG_STMT_DELETED:
            setAvgStmtDeleted(value);
            break;
        case STATEMENT_PARENT_CHANGE:
            setStmtParentChanged(value);
            break;
        case CHURN:
            setChurn(value);
            break;
        case MAX_CHURN:
            setMaxChurn(value);
            break;
        case AVG_CHURN:
            setAvgChurn(value);
            break;
        case DECL:
            setDecl(value);
            break;
        case COND:
            setCond(value);
            break;
        case ELSE_ADDED:
            setElseAdded(value);
            break;
        case ELSE_DELETED:
            setElseDeleted(value);
            break;
        case BUG_COUNT:
            setBugCount(value);
            break;
        }
    }

    public boolean hasResults() {
        return allMethodHistories != 0.00d && methodHistories != 0.00d && authors != 0.00d && stmtAdded != 0.00d
                && maxStmtAdded != 0.00d && avgStmtAdded != 0.00d && stmtUpdated != 0.00d && maxStmtUpdated != 0.00d
                && avgStmtUpdated != 0.00d && stmtDeleted != 0.00d && maxStmtDeleted != 0.00d
                && avgStmtDeleted != 0.00d && stmtParentChanged != 0.00d && churn != 0.00d && maxChurn != 0.00d
                && avgChurn != 0.00d && decl != 0.00d && cond != 0.00d && elseAdded != 0.00d && elseDeleted != 0.00d
                && bugCount != 0.00d;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(allMethodHistories);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(authors);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(avgChurn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(avgStmtAdded);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(avgStmtDeleted);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(avgStmtUpdated);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bugCount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(churn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cond);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(decl);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(elseAdded);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(elseDeleted);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxChurn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxStmtAdded);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxStmtDeleted);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxStmtUpdated);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(methodHistories);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        temp = Double.doubleToLongBits(stmtAdded);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stmtDeleted);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stmtParentChanged);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stmtUpdated);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetricVO other = (MetricVO) obj;
        if (Double.doubleToLongBits(allMethodHistories) != Double.doubleToLongBits(other.allMethodHistories))
            return false;
        if (Double.doubleToLongBits(authors) != Double.doubleToLongBits(other.authors))
            return false;
        if (Double.doubleToLongBits(avgChurn) != Double.doubleToLongBits(other.avgChurn))
            return false;
        if (Double.doubleToLongBits(avgStmtAdded) != Double.doubleToLongBits(other.avgStmtAdded))
            return false;
        if (Double.doubleToLongBits(avgStmtDeleted) != Double.doubleToLongBits(other.avgStmtDeleted))
            return false;
        if (Double.doubleToLongBits(avgStmtUpdated) != Double.doubleToLongBits(other.avgStmtUpdated))
            return false;
        if (Double.doubleToLongBits(bugCount) != Double.doubleToLongBits(other.bugCount))
            return false;
        if (Double.doubleToLongBits(churn) != Double.doubleToLongBits(other.churn))
            return false;
        if (Double.doubleToLongBits(cond) != Double.doubleToLongBits(other.cond))
            return false;
        if (Double.doubleToLongBits(decl) != Double.doubleToLongBits(other.decl))
            return false;
        if (Double.doubleToLongBits(elseAdded) != Double.doubleToLongBits(other.elseAdded))
            return false;
        if (Double.doubleToLongBits(elseDeleted) != Double.doubleToLongBits(other.elseDeleted))
            return false;
        if (Double.doubleToLongBits(maxChurn) != Double.doubleToLongBits(other.maxChurn))
            return false;
        if (Double.doubleToLongBits(maxStmtAdded) != Double.doubleToLongBits(other.maxStmtAdded))
            return false;
        if (Double.doubleToLongBits(maxStmtDeleted) != Double.doubleToLongBits(other.maxStmtDeleted))
            return false;
        if (Double.doubleToLongBits(maxStmtUpdated) != Double.doubleToLongBits(other.maxStmtUpdated))
            return false;
        if (Double.doubleToLongBits(methodHistories) != Double.doubleToLongBits(other.methodHistories))
            return false;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (Double.doubleToLongBits(stmtAdded) != Double.doubleToLongBits(other.stmtAdded))
            return false;
        if (Double.doubleToLongBits(stmtDeleted) != Double.doubleToLongBits(other.stmtDeleted))
            return false;
        if (Double.doubleToLongBits(stmtParentChanged) != Double.doubleToLongBits(other.stmtParentChanged))
            return false;
        if (Double.doubleToLongBits(stmtUpdated) != Double.doubleToLongBits(other.stmtUpdated))
            return false;
        return true;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public double getAllMethodHistories() {
        return allMethodHistories;
    }

    public void setAllMethodHistories(double allMethodHistories) {
        this.allMethodHistories = allMethodHistories;
    }

    public double getMethodHistories() {
        return methodHistories;
    }

    public void setMethodHistories(double methodHistories) {
        this.methodHistories = methodHistories;
    }

    public double getAuthors() {
        return authors;
    }

    public void setAuthors(double authors) {
        this.authors = authors;
    }

    public double getStmtAdded() {
        return stmtAdded;
    }

    public void setStmtAdded(double stmtAdded) {
        this.stmtAdded = stmtAdded;
    }

    public double getMaxStmtAdded() {
        return maxStmtAdded;
    }

    public void setMaxStmtAdded(double maxStmtAdded) {
        this.maxStmtAdded = maxStmtAdded;
    }

    public double getAvgStmtAdded() {
        return avgStmtAdded;
    }

    public void setAvgStmtAdded(double avgStmtAdded) {
        this.avgStmtAdded = avgStmtAdded;
    }

    public double getStmtUpdated() {
        return stmtUpdated;
    }

    public void setStmtUpdated(double stmtUpdated) {
        this.stmtUpdated = stmtUpdated;
    }

    public double getMaxStmtUpdated() {
        return maxStmtUpdated;
    }

    public void setMaxStmtUpdated(double maxStmtUpdated) {
        this.maxStmtUpdated = maxStmtUpdated;
    }

    public double getAvgStmtUpdated() {
        return avgStmtUpdated;
    }

    public void setAvgStmtUpdated(double avgStmtUpdated) {
        this.avgStmtUpdated = avgStmtUpdated;
    }

    public double getStmtDeleted() {
        return stmtDeleted;
    }

    public void setStmtDeleted(double stmtDeleted) {
        this.stmtDeleted = stmtDeleted;
    }

    public double getMaxStmtDeleted() {
        return maxStmtDeleted;
    }

    public void setMaxStmtDeleted(double maxStmtDeleted) {
        this.maxStmtDeleted = maxStmtDeleted;
    }

    public double getAvgStmtDeleted() {
        return avgStmtDeleted;
    }

    public void setAvgStmtDeleted(double avgStmtDeleted) {
        this.avgStmtDeleted = avgStmtDeleted;
    }

    public double getStmtParentChanged() {
        return stmtParentChanged;
    }

    public void setStmtParentChanged(double stmtParentChanged) {
        this.stmtParentChanged = stmtParentChanged;
    }

    public double getChurn() {
        return churn;
    }

    public void setChurn(double churn) {
        this.churn = churn;
    }

    public double getMaxChurn() {
        return maxChurn;
    }

    public void setMaxChurn(double maxChurn) {
        this.maxChurn = maxChurn;
    }

    public double getAvgChurn() {
        return avgChurn;
    }

    public void setAvgChurn(double avgChurn) {
        this.avgChurn = avgChurn;
    }

    public double getDecl() {
        return decl;
    }

    public void setDecl(double decl) {
        this.decl = decl;
    }

    public double getCond() {
        return cond;
    }

    public void setCond(double cond) {
        this.cond = cond;
    }

    public double getElseAdded() {
        return elseAdded;
    }

    public void setElseAdded(double elseAdded) {
        this.elseAdded = elseAdded;
    }

    public double getElseDeleted() {
        return elseDeleted;
    }

    public void setElseDeleted(double elseDeleted) {
        this.elseDeleted = elseDeleted;
    }

    public double getBugCount() {
        return bugCount;
    }

    public void setBugCount(double bugCount) {
        this.bugCount = bugCount;
    }

}
