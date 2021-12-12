package com.example.testsite.repo;

import org.springframework.data.repository.CrudRepository;

import com.example.testsite.models.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

}
