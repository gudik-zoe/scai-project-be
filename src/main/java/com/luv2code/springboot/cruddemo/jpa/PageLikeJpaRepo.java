package com.luv2code.springboot.cruddemo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.PageLike;

@Repository
public interface PageLikeJpaRepo extends JpaRepository<PageLike, Integer> {

	@Query("from PageLike as p where p.relatedPageId =:pageId and p.pageLikeCreatorId=:accountId")
	public PageLike getIfPageLikedByUser(@Param(value = "accountId") int accountId,
			@Param(value = "pageId") int pageId);

	@Query("from PageLike as p where p.pageLikeCreatorId=:accountId")
	public List<PageLike> getMyLikedPages(@Param(value = "accountId") int accountId);

}
