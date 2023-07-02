package ru.inno.market;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Order;
import ru.inno.market.model.PromoCodes;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса MarketService")
public class OrderTest {

    @Test
    @DisplayName("Создание заказа")
    public void shouldCreateOrder() {
        int orderId = 1;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        assertEquals(order.getId(), orderId);
    }

    @Test
    @DisplayName("Создание заказа c отрицательным id")
    public void shouldCreateOrderWithNegativeId() {
        int orderId = -1;
        Client client = new Client(1, "Anna");
        assertThrows(NullPointerException.class, () -> new Order(orderId, client));
    }

    @Test
    @DisplayName("Создание заказа для определённого клиента")
    public void shouldCreateOrderForSpecificClient() {
        int orderId = 1;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        assertEquals(order.getClient(), client);
    }

    @Test
    @DisplayName("Создание заказа без клиента (client == null)")
    public void shouldCreateOrderIfClientIsNull() {
        assertThrows(NullPointerException.class, () -> new Order(1, null));
    }

    @Test
    @DisplayName("Добавление товара в заказ")
    public void shouldAddAnItemToTheOrder() {
        int orderId = 1;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        Catalog catalog = new Catalog();
        order.addItem(catalog.getItemById(1));
        assertTrue(order.getItems().containsKey(catalog.getItemById(1)));
    }

    @Test
    @DisplayName("Добавление нескольких товаров в заказ")
    public void shouldAddSeveralItemsToTheOrder() {
        int orderId = 1;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        Catalog catalog = new Catalog();
        order.addItem(catalog.getItemById(2));
        order.addItem(catalog.getItemById(3));
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(catalog.getItemById(2));
//        items.add(catalog.getItemById(3));
//        assertArrayEquals(order.getItems().keySet().toArray(), items.toArray());
        assertTrue(order.getItems().containsKey(catalog.getItemById(2)));
        assertTrue(order.getItems().containsKey(catalog.getItemById(3)));
    }

    @Test
    @DisplayName("Проверка соответствия количества товаров в корзине добавленному")
    public void shouldQuantityOfItemsMatches() {
        int orderId = 1;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        Catalog catalog = new Catalog();
        order.addItem(catalog.getItemById(2));
        order.addItem(catalog.getItemById(3));
        assertEquals(2, order.getCart().size());
    }

    @Test
    @DisplayName("Добавление одного товара в корзину несколько раз")
    public void shouldAddSeveralInstancesOfOneItem() {
        int orderId = 2;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        Catalog catalog = new Catalog();

        for (int i = 0; i < 4; i++) {
            order.addItem(catalog.getItemById(11));
        }
        assertEquals(4, order.getItems().size());
    }

    @Test
    @DisplayName("Проверка правильности расчёта скидки")
    public void shouldDiscountAppliesCorrectly() {
        int orderId = 3;
        Client client = new Client(1, "Anna");
        Order order = new Order(orderId, client);
        Catalog catalog = new Catalog();
        order.addItem(catalog.getItemById(6));
        double priceBeforeApplyingDiscount = order.getTotalPrice();
        order.applyDiscount(PromoCodes.LOVE_DAY.getDiscount());
        double priceAfterApplyingDiscount = order.getTotalPrice();
        assertEquals(priceBeforeApplyingDiscount * (1 - PromoCodes.LOVE_DAY.getDiscount()), priceAfterApplyingDiscount);
    }
}
