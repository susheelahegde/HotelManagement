/**
 * 
 */
package com.hotel.hotel_management.serviceImpl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.hotel_management.entity.AssetEntity;
import com.hotel.hotel_management.entity.FoodEntity;
import com.hotel.hotel_management.entity.HotelEntity;
import com.hotel.hotel_management.repository.AssetRepository;
import com.hotel.hotel_management.repository.FoodRepository;
import com.hotel.hotel_management.repository.HotelRepository;
import com.hotel.hotel_management.service.FoodService;

import utils.Assetcreator;

/**
 * @author hv98
 *
 */
@Component
public class FoodServiceImpl implements FoodService{

	@Autowired
	private FoodRepository foodRepository;
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private AssetRepository assetRepo;
	
	@Override
	public FoodEntity addFood(FoodEntity foodEntity, AssetEntity asset) {
		if(foodEntity.getHotel_id() == null) {
			//Error needs to handle
			System.out.println("Hotel id is mandatory to add food");
			return null;
		}
		
		//setting food_id
		foodEntity.setFood_id(UUID.randomUUID().toString());
        
		if(this.hotelRepository.exists(foodEntity.getHotel_id())) {   //checking hotel existence
			//Listing all food in an hotel by hotel id and comparing new food names with existing list of foods
			List<FoodEntity> foodMenu = this.foodRepository.findFoodByHotel(foodEntity.getHotel_id());
			for (FoodEntity food : foodMenu) {
				if(food.getFood_name().equalsIgnoreCase(foodEntity.getFood_name())) {
					System.out.println("ohhhh!!!! Already food exists.");
					return null;
				}
			}
			
	        System.out.println("Food entity: "+foodEntity.toString());
	        System.out.println("Asset entity: "+asset.toString());
	        
	        this.assetRepo.save(asset);
	        System.out.println("image stored");
	        this.foodRepository.save(foodEntity);
		} else {
			//Error needs to handle
			System.out.println("Hotel not food");
			return null;
		}
		return foodEntity;
	}

	@Override
	public FoodEntity updateFood(String hotelId, String foodName, String food_price, MultipartFile file) {
		FoodEntity updatedFood = new FoodEntity();
		
		if (hotelId == null) {
			// Error needs to handle
			System.out.println("Hotel id is mandatory to update food details");
			return null;
		}
		try {
			if (this.hotelRepository.exists(hotelId)) { // checking hotel existence
				List<FoodEntity> foodMenu = this.foodRepository.findFoodByHotel(hotelId);
				for (FoodEntity food : foodMenu) {
					if (this.foodRepository.exists(food.getFood_id())) {
						System.out.println("Food exists.");
						AssetEntity asset = Assetcreator.createAssetEntity(file);
						updatedFood = this.foodRepository.update(foodName, food_price,
								hotelId, food.getFood_id(), asset.getAsset_id());
						this.assetRepo.save(asset);
						System.out.println("Food updated successfully");
					} else {
						System.out.println("Food not found");
						return null;
					}
				}
			} else {
				// Error needs to be handle
				System.out.println("Hotel not food");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error occured during updating food : "+e);
		}
		return updatedFood;
	}

	@Override
	public void deleteFood(String id) {
		FoodEntity foodEntity = this.foodRepository.findOne(id);
        if (foodEntity != null) {
        		System.out.println(this.foodRepository.findFoodByHotel(id));
            this.foodRepository.delete(id);
        }	
	}

	@Override
	public List<FoodEntity> getFoodByFilters(String hotelName) {
		List<FoodEntity> foodList = new ArrayList<FoodEntity>();
		List<HotelEntity> hotel = new ArrayList<HotelEntity>();
		try {
			if(hotelName != null) {
				hotel = this.hotelRepository.findHotelByHotelName(hotelName);
				System.out.println("Hotel ====== "+hotel);
				for (HotelEntity hotelEntity : hotel) {
					foodList.addAll(this.foodRepository.findHotelByfilters(hotelEntity.getId()));
					System.out.println("food List ; "+foodList);
				}
			} else {
				return null;
			}
			
		} catch (Exception e) {
			System.out.println("Error occured during search food by hotel name : "+e.getMessage());
		}
		return foodList;
	}

	@Override
	public byte[] getFoodImage(String imageId) {
		AssetEntity asset = this.assetRepo.findOne(imageId);
		ByteBuffer buf = asset.getContent();
		byte[] image = new byte[buf.remaining()];
		buf.get(image);
        //byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File([YOUR PATH] + File.separator + personId + ".png"))
		return image;
	}

}
