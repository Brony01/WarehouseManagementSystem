package com.java.warehousemanagementsystem.controller;

import com.java.warehousemanagementsystem.pojo.Warehouse;
import com.java.warehousemanagementsystem.service.WarehouseService;
import com.java.warehousemanagementsystem.vo.ResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest(WarehouseController.class)
public class WarehouseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WarehouseService warehouseService;

    @Test
    void testGetAllWarehouses() {
        Warehouse warehouse1 = new Warehouse(1, "Warehouse1", "Location1", "Manager1", "Description1", null);
        Warehouse warehouse2 = new Warehouse(2, "Warehouse2", "Location2", "Manager2", "Description2", null);
        List<Warehouse> warehouses = Arrays.asList(warehouse1, warehouse2);

        given(warehouseService.selectWarehouse("", 1L, 10L)).willReturn(Flux.fromIterable(warehouses));

        webTestClient.get().uri("/warehouses")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Warehouse.class).isEqualTo(warehouses);
    }

    @Test
    void testAddWarehouse() {
        given(warehouseService.addWarehouse("NewWarehouse", "NewLocation", "NewManager", "NewDescription"))
                .willReturn(Mono.just(new Warehouse(1, "NewWarehouse", "NewLocation", "NewManager", "NewDescription", null)));

        webTestClient.post().uri("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new Warehouse(null, "NewWarehouse", "NewLocation", "NewManager", "NewDescription", null)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getData().equals(new Warehouse(1, "NewWarehouse", "NewLocation", "NewManager", "NewDescription", null));
                });
    }

    @Test
    void testUpdateWarehouse() {
        given(warehouseService.updateWarehouse(1, "UpdatedWarehouse", "UpdatedLocation", "UpdatedManager", "UpdatedDescription"))
                .willReturn(Mono.just(new Warehouse(1, "UpdatedWarehouse", "UpdatedLocation", "UpdatedManager", "UpdatedDescription", null)));

        webTestClient.put().uri("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(new Warehouse(1, "UpdatedWarehouse", "UpdatedLocation", "UpdatedManager", "UpdatedDescription", null)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getData().equals(new Warehouse(1, "UpdatedWarehouse", "UpdatedLocation", "UpdatedManager", "UpdatedDescription", null));
                });
    }

    @Test
    void testGetWarehouseById() {
        Warehouse warehouse = new Warehouse(1, "Warehouse1", "Location1", "Manager1", "Description1", null);

        given(warehouseService.selectWarehouseById(1)).willReturn(Mono.just(warehouse));

        webTestClient.get().uri("/warehouses/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getData().equals(warehouse);
                });
    }

    @Test
    void testDeleteWarehouse() {
        given(warehouseService.deleteWarehouse(1)).willReturn(Mono.empty());

        webTestClient.delete().uri("/warehouses/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseResult.class)
                .value(response -> {
                    assert response.getCode() == 200;
                    assert response.getMsg().equals("成功删除仓库");
                });
    }
}
