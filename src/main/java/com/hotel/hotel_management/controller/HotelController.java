package com.hotel.hotel_management.controller;

import java.util.List;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotel.hotel_management.entity.AssetEntity;
import com.hotel.hotel_management.entity.FoodEntity;
import com.hotel.hotel_management.entity.HotelEntity;
import com.hotel.hotel_management.service.FoodService;
import com.hotel.hotel_management.service.HotelService;

import io.swagger.annotations.ApiOperation;
import utils.Assetcreator;

import javax.rmi.CORBA.Util;

@RestController
@RequestMapping(value = "/hotels")
public class HotelController {
	
    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private FoodService foodService;

    public static final Logger LOG = LoggerFactory.getLogger(HotelController.class);
    ResponseEntity responseEntity = null;
    /*========================================Hotel Apis===========================================================*/
    
    //Add hotel
    @ApiOperation(value = "Add new Hotel")
    @RequestMapping(value = "/add_hotel", method = RequestMethod.POST, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error") })

    public ResponseEntity<HotelEntity> saveHotel(@RequestBody HotelEntity hotelEntity) {

        try {

            if(hotelEntity.getContact_number().equals("")){
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":" + "\"" + "contact number is empty" + "\"}");
                return responseEntity;
            } else if (hotelEntity.getHotel_name().equals("")){
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":" + "\"" + "hotel name is empty" + "\"}");
                return responseEntity;
            } else if (hotelEntity.getHotel_location().equals("")){
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":" + "\"" + "hotel location is empty" + "\"}");
                return responseEntity;
            }

        }catch ( Exception e){
            LOG.error("Exception >> Hotel controller >> add hotel"+ Util.wrapException(e));
        }

        return ResponseEntity.ok().body(hotelService.save(hotelEntity));
    }
    
    //Delete Hotel
    @ApiOperation(value = "Delete entire hotel and its stuffs")
    @DeleteMapping(path = "/delete_hotel/{hotelId}")
    public ResponseEntity<String> deleteHotel(@PathVariable("hotelId") String hotelId) {
        this.hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>("Deleted", HttpStatus.ACCEPTED);
    }
    
    //Edit or update hotel
    @ApiOperation(value = "Edit / update hotel detals")
    @PutMapping(value = "/update_hotel", produces = "application/json")
    public ResponseEntity<HotelEntity> updateHotel(@RequestBody HotelEntity hotelEntity){
		
    		return ResponseEntity.ok().body(hotelService.updateHotel(hotelEntity));
    	
    }
    
    //Search hotel by filters
    @ApiOperation(value = "Search for Hotel by hotel name or by location or by both")
    @GetMapping(value = "/search_hotel", produces="application/json")
	public ResponseEntity<List<HotelEntity>> getHotelsByFilters(
			@RequestParam(value = "hotelName", required = false) String hotelName,
			@RequestParam(value = "location", required = false) String location) {
		System.out.println("inside search : " + hotelName + " location " + location);
		return ResponseEntity.ok().body(hotelService.getHotelsByFilters(hotelName, location));

	}
    
    /*========================================Food Apis===========================================================*/
    
    //Add food to hotel using hotel_id
	@ApiOperation(value = "Add new food entry to an hotel")
	@PostMapping(value = "/add_food")
	public ResponseEntity<FoodEntity> addFood(@RequestParam("hotel_id") String hotelId,
			@RequestParam("food_name") String foodName, @RequestParam("food_price") String food_price,
			@RequestParam("file") MultipartFile file) {
		
		FoodEntity foodEntity = new FoodEntity();
		foodEntity.setFood_name(foodName);
		foodEntity.setFood_price(food_price);
		foodEntity.setHotel_id(hotelId);
		AssetEntity asset = Assetcreator.createAssetEntity(file);	
		foodEntity.setFood_image_id(String.valueOf(asset.getAsset_id()));
		
		return ResponseEntity.ok().body(foodService.addFood(foodEntity, asset));
	}
    
    //Edit or update food using food_id and hotel_id
    @ApiOperation(value = "Update food details")
    @PutMapping(value = "/update_food", produces = "application/json")
    public ResponseEntity<FoodEntity> updateHotel(@RequestParam("hotel_id") String hotelId, @RequestParam("food_name") String foodName, 
    		@RequestParam("food_price") String food_price, @RequestParam("file") MultipartFile file){
		
    		return ResponseEntity.ok().body(foodService.updateFood(hotelId, foodName, food_price, file));
    	
    }
    
    //Delete food 
    @ApiOperation(value = "Delete food entry in an hotel")
    @DeleteMapping(path = "/delete/food/{foodId}")
    public ResponseEntity<String> deleteFood(@PathVariable("foodId") String foodId) {
        this.foodService.deleteFood(foodId);
        return new ResponseEntity<>("Deleted", HttpStatus.ACCEPTED);
    }
    
  //Search hotel by filters
    @ApiOperation(value = "Search for foods by Hotel name")
    @GetMapping(value = "/search_food", produces="application/json")
	public ResponseEntity<List<FoodEntity>> getFoodByFilters(
			@RequestParam(value = "hotelName") String hotelName) {
		System.out.println("inside search : " + hotelName);
		return ResponseEntity.ok().body(foodService.getFoodByFilters(hotelName));

	}
    
    //accessing food image by image id
    @ApiOperation(value = "Access food image")
    @GetMapping("/foodImage/{imageId}")
    @ResponseBody
    public HttpEntity<byte[]> getImage(@PathVariable String imageId) {
    		byte[] imageByteArr = foodService.getFoodImage(imageId);
    		HttpHeaders headers = new HttpHeaders();
    		if(imageByteArr!=null) {	
        		headers.setContentType(MediaType.IMAGE_JPEG);
        		headers.setContentLength(imageByteArr.length);
    		}
        return new HttpEntity<byte[]>(imageByteArr, headers);
    }  
    
}
