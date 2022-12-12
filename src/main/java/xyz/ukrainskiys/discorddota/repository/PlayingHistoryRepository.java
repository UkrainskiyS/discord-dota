package xyz.ukrainskiys.discorddota.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.ukrainskiys.discorddota.model.PlayHistory;
import xyz.ukrainskiys.discorddota.model.Track;

import java.time.Clock;
import java.time.LocalDateTime;

@Repository
public class PlayingHistoryRepository extends RepositoryBase<PlayHistory> {

  private static final Clock clock = Clock.systemUTC();

  protected PlayingHistoryRepository(JdbcTemplate jdbcTemplate) {
    super(null, "PLAY_HISTORY", jdbcTemplate);
  }

  public void updatePlayingHistory(String url, Long discordServerId) {
    updatePlayingHistory(null, url, discordServerId);
  }

  public void updatePlayingHistory(Track track, Long discordServerId) {
    updatePlayingHistory(track.getId(), track.getUrl(), discordServerId);
  }

  private void updatePlayingHistory(Long trackId, String url, Long discordServerId) {
    final String query = """
            INSERT INTO PLAY_HISTORY (TRACK_ID, TRACK_URL, START_TIME, DISCORD_SERVER_ID)
            VALUES ( ?,?,?,? )
            """;
    jdbcTemplate.update(query, trackId, url, LocalDateTime.now(clock), discordServerId);
  }
}
