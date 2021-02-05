package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Post;

@Repository
public interface PostRepoJpa extends JpaRepository<Account, Integer> {

	@Query("from Post as p where p.postCreatorId =:accountId and p.image != null")
	public List<Post> getPhotos(@Param(value = "accountId") int accountId);

	@Query("from Post as p where p.postCreatorId =:accountId and p.status != 2 order by date desc")
	public List<Post> getMyPosts(@Param(value = "accountId") int accountId);

	@Query("from Post as p where p.postCreatorId =:friendId and p.status != 2  order by date desc")
	public List<Post> getFriendsPosts(@Param(value = "friendId") int friendId);

	@Query("from Post as p where p.pageCreatorId =:pageId order by date desc")
	public List<Post> getPagesPosts(@Param(value = "pageId") int pageId);

}
