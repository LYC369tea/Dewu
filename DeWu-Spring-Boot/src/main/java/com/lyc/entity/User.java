package com.lyc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@Data
@AllArgsConstructor
@TableName("users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;
    @JsonIgnore
    private String password;

    private String email;

    private LocalDateTime registrationTime;

    private Boolean status;

    private Boolean userType;

    private String avatarUrl;

    private String appKey;

    private String secret;

}
