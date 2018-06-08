package com.selfcheckout.repo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.selfcheckout.dto.Item;
import com.selfcheckout.dto.ItemSelected;

@Component
public interface ItemRepository {
	
	public List<Item> getItemDetails(String itemName);
	public boolean postItemDetails(List<ItemSelected> itemsSelectedList);

}
