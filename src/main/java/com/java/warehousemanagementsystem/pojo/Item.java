package com.java.warehousemanagementsystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private Integer warehouseId;
    private Date createTime;
    private Date updateTime;
}
