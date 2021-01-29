package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.PageDAO;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.utility.PageBasicData;
import com.luv2code.utility.UpdatePage;

@Service
@Transactional
public class PageServiceImpl implements PageService {

	private PageDAO pageDAO;

	private EntityManager entityManager;

	@Autowired
	public PageServiceImpl(PageDAO thePageDAO, EntityManager theEntityManager) {
		pageDAO = thePageDAO;
		entityManager = theEntityManager;
	}

	@Override
	public List<PageBasicData> getPages() {
		return pageDAO.getPages();
	}

	@Override
	public PageLike likePage(int accountId, int pageId) {
		return pageDAO.likePage(accountId, pageId);
	}

	@Override
	public List<Integer> getMyLikedPages(int accountId) {
		return pageDAO.getMyLikedPages(accountId);
	}

	@Override
	public PageBasicData getPageById(int pageId) {
		return pageDAO.getPageById(pageId);
	}

	@Override
	public Page createPage(Page page) {
		return pageDAO.createPage(page);
	}

	@Override
	public List<Page> getMyPages(int accountId) {
		return pageDAO.getMyPages(accountId);
	}

	@Override
	public List<Post> getPagePosts(int pageId) {
		return pageDAO.getPagePosts(pageId);
	}

	@Override
	public Post addPost(int accountId, int pageId, Post thePost) {
		return pageDAO.addPost(accountId, pageId, thePost);
	}

	@Override
	public Page getPageFullData(int pageId, int accountId) {
		return pageDAO.getPageFullData(pageId, accountId);
	}

	@Override
	public boolean checkIfPageLikedByAccount(int accountId, int pageId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Page thePage = currentSession.get(Page.class, pageId);
		for (PageLike like : thePage.getPageLike()) {
			if (like.getPageLikeCreatorId() == accountId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Page updatePage(UpdatePage theNewPage, Page theOldPage) throws Exception {
		return pageDAO.updatePage(theNewPage, theOldPage);
	}

	@Override
	public List<String> getPagePhotos(int pageId) {
		return pageDAO.getPagePhotos(pageId);
	}

}
