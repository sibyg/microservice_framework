package uk.gov.justice.services.eventsourcing.source.core;

import uk.gov.justice.services.eventsourcing.common.exception.DuplicateSnapshotException;
import uk.gov.justice.services.eventsourcing.common.exception.InvalidSequenceIdException;
import uk.gov.justice.services.eventsourcing.common.snapshot.AggregateSnapshot;
import uk.gov.justice.services.eventsourcing.repository.core.SnapshotRepository;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class SnapshotServiceTest {

    @InjectMocks
    SnapshotRepository snapshotRepository;

    @Mock
    SnapshotStrategy snapshotStrategy;

    @Test
    public void shouldCreateSnapshotIfNotAvailable() throws DuplicateSnapshotException, InvalidSequenceIdException {

    }

    @Test
    public void shouldNotCreateSnapshotIfOneAvailable() throws DuplicateSnapshotException, InvalidSequenceIdException {

    }

}
