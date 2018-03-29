package utils;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.web.multipart.MultipartFile;

import com.hotel.hotel_management.entity.AssetEntity;

public class Assetcreator {
	
	public static AssetEntity createAssetEntity(MultipartFile file) {
		int assetId = (int) (System.currentTimeMillis() & 0xfffffff);
		AssetEntity asset = new AssetEntity();
		try {
			ByteBuffer content = ByteBuffer.wrap(file.getBytes());
			asset.setContent(content);
			asset.setFile_name(file.getOriginalFilename());
			asset.setAsset_id(String.valueOf(assetId));
		} catch (IOException e) {
			System.out.println("Unable to convert image file into bytestream : "+e);
			e.printStackTrace();
		}
		return asset;
	}
	
}
