package util;

import java.util.Scanner;

public class InputHelper {
    private static Scanner sc= new Scanner(System.in);

    // input phải là số nguyên
    public static int inputInt(String input){
        while (true){
            try{
                System.out.println(input);
                return Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Vui lòng nhập vào số nguyên hợp lệ!");
            }
        }
    }

    // input phải là số thực
    public static double inputDouble(String input){
        while (true){
            try{
                System.out.println(input);
                return Double.parseDouble(sc.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Vui lòng nhập vào số thực hợp lệ!");
            }
        }
    }

    // input là chuỗi
    public static String inputString(String input){
        System.out.println(input);
        return sc.nextLine();
    }

}