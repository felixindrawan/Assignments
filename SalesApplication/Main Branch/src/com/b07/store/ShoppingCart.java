package com.b07.store;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DatabaseUpdateException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Customer;

public class ShoppingCart {

  private HashMap<Item, Integer> items;
  private Customer customer;
  private BigDecimal total;
  private final BigDecimal TAXRATE = new BigDecimal(0.13);

  public ShoppingCart(Customer customer) {
    items = new HashMap<>();
    total = new BigDecimal("0.00");
    this.customer = customer;
  }
  
  public HashMap<Item, Integer> getItemMap() {
    
    return this.items;
  }

  public void addItem(Item item, int quantity) {
    
    Item mappedItem = getMapItem(items, item.getId());
    if(mappedItem == null) {
      //no items so just add
      items.put(item, quantity);
    } else {
      int leftover = (int)(this.items.get(mappedItem));
      items.put(item, quantity + leftover);
    }
   

  }

  public void removeItem(Item item, int quantity) {
    Item mappedItem = getMapItem(items, item.getId());
    int currentAmount = (int)items.get(mappedItem);

    if (currentAmount - quantity <= 0) {
      items.remove(mappedItem);
    } else {
      items.put(mappedItem, currentAmount - quantity);
    }

  }

  public List<Item> getItems() {
    List<Item> itemList = new ArrayList<>();
    // converting the Set of keys in items to a List
    itemList.addAll(items.keySet());
    return itemList;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public BigDecimal getTotal() {
    BigDecimal total = new BigDecimal("0.00");
    BigDecimal itemTotal = new BigDecimal("0.00");
    BigDecimal mult = new BigDecimal("0.00");
    
    for (Map.Entry mapElement : items.entrySet()) { 
      Item key = (Item)mapElement.getKey(); 
      int val = items.get(key);
      mult = new BigDecimal(val);
      itemTotal = new BigDecimal(key.getPrice()+"");
      itemTotal = itemTotal.multiply(mult);
      total = total.add(itemTotal);
      }
    
    return total;
 }

  public BigDecimal getTaxRate() {
    return this.TAXRATE;
  }

  public void clearCart() {
    this.items.clear();
  }

  public boolean checkOutCustomer()
      throws DatabaseInsertException, SQLException, DatabaseUpdateException {

    if (this.customer != null) {
      List<Item> itemList = this.getItems();
      BigDecimal total = this.getTotal();
      BigDecimal tax = total.multiply(TAXRATE);
      boolean sufficentItems = true;
      
      total = total.add(tax);
      
      int saleId = DatabaseInsertHelper.insertSale(customer.getId(), total);
      Sale sale = new SalesImp(customer.getId(), saleId);
      
      Item currentItem = null;
      int stock = 0;
      for (Map.Entry mapElement : items.entrySet()) { 
        currentItem = (Item)mapElement.getKey();
        stock = DatabaseSelectHelper.getInventoryQuantity(currentItem.getId());
        
        if((int)items.get(currentItem) > stock) {
          sufficentItems = false;
        }
        //check if there is enough quanity for this item
       
      }
      
      if (sufficentItems) {
        for (Map.Entry mapElement : items.entrySet()) { 
          currentItem = (Item)mapElement.getKey();
          stock = DatabaseSelectHelper.getInventoryQuantity(currentItem.getId());
          int newStock = stock - items.get(currentItem);
          DatabaseUpdateHelper.updateInventoryQuantity(newStock, currentItem.getId());
          DatabaseInsertHelper.insertItemizedSale(saleId, currentItem.getId(), items.get(currentItem));
         
        }

        sale.setItemMap(this.items);
        sale.setTotalPrice(this.getTotal());
    
        this.clearCart();
        return true;

      } else {
        System.out.println("Inventory has Insufficent Items please remove some items");
        return false;
      }


    }
    return false;

  }
  
  private static Item getMapItem(HashMap<Item, Integer> shopMap, int itemId) {
    Item currentItem = null;
    for (Map.Entry mapElement : shopMap.entrySet()) { 
      currentItem = (Item)mapElement.getKey(); 

      // Add some bonus marks 
      // to all the students and print it 
      if(currentItem.getId() == itemId) {
        return currentItem;
      }
     
    }
    
    return null;

  }




}
