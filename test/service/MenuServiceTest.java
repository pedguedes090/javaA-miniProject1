package service;

import exception.InvalidInputException;
import exception.InvalidPriceException;
import exception.ItemNotFoundException;
import model.Dessert;
import model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuServiceTest {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService();
    }

    // ==========================================
    // 1. TEST HÀM addItem
    // ==========================================

    @Test
    void testAddItem_NullItem_ThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            menuService.addItem(null);
        }, "Phải ném lỗi khi thêm món null");
    }

    @Test
    void testAddItem_EmptyName_ThrowsException() {
        // Khởi tạo món có tên rỗng, truyền đủ id, tên, giá, số lượng
        MenuItem itemWithEmptyName = new Dessert("D01", "", 50000.0, 10);

        assertThrows(InvalidInputException.class, () -> {
            menuService.addItem(itemWithEmptyName);
        });
    }

    @Test
    void testAddItem_InvalidPrice_ThrowsException() {
        // Khởi tạo món có giá <= 0
        MenuItem itemWithInvalidPrice = new Dessert("D02", "Bánh ngọt", 0.0, 10);

        assertThrows(InvalidPriceException.class, () -> {
            menuService.addItem(itemWithInvalidPrice);
        });
    }

    @Test
    void testAddItem_DuplicateName_ThrowsException() throws Exception {
        // Khởi tạo 2 món ăn có CÙNG TÊN (để test phân biệt hoa/thường)
        MenuItem item1 = new Dessert("D03", "Chè đậu đen", 20000.0, 50);
        MenuItem item2 = new Dessert("D04", "CHÈ ĐẬU ĐEN", 25000.0, 30);

        menuService.addItem(item1); // Thêm lần 1 thành công

        // Thêm lần 2 trùng tên phải báo lỗi InvalidInputException
        assertThrows(InvalidInputException.class, () -> {
            menuService.addItem(item2);
        });
    }

    @Test
    void testAddItem_Success() throws Exception {
        MenuItem validItem = new Dessert("D05", "Kem Vanilla", 15000.0, 20);

        menuService.addItem(validItem);

        // Kiểm tra xem danh sách menu đã có 1 phần tử chưa
        assertEquals(1, menuService.getMenu().size());
        assertEquals(validItem, menuService.getMenu().get(0));
    }

    // ==========================================
    // 2. TEST HÀM removeItem
    // ==========================================

    @Test
    void testRemoveItem_EmptyName_ThrowsException() {
        assertThrows(InvalidInputException.class, () -> {
            menuService.removeItem("   ");
        });
    }

    @Test
    void testRemoveItem_NotFound_ThrowsException() {
        assertThrows(ItemNotFoundException.class, () -> {
            menuService.removeItem("Món không tồn tại");
        });
    }

    @Test
    void testRemoveItem_Success() throws Exception {
        MenuItem validItem = new Dessert("D06", "Bánh Flan", 15000.0, 20);

        // Thêm món vào trước
        menuService.addItem(validItem);
        assertEquals(1, menuService.getMenu().size());

        // Sau đó xóa đi (Test luôn việc không phân biệt hoa thường khi xóa)
        menuService.removeItem("bánh flan");

        // Kiểm tra menu phải về lại 0 phần tử
        assertTrue(menuService.getMenu().isEmpty());
    }

    // ==========================================
    // 3. TEST HÀM search
    // ==========================================

    @Test
    void testSearch_EmptyKeyword_ReturnsEmptyList() throws Exception {
        MenuItem validItem = new Dessert("D07", "Bánh Tiramisu", 45000.0, 10);
        menuService.addItem(validItem);

        List<MenuItem> result = menuService.search("   ");
        assertTrue(result.isEmpty(), "Keyword rỗng phải trả về list rỗng");
    }

    @Test
    void testSearch_ValidKeyword_ReturnsMatchingItems() throws Exception {
        MenuItem item1 = new Dessert("D08", "Trà sữa trân châu", 30000.0, 20);
        MenuItem item2 = new Dessert("D09", "Trà đào cam sả", 35000.0, 15);
        MenuItem item3 = new Dessert("D10", "Cà phê đen", 20000.0, 30);

        menuService.addItem(item1);
        menuService.addItem(item2);
        menuService.addItem(item3);

        // Tìm từ khóa "Trà", kết quả phải ra 2 món
        List<MenuItem> result = menuService.search("Trà");
        assertEquals(2, result.size());

        // Tìm từ khóa "cà phê", kết quả ra 1 món
        List<MenuItem> result2 = menuService.search("cà phê");
        assertEquals(1, result2.size());
    }
}