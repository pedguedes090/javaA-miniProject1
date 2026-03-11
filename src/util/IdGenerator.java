package util;
public class IdGenerator {
    private static int orderCount=1;
    private static int menuCount=1;

    // Tự sinh id cho order vd ORD001
    public static String generatorOrderId(){

        return String.format("ORD%03d",orderCount++);
    }

    // Tự sinh id cho menu vs M001
    public static String generatorMenuId(){

        return String.format("M%03d",menuCount++);
    }
}