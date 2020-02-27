package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.b07.database.helper.*;
import com.b07.exceptions.*;
import com.b07.inventory.*;
import com.b07.users.*;

public class ShoppingCartImp {

	public static void ShoppingCartInterface()
			throws NumberFormatException, IOException, SQLException, DatabaseUpdateException, DatabaseInsertException {

		Inventory inventory = DatabaseSelectHelper.getInventory();
		List<Item> itemList = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int userInput = -1;
		int selection = -1;
		int itemId = -1;
		int quantity = -1;
		int maxStock = -1;
		int roleId = -1;
		int userId = -1;
		String roleName = "";
		String password = "";
		/*
		 * userId = DatabaseInsertHelper.insertNewUser("Customer", 12 , "1234", "1234");
		 * roleId = DatabaseInsertHelper.insertRole("CUSTOMER"); int relationshipId =
		 * DatabaseInsertHelper.insertUserRole(userId, roleId);
		 * System.out.println("ID: " + relationshipId);
		 */
		// valid the user make a function
		boolean authenticated = false;

		while (!authenticated) {
			try {
				System.out.println("Login: " + "\nEnter UserId");
				userInput = Integer.parseInt(br.readLine());
				User user = DatabaseSelectHelper.getUserDetails(userInput);

				if (user != null) {
					userId = userInput;
					roleId = DatabaseSelectHelper.getUserRole(userId);
					roleName = DatabaseSelectHelper.getRoleName(roleId);
					if (roleName.equals("CUSTOMER")) {
						// get password
						System.out.println("Please Enter Password");
						password = br.readLine();
						authenticated = user.authenticate(password);

						if (!authenticated) {
							System.out.println("Incorrect password please try again");
						}
					} else {
						System.out.println("User is not a customer");
					}

				} else {
					System.out.println("UserID is invalid please try again");
				}
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Invalid Input");
				userInput = -1;
				password = "";
			}

		}

		// Aaron - checking if user has an account
		boolean hasAccount = false;

		System.out.println("Enter account Id if applicable: \nEnter-1 otherwise");
		int accountId = Integer.parseInt(br.readLine());
		if (DatabaseSelectHelper.getUserAccounts(userId).contains(accountId))
			hasAccount = true;

		System.out.println("hasAccount = " + hasAccount);

		Customer customer = (Customer) DatabaseSelectHelper.getUserDetails(userId);
		ShoppingCart shopCart = new ShoppingCart(customer);
		Item item = null;

		while (selection != 7 && selection != 5) {
			System.out.println("Welcome: " + customer.getName() + "Shopping Cart:"
					+ "\nPlease select one of the following options" + "\n1. List current items in cart"
					+ "\n2. Add a quantity of an item to the cart" + "\n3. Check total price of items in the cart"
					+ "\n4. Remove a quantity of an item from the cart" + "\n5.check out" + "\n6. Restore previous cart"
					+ "\n7. Exit");

			try {
				selection = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				System.out.println("Enter an integer");
			}

			switch (selection) {
			case 1:// Print List
				HashMap<Item, Integer> itemMap = shopCart.getItemMap();
				System.out.println("Inventory:");
				
				for (Map.Entry mapElement : itemMap.entrySet()) {
					Item currentItem = (Item) mapElement.getKey();
					System.out.println("\n" + currentItem.getName() + " : " + itemMap.get(currentItem));
				}
				break;

			case 2:// Add item
				try {
					HashMap<Item, Integer> currentInventory = inventory.getItemMap();
					System.out.println(inventory.toString());
					System.out.println("Please enter in ItemId");
					userInput = Integer.parseInt(br.readLine());
					itemId = userInput;
					System.out.println("Please enter a quantity");
					quantity = Integer.parseInt(br.readLine());
					item = getMapItem(currentInventory, userInput); // gets an item whos keys work for the function
					maxStock = (int) currentInventory.get(item);
					System.out.println(maxStock);

					if (item != null && quantity > 0 && quantity <= maxStock) {
						shopCart.addItem(item, quantity);
					} else if (item == null) {
						System.out.println("Invalid Item Id!");
					} else if (quantity <= 0) {
						System.out.println("Quantity must be greater than 0! and less than " + maxStock);
					}

				} catch (Exception e) {
					System.out.println("Please enter a integer");
				}
				
				//Aaron -- adding items to accountsummary db
				if(hasAccount) {
					System.out.println("Entered Here");
					DatabaseInsertHelper.insertAccountLine(accountId, itemId, quantity);
				}
				
				break;

			case 3: // print Total price
				BigDecimal totalprice = shopCart.getTotal();
				System.out.println("Total Price: " + totalprice);
				break;

			case 4: // Remove Item
				try {
					itemMap = shopCart.getItemMap();
					System.out.println("Enter Item id");
					itemId = Integer.parseInt(br.readLine());
					item = getMapItem(itemMap, itemId); // grabs item from cart

					if (item != null) {
						System.out.println("Enter how many items are to be removed");
						quantity = Integer.parseInt(br.readLine());

						if (quantity > 0) {
							shopCart.removeItem(item, quantity);
						} else {
							System.out.println("Invalid Quanity, must be greater than 0!");
						}

					} else {
						System.out.println("Invalid ItemId");
					}
				} catch (Exception e) {
					System.out.println("Please enter an integer");
				}
				break;

			case 5:
				; // check out
				System.out.println("Your total: " + shopCart.getTotal() + "" + "\n1. To contiune to Checkout"
						+ "'\n anything to return");

				try {
					userInput = Integer.parseInt(br.readLine());
				} catch (Exception e) {
					System.out.println("Please Input a number");
				}

				if (userInput == 1) {
					boolean checkedout = shopCart.checkOutCustomer();
					if (checkedout) {
						System.out.println("Successful Checkout" + "\nThank you for shopping!");
					} else {
						System.out.println("Checkout has failed");
					}
				}

				break;
			case 6:
				System.out.println("Option - restore previous shopping cart");
				HashMap<Item, Integer> currentInventory = inventory.getItemMap();
				
				if(hasAccount) {
					
					HashMap<Integer, Integer> results = DatabaseSelectHelper.getAccountDetails(accountId);
					
					Iterator iterator = results.keySet().iterator();
					
					while(iterator.hasNext()) {
						int key = (int) iterator.next();
						int value = results.get(key);
						
						Item itemPrev = getMapItem(currentInventory, key);
						shopCart.addItem(itemPrev, value);
						//System.out.println("ItemId: " + key + " Quantity: " + value);
					}
				}
				break;
			case 7:
				System.out.println("Exiting Application thank you!");
				break;
			default:
				System.out.println("Invalid Number please try again");
				break;

			}

		}

	}

	private static Item getMapItem(HashMap<Item, Integer> shopMap, int itemId) {

		try {
			for (Map.Entry mapElement : shopMap.entrySet()) {
				Item item = (Item) mapElement.getKey();
				if (item.getId() == itemId) {
					System.out.println("Found");
					return item;
				}
				System.out.print("\n" + item.getName() + " : " + shopMap.get(item));
			}
		} catch (Exception e) {
			System.out.println("Null Map");
			return null;
		}

		return null;

	}

}
