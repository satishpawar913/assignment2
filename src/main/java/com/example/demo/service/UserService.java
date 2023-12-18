package com.example.demo.service;

import java.util.List;
import java.util.Map;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

	public Mono<List<Map<String, Object>>> getAllPosts();

	Mono<Map<String, Object>> getPostById(Integer postId);

	Mono<Map<String, Object>> savePost(Map<String, Object> post);

	Mono<Map<String, Object>> updatePost(Integer postId, Map<String, Object> updatedPost);

	Mono<Void> deletePost(Integer postId);

}
