package xyz.ukrainskiys.discorddota.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServersRepository {

  private final JdbcTemplate jdbcTemplate;

  public Optional<Long> getVoiceChannelId(Long serverId) {
    final String query = "SELECT VOICE_CHANNEL_ID FROM DISCORD_SERVERS WHERE ID = ?";
    return Optional.ofNullable(jdbcTemplate.queryForObject(query, Long.TYPE, serverId));
  }

  public void saveServer(Long serverId, Long voiceChannelId) {
    final String query = "MERGE INTO DISCORD_SERVERS KEY (ID) VALUES ( ?,? )";
    jdbcTemplate.update(query, serverId, voiceChannelId);
  }

}
