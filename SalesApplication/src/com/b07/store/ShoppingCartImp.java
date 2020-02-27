package com.b07.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
		int accountId = -2;

		System.out.println("Enter account Id if applicable: \nEnter-1 otherwise\n");

		while (!hasAccount && accountId != -1) {
			accountId = Integer.parseInt(br.readLine());

			if (DatabaseSelectHelper.getUserActiveAccounts(userId).contains(accountId)) {
				hasAccount = true;
			} else if (accountId != -1) {
				System.out.print("Invalid. Please enter your account id or enter -1 if you don't have one\n");
			}
		}

		System.out.println("hasAccount = " + hasAccount);

		Customer customer = (Customer) DatabaseSelectHelper.getUserDetails(userId);
		ShoppingCart shopCart = new ShoppingCart(customer);
		Item item = null;

		while (selection != 9 && selection != 5) {
			int itemId = -1;
			int quantity = -1;
			
			System.out.println("\nWelcome: " + customer.getName() + " Shopping Cart:"
					+ "\nPlease select one of the following options" + "\n1. List current items in cart"
					+ "\n2. Add a quantity of an item to the cart" + "\n3. Check total price of items in the cart"
					+ "\n4. Remove a quantity of an item from the cart" + "\n5.check out" + "\n6. Restore previous cart"
					+ "\n7. Send current shopping cart as gift" + "\n8. Email new wishlist" + "\n9. Exit");

			try {
				// selection = -1;
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
					userInput = getItemId();
					itemId = userInput;
					
					System.out.println("Please enter a quantity");
					while(quantity < 1) {
						quantity = Integer.parseInt(br.readLine());
						if(quantity < 1) System.out.println("Enter a valid quantity");
					}
					
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

				// Aaron -- adding items to accountsummary db
				if (hasAccount) {
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
						while(quantity < 0) {
							quantity = Integer.parseInt(br.readLine());
							if(quantity < 0) System.out.println("Invalid Quanity, must be greater than 0!");
						}
						shopCart.removeItem(item, quantity);
					} else {
						System.out.println("Invalid ItemId");
					}
				} catch (Exception e) {
					System.out.println("Please enter an integer");
				}
				break;

			case 5:
				// check out
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
						if (hasAccount) {
							DatabaseUpdateHelper.updateAccountStatus(accountId, false);
						}
					} else {
						System.out.println("Checkout has failed");
					}
				}

				break;
			case 6:
				System.out.println("Option - restore previous shopping cart");
				HashMap<Item, Integer> currentInventory = inventory.getItemMap();

				if (hasAccount) {

					HashMap<Integer, Integer> results = DatabaseSelectHelper.getAccountDetails(accountId);

					Iterator iterator = results.keySet().iterator();

					while (iterator.hasNext()) {
						int key = (int) iterator.next();
						int value = results.get(key);

						Item itemPrev = getMapItem(currentInventory, key);
						shopCart.addItem(itemPrev, value);
						// System.out.println("ItemId: " + key + " Quantity: " + value);
					}
				}
				break;

			case 7:
				System.out.println("SENDING GIFT(S) OPTION CHOSEN!!!!");
				String receiverName = "";
				String receiverAddress = "";
				char correctInfo = 'n';

				if (shopCart.getItemMap() != null) {
					HashMap<Item, Integer> mapOfItems = shopCart.getItemMap();

					System.out.println("You are sending these items");

					for (Map.Entry mapElement : mapOfItems.entrySet()) {
						Item currentItem = (Item) mapElement.getKey();
						System.out.println("\n" + currentItem.getName() + " : " + mapOfItems.get(currentItem));
					}

					while (correctInfo == 'n') {
						System.out.println("Who would you like to send gifts to? ");
						receiverName = br.readLine();

						System.out.println("Enter address of person: ");
						receiverAddress = br.readLine();

						System.out.println("You are sending gift(s) to " + receiverName);
						System.out.println(receiverName + "'s address is " + receiverAddress);
						System.out.println("Is this correct (y/n)?");
						correctInfo = (char) br.read();
						br.readLine();
					}

					System.out.println("Checking out now...");

					boolean checkedout = shopCart.checkOutCustomer();
					if (checkedout) {
						System.out.println("Successful Checkout" + "\nThank you for shopping!");
						if (hasAccount) {
							DatabaseUpdateHelper.updateAccountStatus(accountId, false);
						}
					} else {
						System.out.println("Checkout has failed");
					}
				}

				else
					System.out.println("Shopping Cart is empty");

				break;

			case 8:
				System.out.println("WISHLIST OPTION");
				System.out.println(inventory.toString());
				boolean done = false;
				int idAdd;
				String itemName;
				List<String> wishlistItems = new ArrayList<>();
				List<String> emailAddresses = null;

				while (!done) {
					try {
						System.out.println("Enter Item Id # to add to wishlist (-1 when done)");
						idAdd = getItemId();

						if (idAdd >= 1 && idAdd <= 5) {
							Item wish = DatabaseSelectHelper.getItem(idAdd);
							itemName = wish.getName();
							wish = null;
							if (wishlistItems.size() == 5) {
								System.out.println("Already added all items");
								done = true;
								break;
							}
							if (wishlistItems.contains(itemName))
								System.out.println("Item already in wishlist");
							else {
								wishlistItems.add(itemName);
							}
						} else if (idAdd == -1)
							done = true;
						else
							throw new Exception();
					} catch (Exception e) {
						System.out.println("Must enter item id 1-5; (-1 when done)");
					}
				}

				if (!wishlistItems.isEmpty()) {
					System.out.println("Enter email(s) to send wishlist to, separated by commas(,):");
					String emails = br.readLine();
					emailAddresses = new ArrayList<String>(Arrays.asList(emails.split(",")));
					emailAddresses.replaceAll(String::trim);
				}

				if (!emailAddresses.isEmpty() && !emailAddresses.get(0).equals(""))
					Email.emailWishlist(wishlistItems, emailAddresses, customer.getName());

				else
					System.out.println("Finished");

				break;
			case 9:
				System.out.println("Exiting Application thank you!");
				break;

			default:
				System.out.println("Invalid Number please try again");
				break;

			}

		}

	}
	
	
	private static int getItemId() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int userInput = 0;
		
		while (5 < userInput || userInput < 1) {
			userInput = Integer.parseInt(br.readLine());
			if (1 <= userInput && userInput <= 5) System.out.println("Enter valid item ID");
		}
		
		return userInput;
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
