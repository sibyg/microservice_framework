package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.common.exception.DuplicateSnapshotException;
import uk.gov.justice.services.eventsourcing.common.exception.InvalidSequenceIdException;
import uk.gov.justice.services.eventsourcing.common.snapshot.AggregateSnapshot;
import uk.gov.justice.services.eventsourcing.repository.core.SnapshotRepository;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

public class SnapshotServiceImpl implements SnapshotService {

    @Inject
    SnapshotRepository snapshotRepository;

    SnapshotStrategy snapshotStrategy;

    SnapshotStrategyContext snapshotStrategyContext;

    private SnapshotServiceImpl(final SnapshotStrategyContext snapshotStrategyContext, final SnapshotStrategy snapshotStrategy) {
        this.snapshotStrategyContext = snapshotStrategyContext;
        this.snapshotStrategy = snapshotStrategy;

        snapshotStrategyContext.setSnapshotStrategy(snapshotStrategy);
    }

    public static SnapshotService createSnapshotServiceWith(final SnapshotStrategyContext snapshotStrategyContext, final SnapshotStrategy snapshotStrategy) {
        return new SnapshotServiceImpl(snapshotStrategyContext, snapshotStrategy);
    }

    @Override
    public void storeSnapshot(AggregateSnapshot aggregateSnapshot) throws DuplicateSnapshotException, InvalidSequenceIdException {
        if (snapshotStrategy.createSnapshot()) {
            snapshotRepository.storeSnapshot(aggregateSnapshot);
        }
    }

    @Override
    public Optional<AggregateSnapshot> getLatestSnapshot(UUID streamId) {
        return snapshotRepository.getLatestSnapshot(streamId);
    }
}
