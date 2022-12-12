package xyz.ukrainskiys.discorddota.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.ukrainskiys.discorddota.model.DiscordServer;

import java.util.Objects;
import java.util.Optional;

@Repository
public class ServersRepository extends RepositoryBase<DiscordServer> {

  protected ServersRepository(JdbcTemplate jdbcTemplate) {
    super(null, "DISCORD_SERVERS", jdbcTemplate);
  }

  @Transactional
  public Optional<Long> getVoiceChannelId(Long serverId) {
    String query = "SELECT COUNT(*) FROM DISCORD_SERVERS WHERE ID = ?";
    final Long count = Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, serverId));
    if (count > 0) {
      return Optional.ofNullable(jdbcTemplate.queryForObject(query, Long.TYPE, serverId));
    }
    return Optional.empty();
  }

  public void saveServer(Long serverId, Long voiceChannelId) {
    final String query = "MERGE INTO DISCORD_SERVERS KEY (ID) VALUES ( ?,? )";
    jdbcTemplate.update(query, serverId, voiceChannelId);
  }

}
