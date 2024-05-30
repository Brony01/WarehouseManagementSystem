package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.config.SecurityConfig;
import com.java.warehousemanagementsystem.pojo.Item;
import com.java.warehousemanagementsystem.service.ItemService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(ItemController.class)
@Import(SecurityConfig.class)
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        this.webTestClient = this.webTestClient.mutateWith(SecurityMockServerConfigurers.mockUser());
    }

    @Test
    void testGetAllItems() {
        Item item1 = new Item(1, "Item1", "Description1", 100, 10.0, 1, null, null);
        Item item2 = new Item(2, "Item2", "Description2", 200, 20.0, 2, null, null);
        List<Item> items = Arrays.asList(item1, item2);

        given(itemService.findAllItems()).willReturn(Flux.fromIterable(items));

        webTestClient.get().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class).isEqualTo(items);
    }

    @Test
    void testAddItem() {
        given(itemService.addItem("NewItem", "NewDescription", 100, 30.0, 1))
                .willReturn(Mono.just(true));

        webTestClient.post().uri("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new Item(null, "NewItem", "NewDescription", 100, 30.0, 1, null, null)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("物品添加成功");
                });
    }

    @Test
    void testUpdateItem() {
        given(itemService.updateItem(1, "UpdatedItem", "UpdatedDescription", 150, 35.0, 1))
                .willReturn(Mono.just(true));

        webTestClient.put().uri("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new Item(1, "UpdatedItem", "UpdatedDescription", 150, 35.0, 1, null, null)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("物品信息更新成功");
                });
    }

    @Test
    void testGetItemById() {
        Item item = new Item(1, "Item1", "Description1", 100, 10.0, 1, null, null);

        given(itemService.findItemById(1)).willReturn(Mono.just(item));

        webTestClient.get().uri("/item/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    System.out.println("Response Code: " + response.getCode());
                    System.out.println("Response Data: " + response.getData());
                    assert response.getCode() == 200 : "Expected code 200 but got " + response.getCode();
                    assert response.getData().equals(item) : "Expected item " + item + " but got " + response.getData();
                });
    }

    @Test
    void testDeleteItem() {
        given(itemService.deleteItem(1)).willReturn(Mono.just(true));

        webTestClient.delete().uri("/item/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("成功删除物品");
                });
    }
}
