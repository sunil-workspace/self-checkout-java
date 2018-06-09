package com.selfcheckout.repo.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.selfcheckout.dto.Item;
import com.selfcheckout.dto.ItemSelected;
import com.selfcheckout.repo.ItemRepository;

@Repository
public class ItemRepositoryImpl implements ItemRepository{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * This method will take itemName and gets all the Item details
	 */
	@Override
	public List<Item> getItemDetails(final String itemName) {

		List<Item> itemDetails = jdbcTemplate.query("SELECT NAME, PRICE, WEIGHT FROM items_price WHERE NAME = '" + itemName +"'", (rs, rowNum) -> 
		new Item(rs.getString("NAME"), rs.getDouble("PRICE"), rs.getDouble("WEIGHT")));
		
		
		return itemDetails;	
	}

	@Override
	public boolean  postItemDetails(final List<ItemSelected> itemsSelectedList) {
		boolean isInserted = false;
		int response[]  = {};
		try {
			String insertSql = "INSERT INTO ITEMS_SELECTED (NAME, PRICE, QUANTITY, WEIGHT, PURCHASED_DATE) VALUES (?, ?, ?, ?, ?)";

			response  = jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ItemSelected itemSelected = itemsSelectedList.get(i);
					ps.setString(1, itemSelected.getNAME());
					ps.setDouble(2, itemSelected.getPRICE());
					ps.setInt(3, itemSelected.getQUANTITY());
					ps.setDouble(4, itemSelected.getWEIGHT());
					ps.setDate(5, itemSelected.getPURCHASED_DATE());
				}

				@Override
				public int getBatchSize() {
					return itemsSelectedList.size();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}

		if(itemsSelectedList.size() == response.length)
			isInserted = true;

		return isInserted;
	}

}
