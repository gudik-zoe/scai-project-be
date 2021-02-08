package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Account;

@Repository
public interface AccountJpaRepo extends JpaRepository<Account, Integer> {
	
	@Query("from Account as a where a.email =:email")
	public Account findByEmail(@Param(value="email")String email);
	
	@Query("from Account as a where a.idAccount !=:accountId")
	public List<Account> findPeopleYouMayKnow(@Param(value="accountId")int accountId);
	
	

}
