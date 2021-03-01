package com.luv2code.springboot.cruddemo.jpa;



import com.luv2code.springboot.cruddemo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileJpaRepo extends JpaRepository<Profile, Integer> {


}
