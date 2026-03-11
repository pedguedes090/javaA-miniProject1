package ui;

import exception.InsufficientStockException;
import exception.InvalidInputException;
import exception.InvalidOrderIdException;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import service.MenuService;
import service.OrderService;
import util.IdGenerator;
import util.InputHelper;

import java.util.List;

public class OrderUI {
    private OrderService orderService;
    private MenuService menuService;

    public OrderUI(OrderService orderService, MenuService menuService) {
        this.orderService = orderService;
        this.menuService = menuService;
    }

    public void OrderMenu() throws InvalidOrderIdException {
        while (true){
            System.out.println("""
                    ORDER MANAGEMENT
            1. Create order
            2. Add item to order
            3. Update order status
            4. Remove item from order
            5. Show order
            0. Back                  
                    """);
            int choice= InputHelper.inputInt("Your choice: ");
            switch (choice){
                case 1:{
                    // sinh id
                    String orderId= IdGenerator.generatorOrderId();
                    Order order=orderService.createOrder(orderId);
                    System.out.println("Created order: "+order.getId());
                    break;}
                case 2:{
                    try {
                        String orderId = InputHelper.inputString("Enter order id: ");
                        String itemId = InputHelper.inputString("Enter menu item id: ");
                        int quantity = InputHelper.inputInt("Enter quantity: ");
                        MenuItem menuItem = findMenuItemById(itemId);
                        if (menuItem == null) { System.out.println("Menu item not found");
                            break;
                        }
                        orderService.addItemToOrder(orderId, menuItem, quantity);
                        System.out.println("Item added to order successfully!");
                    } catch (InvalidOrderIdException | InsufficientStockException | InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;}
                case 3: {
                    try {
                        String orderId = InputHelper.inputString("Enter order id: ");
                        System.out.println("""
                Choose new status
                1. PENDING
                2. PAID
                3. CANCELLED
                """);
                        int statusChoice = InputHelper.inputInt("Your choice: ");
                        OrderStatus newStatus = null;
                        switch (statusChoice) {
                            case 1:
                                newStatus = OrderStatus.PENDING;
                                break;
                            case 2:
                                newStatus = OrderStatus.PAID;
                                break;
                            case 3:
                                newStatus = OrderStatus.CANCELLED;
                                break;
                            default:
                                System.out.println("Invalid status");
                                break;
                        }
                        orderService.updateOrderStatus(orderId, newStatus);
                        System.out.println("Order status updated successfully!");
                    } catch (InvalidOrderIdException | InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4: {
                    try {
                        String orderId = InputHelper.inputString("Enter order id: ");
                        String menuItemId = InputHelper.inputString("Enter menu item id to remove: ");
                        orderService.removeItemFromOrder(orderId, menuItemId);
                        System.out.println("Item removed from order successfully!");
                    } catch (InvalidOrderIdException | InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5: {
                    try {

                        String orderId = InputHelper.inputString("Enter order id: ");

                        Order order = orderService.getOrderById(orderId);

                        System.out.println("\n===== ORDER INFO =====");
                        System.out.println("Order ID: " + order.getId());
                        System.out.println("Status: " + order.getStatus());

                        if (order.getItems().isEmpty()) {
                            System.out.println("Order has no items");
                            break;
                        }

                        double total = 0;

                        System.out.println("\nItems:");

                        for (OrderItem item : order.getItems()) {

                            MenuItem menuItem = item.getMenuItem();

                            System.out.println(
                                    menuItem.getId() + " | " +
                                            menuItem.getName() +
                                            " | qty: " + item.getQuantity() +
                                            " | price: " + item.getUnitPrice() +
                                            " | total: " + item.getLineTotal()
                            );
                            total += item.getLineTotal();
                        }
                        System.out.println("------------------------");
                        System.out.println("Order total: " + total);

                    } catch (InvalidOrderIdException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Your choice is invalid");

            }
        }
    }
    private MenuItem findMenuItemById(String id) {
        List<MenuItem> menu = menuService.getMenu();
        for (MenuItem item : menu) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

}
