package com.BlogProject.dao;

import com.BlogProject.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    /**
     * find all published blogs
     * @param pageable
     * @return a page object that contains all published blogs
     */

    @Query("SELECT b FROM Blog b WHERE b.published = true")
    Page<Blog> findPublished(Pageable pageable);

    /**
     * finds top n published blogs
     * @param pageable
     * @return a page object that contains top n published blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.recommend = true AND b.published = true")
    List<Blog> findTop(Pageable pageable);

    /**
     * finds all published blogs that contain String query in their title or content
     * @param query
     * @param pageable
     * @return a page object that contains all blogs that have String query in their title or content
     */
    @Query("SELECT b FROM Blog b WHERE (b.title LIKE ?1 OR b.content LIKE ?1) AND b.published = true")
    Page<Blog> findPublishedByQuery(String query, Pageable pageable);

    /**
     * find blog by id and update its views count by adding 1 to it
     * @param id
     */
    @Transactional
    @Modifying
    @Query("UPDATE Blog b SET b.view = b.view + 1 WHERE b.id = ?1")
    void updateView(Long id);

    /**
     * find distinct years and order in descending order
     * @return List of distinct years and order in descending order
     */
    @Query("SELECT DISTINCT YEAR(b.updateTime) AS year FROM Blog b ORDER BY year DESC")
    List<Integer> findDistinctYears();

    /**
     * find all blogs that are published and have createTime in input year
     * @param year
     * @return List of blogs that are published and have createTime in input year
     */
    @Query("SELECT b FROM Blog b WHERE YEAR(b.createTime) = ?1 AND b.published = true ORDER BY b.createTime DESC")
    List<Blog> findBlogsByYear(Integer year);

    /**
     *
     * @return count of all published blogs
     */
    @Query("SELECT COUNT(b) FROM Blog b WHERE b.published = true")
    Integer countPublishedBlogs();
}
