package xyz.ukrainskiys.discorddota.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import xyz.ukrainskiys.discorddota.model.PlayHistory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.util.List;

@Repository
public class PlayingHistoryRepository extends RepositoryBase<PlayHistory.DTO> {

  private static final Clock clock = Clock.systemUTC();

  protected PlayingHistoryRepository(JdbcTemplate jdbcTemplate) {
    super(new PlayHistoryRowMapper(), "PLAY_HISTORY", jdbcTemplate);
  }

  public List<PlayHistory.DTO> getLastHistoryList(Long discordServerId, Long count) {
    final String query = """
            SELECT PLAY_HISTORY.START_TIME,
                   PLAY_HISTORY.TRACK_URL,
                   (SELECT TITLE FROM TRACKS WHERE TRACKS.ID = PLAY_HISTORY.TRACK_ID) AS NAME
            FROM PLAY_HISTORY
            WHERE PLAY_HISTORY.DISCORD_SERVER_ID = ?
            ORDER BY PLAY_HISTORY.START_TIME DESC
            LIMIT ?
            """;
    return jdbcTemplate.query(query, mapper, discordServerId, count);
  }

  static class PlayHistoryRowMapper implements RowMapper<PlayHistory.DTO> {
    @Override
    public PlayHistory.DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
      return PlayHistory.DTO.builder()
              .name(rs.getString("NAME"))
              .startTime(rs.getTimestamp("START_TIME").toLocalDateTime())
              .url(rs.getString("TRACK_URL"))
              .build();
    }
  }
}
