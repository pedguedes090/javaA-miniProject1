package service;

import model.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    /**
     * Tìm món theo tên (không phân biệt hoa thường)
     */
    public List<MenuItem> searchByName(List<MenuItem> menuItems, String keyword) {

        return menuItems.stream()
                .filter(item -> item.getName()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Tìm món theo khoảng giá
     */
    public List<MenuItem> searchByPriceRange(List<MenuItem> menuItems, double minPrice, double maxPrice) {

        return menuItems.stream()
                .filter(item -> {
                    double price = item.calculatePrice();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
}
