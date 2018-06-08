package com.selfcheckout.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selfcheckout.dto.Item;
import com.selfcheckout.dto.ItemSelected;
import com.selfcheckout.exceptions.SelfCheckoutException;
import com.selfcheckout.repo.ItemRepository;
import com.selfcheckout.service.ItemDetailsService;

@Service
public class ItemDetailsServiceImpl implements ItemDetailsService {

	@Autowired
	ItemRepository itemRepository;

	/*@Override
	public Item getItemDetailsFromDB(String itemName) {

		List<Item> items = itemRepository.getItemDetails(itemName);

		for(Item item : items) {
			if(item.getITEM_NAME().equals(itemName)) {
				return item;
			}
		}
		return null;
	}*/

	@Override
	public JSONObject getItemDetailsFromDB(String itemName) {
		JSONObject jsonObj = new JSONObject();
		try {
			List<Item> items = itemRepository.getItemDetails(itemName);

			if(items.size()>0) {
				Item item = items.get(0);

				jsonObj.put("NAME", item.getNAME());
				jsonObj.put("PRICE", item.getPRICE());
				jsonObj.put("QUANTITY", 1);
			}
		} catch(SelfCheckoutException | JSONException e) {
			e.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return jsonObj;
	}

	@Override
	public JSONObject postItemDetailsToDB(String itemsDetails) {
		JSONObject responseJsonObj = new JSONObject();
		boolean isInserted = false;
		try {
			JSONObject jsonObj = new JSONObject(itemsDetails);
			JSONArray jsonArr = (JSONArray) jsonObj.get("itemDetails");
			List<ItemSelected> itemsSelectedList = new ArrayList<ItemSelected>();

			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject itemDetails = (JSONObject) jsonArr.get(i);

				ItemSelected itemsSelected = new ItemSelected();
				itemsSelected.setNAME(itemDetails.getString("NAME"));

				String priceStr = (String) itemDetails.get("PRICE");
				itemsSelected.setPRICE(Double.parseDouble(priceStr));

				String quantityStr =  (String) itemDetails.get("QUANTITY");;
				itemsSelected.setQUANTITY(Integer.parseInt(quantityStr));

				String weightStr = (String) itemDetails.get("WEIGHT");
				itemsSelected.setWEIGHT(Double.parseDouble(weightStr));

				java.sql.Date currentSqlDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
				itemsSelected.setPURCHASED_DATE(currentSqlDate);
				itemsSelectedList.add(itemsSelected);
			}
			isInserted = itemRepository.postItemDetails(itemsSelectedList);
			responseJsonObj.put("isInserted", isInserted);

		} catch (SelfCheckoutException | JSONException e) {
			e.printStackTrace();
		}	
		return responseJsonObj;
	}

}
