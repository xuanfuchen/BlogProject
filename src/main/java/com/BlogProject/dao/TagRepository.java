package com.BlogProject.dao;

import com.BlogProject.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Query("SELECT t FROM Tag t")
    List<Tag> findTop(Pageable pageable);
}
