package com.b07.store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.inventory.Item;
import com.b07.users.*;

public class SalesLogImp implements SalesLog {

  private List<Sale> log;
  private HashMap<Item, Integer> soldItems;
  private BigDecimal totalHistorySale;


  public SalesLogImp(Sale sale) {
    log = new ArrayList<Sale>();
    soldItems = new HashMap<>();
    log.add(sale);
    totalHistorySale = new BigDecimal("0.00");



  }

  public SalesLogImp() {
    log = new ArrayList<Sale>();
    soldItems = new HashMap<>();
    totalHistorySale = new BigDecimal("0.00");

  }

  @Override
  public List<Sale> getLog() {
    // TODO Auto-generated method stub
    return this.log;
  }

  @Override
  public void setLog(List<Sale> log) {
    // TODO Auto-generated method stub
    this.log = log;

  }

  @Override
  public void UpdateLog(Sale sale) {
    // TODO Auto-generated method stub
    this.log.add(sale);

  }

  public String toString() {
    String output = "";
    HashMap<Item, Integer> itemMap = new HashMap<>();
    Sale sale = null;
    int leftovers = 0;
    for (int i = 0; i < this.log.size(); i++) {
      sale = this.log.get(i);
      output += sale.toString();
      output += "\n------------------------------------\n";
      totalHistorySale = totalHistorySale.add(sale.getTotalPrice());

      itemMap = sale.getItemMap();

      for (Map.Entry mapElement : itemMap.entrySet()) {
        Item item = (Item) mapElement.getKey();
        Item historyItem = getMapItem(this.soldItems, item.getId());

        if (historyItem == null) {
          (this.soldItems).put(item, itemMap.get(item));
        } else {
          leftovers = (int) this.soldItems.get(historyItem); // this is giving me null
          this.soldItems.put(item, (int) itemMap.get(item) + leftovers);
        }

      }
    }

    for (Map.Entry mapElement : soldItems.entrySet()) {
      Item item = (Item) mapElement.getKey();
      output += "Number " + item.getName() + " Sold: " + soldItems.get(item) + "\n";
    }
    
    output += "\n------------------------------------\n";
    totalHistorySale = totalHistorySale.setScale(2, RoundingMode.CEILING);
    output += "\nTOTAL SALES:" + totalHistorySale;

    return output;
  }

  private static Item getMapItem(HashMap<Item, Integer> shopMap, int itemId) {
    Item currentItem = null;
    try {
      for (Map.Entry mapElement : shopMap.entrySet()) { // itemMap is current null
        currentItem = (Item) mapElement.getKey();

        // Add some bonus marks
        // to all the students and print it
        if (currentItem.getId() == itemId) {
          return currentItem;
        }

      }


    } catch (Exception e) {
      System.out.println("Null map");
      return null;
    }

    return null;
  }

}
