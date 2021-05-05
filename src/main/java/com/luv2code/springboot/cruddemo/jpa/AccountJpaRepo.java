package com.luv2code.springboot.cruddemo.jpa;


import com.luv2code.springboot.cruddemo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountJpaRepo extends JpaRepository<Account, Integer> {
	
	@Query("from Account as a where a.email =:email")
	public Account findByEmail(@Param(value="email")String email);
	
	@Query("from Account as a where a.idAccount !=:accountId")
	public List<Account> findPeopleYouMayKnow(@Param(value="accountId")int accountId);

	@Query("from Account as a where a.temporaryPassword =:password")
	public Account getAccountByTempPassword(@Param(value="password")String password);

	//da aggiustare
//	@Query("SELECT a FROM Account as a join Relationship as r on r.userOneId=:accountId and r.userTwoId !=:accountId and r.status = 1")
//	public List<Account> findPeopleYouMayKnow2(@Param(value="accountId")int accountId);
	

}
