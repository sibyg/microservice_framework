package uk.gov.justice.services.eventsourcing.source.core;

public class SnapshotStrategyContext {
    private SnapshotStrategy snapshotStrategy;

    public void setSnapshotStrategy(SnapshotStrategy strategy) {
        this.snapshotStrategy = strategy;
    }

    public boolean createSnapshot() {
       return snapshotStrategy.createSnapshot();
    }
}
