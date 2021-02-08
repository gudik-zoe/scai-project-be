//package com.luv2code.springboot.cruddemo.dao;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.persistence.EntityManager;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import com.luv2code.exception.error.handling.CustomeException;
//import com.luv2code.springboot.cruddemo.entity.Page;
//import com.luv2code.springboot.cruddemo.entity.PageLike;
//import com.luv2code.springboot.cruddemo.entity.Post;
//import com.luv2code.springboot.cruddemo.service.StorageService;
//import com.luv2code.utility.ImageUrl;
//import com.luv2code.utility.PageBasicData;
//import com.luv2code.utility.UpdatePage;
//
//@Repository
//public class PageDAOHibernateImpl implements PageDAO {
//
//	private EntityManager entityManager;
//
//	private StorageService storageService;
//
//	@Autowired
//	public PageDAOHibernateImpl(EntityManager theEntityManager, StorageService theStorageService) {
//		entityManager = theEntityManager;
//		storageService = theStorageService;
//	}
//
//	@Override
//	public List<PageBasicData> getPages() {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Page> theQuery = currentSession.createQuery("from Page", Page.class);
//		List<Page> pages = theQuery.getResultList();
//		List<PageBasicData> thePagesBasicData = new ArrayList<PageBasicData>();
//		for (Page page : pages) {
//			PageBasicData thePage = new PageBasicData(page.getIdPage(), page.getName(), page.getProfilePhoto(),
//					page.getCoverPhoto(), page.getPageCreatorId(), page.getPageLike());
//			thePagesBasicData.add(thePage);
//		}
//		return thePagesBasicData;
//	}
//
//	@Override
//	public PageLike likePage(int accountId, int pageId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Page theLikedPage = currentSession.get(Page.class, pageId);
//		if (theLikedPage == null) {
//			throw new CustomeException("there is no such a page");
//		}
//		Query<PageLike> theQuery = currentSession.createQuery(
//				"from PageLike where related_page_id=" + pageId + "and page_like_creator_id = " + accountId,
//				PageLike.class);
//		try {
//			PageLike theLike = theQuery.getSingleResult();
//			currentSession.delete(theLike);
//			return null;
//		} catch (Exception e) {
//			PageLike theNewLike = new PageLike();
//			if (theLikedPage.getPageCreatorId() != accountId) {
//				theNewLike.setPageLikeCreatorId(accountId);
//				theNewLike.setRelatedPageId(pageId);
//				currentSession.save(theNewLike);
//				return theNewLike;
//			} else {
//				throw new CustomeException("you can't like your own page");
//			}
//		}
//	}
//
//	@Override
//	public List<Integer> getMyLikedPages(int accountId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<PageLike> theQuery = currentSession.createQuery("from PageLike where page_like_creator_id = " + accountId,
//				PageLike.class);
//		List<PageLike> pageLikes = theQuery.getResultList();
//		List<Integer> thePagesIds = new ArrayList<Integer>();
//		for (PageLike page : pageLikes) {
//			thePagesIds.add(page.getRelatedPageId());
//		}
//		return thePagesIds;
//	}
//
//	@Override
//	public PageBasicData getPageById(int pageId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Page thePage = currentSession.get(Page.class, pageId);
//		if (thePage == null) {
//			throw new CustomeException("this page doesn't exist");
//		}
//		PageBasicData pageBasicData = new PageBasicData(thePage.getIdPage(), thePage.getName(),
//				thePage.getProfilePhoto(), thePage.getCoverPhoto(), thePage.getPageCreatorId(), thePage.getPageLike());
//		return pageBasicData;
//	}
//
//	@Override
//	public Page createPage(Page page) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		currentSession.save(page);
//		return page;
//	}
//
//	@Override
//	public List<PageBasicData> getMyPages(int accountId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Page> theQuery = currentSession.createQuery("from Page where page_creator_id = " + accountId, Page.class);
//		if (theQuery.getResultList() != null) {
//			List<Page> pages = theQuery.getResultList();
//			List<PageBasicData> myPages = new ArrayList<PageBasicData>();
//			for (Page page : pages) {
//				myPages.add(new PageBasicData(page.getIdPage(), page.getName(), page.getProfilePhoto(),
//						page.getCoverPhoto(), page.getPageCreatorId(), page.getPageLike()));
//			}
//			return myPages;
//		} else {
//			return null;
//		}
//
//	}
//
//	@Override
//	public List<Post> getPagePosts(int pageId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Post> theQuery = currentSession
//				.createQuery("from Post where page_creator_id = " + pageId + "order by date desc", Post.class);
//		if (theQuery.getResultList() != null) {
//			return theQuery.getResultList();
//		} else {
//			return null;
//		}
//	}
//
//	@Override
//	public Post addPost(int accountId, int pageId, Post thePost) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		try {
//			Query<Page> theQuery = currentSession.createQuery(
//					"from Page where id_page = " + pageId + " and page_creator_id = " + accountId, Page.class);
//			if (theQuery != null) {
//				thePost.setPageCreatorId(pageId);
//				thePost.setStatus(0);
//				thePost.setDate(new Date(System.currentTimeMillis()));
//				currentSession.save(thePost);
//			}
//		} catch (Exception e) {
//			throw new CustomeException("cannot add a post to an unexisting page or the page is not yours");
//		}
//		return thePost;
//	}
//
//	@Override
//	public Page getPageFullData(int pageId, int accountId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Page theRequestedPage = currentSession.get(Page.class, pageId);
//		if (theRequestedPage != null) {
//			return theRequestedPage;
//		} else {
//			throw new CustomeException("there is no such page");
//		}
//	}
//
//	@Override
//	public Page updatePage(UpdatePage theNewPage, Page theOldPage) throws Exception {
//		Session currentSession = entityManager.unwrap(Session.class);
//		if (theNewPage.getProfilePhoto() != null) {
//			ImageUrl profilePhoto = storageService.pushImage(theNewPage.getProfilePhoto());
//			theOldPage.setProfilePhoto(profilePhoto.getImageUrl());
//		}
//		if (theNewPage.getCoverPhoto() != null) {
//			ImageUrl coverPhoto = storageService.pushImage(theNewPage.getCoverPhoto());
//			theOldPage.setCoverPhoto(coverPhoto.getImageUrl());
//		}
//
//		theOldPage.setName(theNewPage.getName());
//		theOldPage.setDescription(theNewPage.getDescription());
//		currentSession.update(theOldPage);
//		return theOldPage;
//	}
//
//	@Override
//	public List<String> getPagePhotos(int pageId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Post> theQuery = currentSession.createQuery("from Post where page_creator_id=" + pageId, Post.class);
//		List<Post> posts = theQuery.getResultList();
//		List<String> photos = new ArrayList<String>();
//		Page thePage = currentSession.get(Page.class, pageId);
//		for (Post post : posts) {
//			if (post.getImage() != null && !photos.contains(post.getImage())) {
//				photos.add(post.getImage());
//			}
//		}
//
//		photos.add(thePage.getProfilePhoto());
//		photos.add(thePage.getCoverPhoto());
//
//		return photos;
//	}
//
//}
