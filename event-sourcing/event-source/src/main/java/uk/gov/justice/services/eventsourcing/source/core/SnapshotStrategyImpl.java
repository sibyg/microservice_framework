package uk.gov.justice.services.eventsourcing.source.core;

import java.util.UUID;

public class SnapshotStrategyImpl implements SnapshotStrategy {

    @Override
    public boolean createSnapshot() {
        return false;
    }
}
