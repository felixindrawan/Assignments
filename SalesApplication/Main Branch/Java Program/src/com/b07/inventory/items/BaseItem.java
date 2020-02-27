package com.b07.inventory.items;

import java.math.BigDecimal;
import com.b07.inventory.Item;

public class BaseItem implements Item {

  private int id;
  private String name;
  private BigDecimal price;

  public BaseItem(int id, String name, BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.price.setScale(2, BigDecimal.ROUND_HALF_EVEN);

  }
  

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return this.id;
  }

  @Override
  public void setId(int id) {
    // TODO Auto-generated method stub
    this.id = id;

  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return this.name;
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub
    this.name = name;

  }

  @Override
  public BigDecimal getPrice() {
    // TODO Auto-generated method stub
    return this.price;
  }

  @Override
  public void setPrice(BigDecimal price) {
    // TODO Auto-generated method stub
    this.price = price;

  }

}


