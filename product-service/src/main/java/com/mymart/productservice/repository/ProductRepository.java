package com.mymart.productservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.mymart.productservice.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {}
