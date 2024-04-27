package com.java.warehousemanagementsystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse
{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String manager;
    private String description;
    private Date createTime;

}
