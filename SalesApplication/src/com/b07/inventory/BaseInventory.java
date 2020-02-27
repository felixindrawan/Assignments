package com.b07.inventory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.inventory.items.BaseItem;

public class BaseInventory implements Inventory {
  
  //does inventory store all if the iteams or only 1?
  private HashMap<Item, Integer> itemMap;
  private int totalItems;
  
  public BaseInventory() {
    this.totalItems = 0;
    this.itemMap = new HashMap<>();

  }

  @Override
  public HashMap<Item, Integer> getItemMap() {
    // TODO Auto-generated method stub
    return this.itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // TODO Auto-generated method stub
    this.itemMap = itemMap;
    

  }

  @Override
  public void updateMap(Item item, int value) {
    // TODO Auto-generated method stub
    this.itemMap.put(item, value);
    
  }

  @Override
  public int getTotalItems() {
    // TODO Auto-generated method stub
    return this.totalItems;
  }

  @Override
  public void setTotalItems(int total) {
    // TODO Auto-generated method stub
    this.totalItems = total;

  }
  
  public String toString() {
    String totalInventory = "";
    
    System.out.println("Current Inventory");
    
    for (Map.Entry mapElement : itemMap.entrySet()) { 
      Item key = (Item)mapElement.getKey(); 

      totalInventory += key.getId() + ". " + key.getName() + " - quantity: " + itemMap.get(key) + "\n"; 
    }
    
    return totalInventory;
  }

}
