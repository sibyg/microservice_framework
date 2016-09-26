package uk.gov.justice.services.eventsourcing.repository.jdbc.snapshot;


import static java.lang.String.format;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.domain.snapshot.AggregateSnapshot;
import uk.gov.justice.services.eventsourcing.repository.core.SnapshotRepository;
import uk.gov.justice.services.jdbc.persistence.AbstractJdbcRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.slf4j.Logger;

/**
 * JDBC based repository for snapshot records.
 */
public class SnapshotJdbcRepository extends AbstractJdbcRepository implements SnapshotRepository {

    @Inject
    Logger logger;

    private static final String COL_STREAM_ID = "stream_id";
    private static final String COL_VERSION_ID = "version_id";
    private static final String COL_TYPE = "type";
    private static final String COL_AGGREGATE = "aggregate";

    private static final String SQL_FIND_LATEST_BY_STREAM_ID = "SELECT * FROM snapshot WHERE stream_id=? ORDER BY version_id DESC";
    private static final String SQL_INSERT_EVENT_LOG = "INSERT INTO snapshot (stream_id, version_id, type, aggregate ) VALUES(?, ?, ?, ?)";
    private static final String DELETE_ALL_SNAPSHOTS_FOR_STREAM_ID_AND_CLASS = "delete from snapshot where stream_id =? and  type=?";

    protected static final String READING_STREAM_EXCEPTION = "Exception while reading stream %s";
    private static final String JNDI_DS_EVENT_STORE_PATTERN = "java:/app/%s/DS.eventstore";

    @Override
    public void storeSnapshot(final AggregateSnapshot aggregateSnapshot) {

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT_EVENT_LOG)) {
            ps.setObject(1, aggregateSnapshot.getStreamId());
            ps.setLong(2, aggregateSnapshot.getVersionId());
            ps.setString(3, aggregateSnapshot.getType());
            ps.setBytes(4, aggregateSnapshot.getAggregateByteRepresentation());
            ps.executeUpdate();
        } catch (SQLException | NamingException e) {
            logger.error("Error while storing a snapshot for {} at version {}", aggregateSnapshot.getStreamId(), aggregateSnapshot.getVersionId(), e);
        }
    }

    @Override
    public <T extends Aggregate> Optional<AggregateSnapshot<T>> getLatestSnapshot(final UUID streamId, final Class<T> clazz) {

        try (final Connection connection = getDataSource().getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_LATEST_BY_STREAM_ID)) {

            preparedStatement.setObject(1, streamId);

            return extractResults(preparedStatement, clazz);

        } catch (SQLException | NamingException e) {
            logger.error(format(READING_STREAM_EXCEPTION, streamId), e);
        }
        return Optional.empty();
    }

    @Override
    public <T extends Aggregate> void removeAllSnapshots(final UUID streamId, final Class<T> clazz) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_ALL_SNAPSHOTS_FOR_STREAM_ID_AND_CLASS)) {
            ps.setObject(1, streamId);
            ps.setString(2, clazz.getName());
            ps.executeUpdate();
        } catch (SQLException | NamingException e) {
            logger.error(format("Exception while removing snapshots %s of stream %s", clazz, streamId), e);
        }
    }

    @Override
    protected String jndiName() throws NamingException {
        return format(JNDI_DS_EVENT_STORE_PATTERN, warFileName());
    }

    private <T extends Aggregate> Optional<AggregateSnapshot<T>> extractResults(final PreparedStatement preparedStatement, final Class<T> clazz) throws SQLException {

        try (final ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return createAggregateSnapshot(resultSet, clazz);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings({"unchecked", "unused"})
    private <T extends Aggregate> Optional<AggregateSnapshot<T>> createAggregateSnapshot(final ResultSet resultSet, final Class<T> clazz) throws
            SQLException {

        return Optional.of(new AggregateSnapshot<T>(
                (UUID) resultSet.getObject(COL_STREAM_ID),
                resultSet.getLong(COL_VERSION_ID),
                resultSet.getString(COL_TYPE),
                resultSet.getBytes(COL_AGGREGATE)));
    }
}
