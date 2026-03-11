package service;

import exception.InvalidInputException;
import exception.InvalidPriceException;
import exception.ItemNotFoundException;
import model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuService {

    private List<MenuItem> menu = new ArrayList<>();

    public void addItem(MenuItem item) throws InvalidInputException, InvalidPriceException {

        if (item == null) {
            throw new InvalidInputException("Item không hợp lệ");
        }

        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new InvalidInputException("Tên món không hợp lệ");
        }

        if (item.getBasePrice() <= 0) {
            throw new InvalidPriceException("Giá phải lớn hơn 0");
        }

        for (MenuItem i : menu) {
            if (i.getName().equalsIgnoreCase(item.getName())) {
                throw new InvalidInputException("Món đã tồn tại");
            }
        }

        menu.add(item);
    }

    public void removeItem(String name) throws InvalidInputException, ItemNotFoundException {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Tên món không hợp lệ");
        }

        MenuItem found = null;

        for (MenuItem item : menu) {
            if (item.getName().equalsIgnoreCase(name)) {
                found = item;
                break;
            }
        }

        if (found == null) {
            throw new ItemNotFoundException("Không tìm thấy món");
        }

        menu.remove(found);
    }

    public List<MenuItem> search(String keyword) {

        List<MenuItem> result = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        for (MenuItem item : menu) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(item);
            }
        }

        return result;
    }

    public void displayMenu() {

        if (menu.isEmpty()) {
            System.out.println("Menu đang trống");
            return;
        }

        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public List<MenuItem> getMenu() {
        return menu;
    }
}