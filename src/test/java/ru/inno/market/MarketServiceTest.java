package ru.inno.market;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты для класса MarketService")
public class MarketServiceTest {

    @Test
    @DisplayName("Проверка соответствия пользователя из заказа текущему")
    public void createOrderForClient() {
        Client client = new Client(1, "Anna");
        MarketService service = new MarketService();
        int orderId = service.createOrderFor(client);
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
        service.addItemToOrder(catalog.getItemById(4), orderId);
        service.addItemToOrder(catalog.getItemById(5), orderId);
        assertEquals(2, service.getOrderInfo(orderId).getItems().size());
    }
}
