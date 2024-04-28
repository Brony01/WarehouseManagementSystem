package com.java.warehousemanagementsystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private List<Item> items;
    private User user;
    private String status;
    private Double totalPrice;
    private String address;
    private Date createTime;
    private Date updateTime;
}
