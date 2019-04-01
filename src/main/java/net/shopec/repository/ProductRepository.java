package net.shopec.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import net.shopec.entity.Product;

@Component
public interface ProductRepository extends ElasticsearchRepository<Product, Long> {

}
