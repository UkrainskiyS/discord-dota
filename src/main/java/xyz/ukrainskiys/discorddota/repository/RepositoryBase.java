package xyz.ukrainskiys.discorddota.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class RepositoryBase<T> implements Repository<T> {

  private final String tableName;

  final RowMapper<T> mapper;
  final JdbcTemplate jdbcTemplate;

  protected RepositoryBase(RowMapper<T> mapper, String tableName, JdbcTemplate jdbcTemplate) {
    this.mapper = mapper;
    this.tableName = tableName;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<T> findById(Long id) {
    final String query = "SELECT * FROM " + tableName + " WHERE ID = ?";
    final List<T> resultList = jdbcTemplate.query(query, mapper, id);
    return resultList.size() == 0 ? Optional.empty() : Optional.ofNullable(resultList.get(0));
  }
}
