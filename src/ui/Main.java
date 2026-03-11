package ui;

import exception.InvalidOrderIdException;

public class Main {
    public static void main(String[] args) throws InvalidOrderIdException {
        MainMenu mainMenu= new MainMenu();
        mainMenu.main(args);
    }
}