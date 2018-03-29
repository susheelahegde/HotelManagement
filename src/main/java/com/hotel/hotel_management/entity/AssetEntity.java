/**
 * 
 */
package com.hotel.hotel_management.entity;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("asset")
public class AssetEntity {
	
	@PrimaryKey()
	private String asset_id;
	private ByteBuffer content;
	private String file_name;
	
	public AssetEntity() {
		
	}

	/**
	 * @return the asset_id
	 */
	public String getAsset_id() {
		return asset_id;
	}

	/**
	 * @param asset_id the asset_id to set
	 */
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}


	/**
	 * @return the content
	 */
	public ByteBuffer getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(ByteBuffer content) {
		this.content = content;
	}

	/**
	 * @return the file_name
	 */
	public String getFile_name() {
		return file_name;
	}

	/**
	 * @param file_name the file_name to set
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssetEntity [asset_id=" + asset_id + ", content=" + content + ", file_name=" + file_name + "]";
	}
		
}
