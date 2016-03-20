/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author Katie Lin
 */
public class Descriptions {
    public static final String TEAM_QUERY = "Displays the total revenue earned by a particular team or simply revenue by all teams.";
    public static final String PLAYER_SALES = "Displays the number of unique customers that have ever purchased. "
            + "a product belonging to a particular player.";
    public static final String PRODUCTS_PURCHASED = "Displays information of every customer that has ever purchased a particular product.";
    public static final String PLACE_ORDER = "Creates a new order in our database using the inputted information specified below. "
            + "If the customer's ID does not exist in our system, the user is prompted by an additional, optional dialog to create this user.";
    public static final String TRANSFER_WAREHOUSE = "Moves a number of products in our system from the source warehouse to the destination warehouse. "
            + "Please note that atleast 1 unit of the product must remain within the warehouse. Namely, you cannot move everything.";
    public static final String CHANGE_PRICE = "This is a multi-purpose procedure that given a threshold, we can increase or decrease the prices by an input modifier.\n" +
"The decision to change the prices above or below the threshold is indicated by selecting Above or Below.\n" +
"The modifier is a multiplier on the existing price.\n" +
"If above is selected, only the products that have sold quantity strictly greater than the threshold will be modified.\n" +
"Likewise, if below is selected, only the products that have sold quantity strictly lesser than the threshold will be modified.\n" +
"The items are modified by changing their retail price given by newPrice = oldPrice * modifier\n" +
"e.g. changePrice(5, true, 1.1) = multiply the prices of all items that have been sold over 5 times by 1.1\n" +
"changePrices(5, false, 0.9) = multiply the prices of all items that have been sold under 5 times by 0.9\n"
            + "Note that you cannot increase or decrease by a modifier greater than 20% in one go.";
}
