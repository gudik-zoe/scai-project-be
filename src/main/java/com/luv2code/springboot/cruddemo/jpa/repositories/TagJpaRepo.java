package com.luv2code.springboot.cruddemo.jpa.repositories;

import com.luv2code.springboot.cruddemo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepo extends JpaRepository<Tag, Integer> {

}
