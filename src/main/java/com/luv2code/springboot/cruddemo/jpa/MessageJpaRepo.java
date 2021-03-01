package com.luv2code.springboot.cruddemo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Message;

@Repository
public interface MessageJpaRepo extends JpaRepository<Message, Integer> {

	@Query("from Message as m where m.idReceiver=:accountId and m.seen = false")
	public List<Message> getMyUnseenMessages(@Param(value = "accountId") int accountId);

	@Query("from Message as m where m.idReceiver=:accountId and m.idSender =:senderId or m.idReceiver=:senderId and m.idSender=:accountId")
	public List<Message> getConversation(@Param(value = "accountId") int accountId,
			@Param(value = "senderId") int senderId);
	
	@Query("from Message as m where m.idReceiver =:accountId and m.idSender =:account2Id and m.seen = false")
	public List<Message> getunSeenMessagesFromUser(@Param(value="accountId") int accountId,@Param(value="account2Id") int account2Id);

}
