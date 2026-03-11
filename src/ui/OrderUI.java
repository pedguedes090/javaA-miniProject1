package ui;

import exception.InvalidOrderIdException;
import model.Order;
import service.OrderService;
import util.IdGenerator;
import util.InputHelper;

public class OrderUI {
    private OrderService orderService= new OrderService();
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
                    String orderId=InputHelper.inputString("Order id: ");
                  //  String itemId=InputHelper.inputString("")
                    break;}
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Your choice is invalid");

            }
        }
    }
}