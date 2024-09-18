package com.fashionstore.fashion_store_backend.repository;

import com.fashionstore.fashion_store_backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "image", path = "image")
public interface ImageRepository extends JpaRepository<Image, Long> {
}