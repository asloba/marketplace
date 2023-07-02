package ru.inno.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.inno.market.model.PromoCodes.FIRST_ORDER;

@DisplayName("Тесты для класса MarketService")
public class MarketServiceTest {

    private int orderId;
    private MarketService service;
    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client(1, "Anna");
        service = new MarketService();
        orderId = service.createOrderFor(client);
    }

    @Test
    @DisplayName("Проверка соответствия пользователя из заказа текущему")
    public void createOrderForClient() {
        Client clientFromOrder = service.getOrderInfo(orderId).getClient();
        assert clientFromOrder.equals(client);
    }

    @Test
    @DisplayName("Добавление товара в заказ")
    public void addAnItemToTheMarketOrder() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        service.addItemToOrder(catalog.getItemById(8), orderId);
        assertTrue(service.getOrderInfo(orderId).getCart().containsKey(catalog.getItemById(8)));
    }

    @Test
    @DisplayName("Добавление нескольких товаров в заказ")
    public void addSeveralItemsToTheMarketOrder() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        service.addItemToOrder(catalog.getItemById(8), orderId);
        service.addItemToOrder(catalog.getItemById(9), orderId);
        assertTrue(service.getOrderInfo(orderId).getCart().containsKey(catalog.getItemById(8)));
        assertTrue(service.getOrderInfo(orderId).getCart().containsKey(catalog.getItemById(9)));
    }

    @Test
    @DisplayName("Проверка соответствия количества товаров в заказе добавленному")
    public void shouldQuantityOfItemsMatchesInMarketOrder() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        Item firstItem = catalog.getItemById(4);
        Item secondItem = catalog.getItemById(5);
        service.addItemToOrder(firstItem, orderId);
        service.addItemToOrder(secondItem, orderId);
        assertEquals(2, service.getOrderInfo(orderId).getItems().size());
    }

    @Test
    @DisplayName("Проверка корректности применения скидки")
    public void shouldDiscountApplyCorrectly() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        service.addItemToOrder(catalog.getItemById(12), orderId);
        service.addItemToOrder(catalog.getItemById(10), orderId);
        double totalPriceBeforeApplyingDiscount = service.getOrderInfo(orderId).getTotalPrice();
        service.applyDiscountForOrder(orderId, FIRST_ORDER);
        double totalPriceAfterApplyingDiscount = service.getOrderInfo(orderId).getTotalPrice();
        assertEquals(totalPriceBeforeApplyingDiscount * (1 - FIRST_ORDER.getDiscount()), totalPriceAfterApplyingDiscount);
    }

    @Test
    @DisplayName("Проверка  применения скидки")
    public void shouldDiscountApply() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        service.addItemToOrder(catalog.getItemById(10), orderId);
        service.applyDiscountForOrder(orderId, FIRST_ORDER);
        assertTrue(service.getOrderInfo(orderId).isDiscountApplied());
    }

    @Test
    @DisplayName("Проверка уведомления об окончании товара на складе")
    public void shouldReceiveExceptionWhileItemIsOver() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
        Catalog catalog = new Catalog();
        int itemQuantity = catalog.getCountForItem(catalog.getItemById(6));

        for (int i = 0; i < itemQuantity; i++) {
            service.addItemToOrder(catalog.getItemById(6), orderId);
        }
        assertThrows(NoSuchElementException.class, () -> catalog.getCountForItem(catalog.getItemById(6)));
    }
}
