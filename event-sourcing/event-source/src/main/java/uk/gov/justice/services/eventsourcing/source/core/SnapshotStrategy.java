package uk.gov.justice.services.eventsourcing.source.core;

public interface SnapshotStrategy {

    boolean createSnapshot();

}
