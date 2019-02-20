package com.CGI.springBoot.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
	List<Group> findAllByOrderById();
	List<Group> findAlldByOrderByIdDesc();
	Group findGroupById(Integer id);

	@Query("SELECT distinct g FROM Group g WHERE g.title = :title")
	Group findByTitleWithQuery(@Param("title") String title);
}
