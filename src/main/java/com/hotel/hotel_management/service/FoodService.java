/**
 * 
 */
package com.hotel.hotel_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.hotel_management.entity.AssetEntity;
import com.hotel.hotel_management.entity.FoodEntity;

@Service
public interface FoodService {
	
	public FoodEntity addFood(FoodEntity foodEntity, AssetEntity asset);

	//public FoodEntity updateFood(FoodEntity foodEntity);

	public void deleteFood(String id);

	public List<FoodEntity> getFoodByFilters(String hotelName);

	public byte[] getFoodImage(String imageId);

	public FoodEntity updateFood(String hotelId, String foodName, String food_price, MultipartFile file);
	
}
