package com.luv2code.springboot.cruddemo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Page;

@Repository
public interface PageJpaRepo extends JpaRepository<Page, Integer> {

	@Query("from Page as p where p.pageCreatorId =:accountId")
	public List<Page> getMyPages(@Param(value = "accountId") int accountId);

}
