package net.shopec.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import net.shopec.entity.Store;

@Component
public interface StoreRepository extends ElasticsearchRepository<Store, Long> {

}
