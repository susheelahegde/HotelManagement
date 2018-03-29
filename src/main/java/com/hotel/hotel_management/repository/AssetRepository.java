/**
 * 
 */
package com.hotel.hotel_management.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hotel.hotel_management.entity.AssetEntity;

public interface AssetRepository extends CrudRepository<AssetEntity, String>  {
	
	@Query(value = "Insert into foodpoc.asset(asset_id, content, file_name) values (?0, ?1, ?2)")
	AssetEntity storeImg(String asset_id, byte[] content, String file_name);
	
}
