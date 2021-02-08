package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.luv2code.springboot.cruddemo.entity.Post;

@Repository
public interface PostJpaRepo extends JpaRepository<Post, Integer> {

	@Query("from Post as p where p.postCreatorId =:accountId and p.image != null")
	public List<Post> getPhotos(@Param(value = "accountId") int accountId);

	@Query("from Post as p where p.postCreatorId =:accountId and p.status != 2 and p.postedOn = null order by date desc ")
	public List<Post> getMyPosts(@Param(value = "accountId") int accountId);
	
	@Query("from Post as p where p.postCreatorId=:accountId or p.postedOn =:accountId  order by date DESC")
	public List<Post> getMyPostsAndPostedOnMyWAll(@Param(value = "accountId") int accountId);

	@Query("from Post as p where p.postCreatorId =:friendId and p.status != 2  order by date desc")
	public List<Post> getFriendsPosts(@Param(value = "friendId") int friendId);

	@Query("from Post as p where p.pageCreatorId =:pageId order by date desc")
	public List<Post> getPagesPosts(@Param(value = "pageId") int pageId);
	
	@Query("from Post  as p where p.postCreatorId =:accountId and status = 0  order by date DESC")
	public List<Post> getPostsOfNonFriend(@Param(value = "accountId") int accountId);
	
	@Query("from Post  as p where p.postCreatorId =:accountId and status != 2 or p.postedOn =:accountId order by date DESC")
	public List<Post> getPostsOfAFriend(@Param(value = "accountId") int accountId);

}
