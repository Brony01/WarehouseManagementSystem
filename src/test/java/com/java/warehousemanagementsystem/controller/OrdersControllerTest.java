package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.config.SecurityConfig;
import com.java.warehousemanagementsystem.pojo.Orders;
import com.java.warehousemanagementsystem.service.OrdersService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@WebFluxTest(OrdersController.class)
@Import(SecurityConfig.class)
public class OrdersControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrdersService ordersService;

    @BeforeEach
    public void setUp() {
        this.webTestClient = this.webTestClient.mutateWith(SecurityMockServerConfigurers.mockUser());
    }

    @Test
    void testGetAllOrders() {
        Orders order1 = new Orders(1, "user1", "Pending", 100.0, "Address1", null, null, null);
        Orders order2 = new Orders(2, "user2", "Completed", 200.0, "Address2", null, null, null);
        List<Orders> orders = Arrays.asList(order1, order2);

        given(ordersService.findOrdersByStatus("Pending")).willReturn(Flux.fromIterable(orders));

        webTestClient.get().uri("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Orders.class).isEqualTo(orders);
    }

    @Test
    void testAddOrder() {
        Orders order = new Orders(null, "user3", "Pending", 300.0, "NewAddress", null, null, null);
        given(ordersService.addOrder(order)).willReturn(Mono.just(true));

        webTestClient.post().uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(order))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("订单添加成功");
                });
    }

    @Test
    void testUpdateOrder() {
        Orders order = new Orders(1, "user1", "Completed", 350.0, "UpdatedAddress", null, null, null);
        given(ordersService.updateOrder(order)).willReturn(Mono.just(true));

        webTestClient.put().uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(order))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("订单信息更新成功");
                });
    }

    @Test
    void testGetOrderById() {
        Orders order = new Orders(1, "user1", "Pending", 100.0, "Address1", null, null, null);

        given(ordersService.findOrderById(1)).willReturn(Mono.just(order));

        webTestClient.get().uri("/orders/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getData().equals(order);
                });
    }

    @Test
    void testDeleteOrder() {
        //     private Integer id;
        //    private String username;
        //    private String status;
        //    private Double totalPrice;
        //    private String address;
        //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        //    private Date createTime;
        //
        //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        //    private Date updateTime;
        given(ordersService.updateOrder(new Orders(1, "user1", "Pending", 100.0, "Address1", null, null, null))).willReturn(Mono.just(true));

        webTestClient.delete().uri("/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("成功删除订单");
                });
    }
}
