package xyz.ukrainskiys.discorddota.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import xyz.ukrainskiys.discorddota.model.Track;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TracksRepository extends RepositoryBase<Track> {

  public TracksRepository(JdbcTemplate jdbcTemplate) {
    super(new TrackRowMapper(), "TRACKS", jdbcTemplate);
  }

  public List<Track> getAllByServerId(Long serverId) {
    final String query = "SELECT * FROM TRACKS WHERE DISCORD_SERVER_ID = ?";
    return jdbcTemplate.query(query, mapper, serverId);
  }

  static class TrackRowMapper implements RowMapper<Track> {
    @Override
    public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Track.builder()
              .id(rs.getLong("ID"))
              .title(rs.getString("TITLE"))
              .url(rs.getString("URL"))
              .discordServerId(rs.getLong("DISCORD_SERVER_ID"))
              .build();
    }
  }
}
