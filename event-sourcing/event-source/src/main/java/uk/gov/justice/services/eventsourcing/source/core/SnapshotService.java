package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.common.exception.DuplicateSnapshotException;
import uk.gov.justice.services.eventsourcing.common.exception.InvalidSequenceIdException;
import uk.gov.justice.services.eventsourcing.common.snapshot.AggregateSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface SnapshotService {
    /**
     * Stores an aggregate snapshot.
     *
     * @param AggregateSnapshot aggregateSnapshot
     */
    void storeSnapshot(final AggregateSnapshot AggregateSnapshot)
            throws DuplicateSnapshotException, InvalidSequenceIdException;

    /**
     * Get an Optional Aggregate Snapshot.
     *
     * @param streamId the id of the stream to retrieve
     * @return the Optional<AggregateSnapshot>. Never returns null.
     */
    Optional<AggregateSnapshot> getLatestSnapshot(final UUID streamId);
}
