package xyz.ukrainskiys.discorddota.repository;

import java.util.Optional;

public interface Repository<T> {

  Optional<T> findById(Long id);
}
