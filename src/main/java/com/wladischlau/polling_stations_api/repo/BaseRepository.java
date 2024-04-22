package com.wladischlau.polling_stations_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    // TODO:
    default T getExisted(ID id) {
        return findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Entity with id" + id + " not found."
                )
        );
    }
}
