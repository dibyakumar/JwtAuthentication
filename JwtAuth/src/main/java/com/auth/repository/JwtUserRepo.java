package com.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.auth.entity.JwtUser;

public interface JwtUserRepo extends MongoRepository<JwtUser, String>{

}
