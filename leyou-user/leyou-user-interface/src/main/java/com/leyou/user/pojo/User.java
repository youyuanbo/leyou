package com.leyou.user.pojo;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class User {

    private Long id;

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 8,max = 32, message = "用户名必须在8到32位之间")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8,max = 32, message = "密码必须在8到32位之间")
    private String password;

    @Pattern(regexp = "/^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/\n", message = "手机号不正确")
    private String phone;

    private Date created;

    @JsonIgnore
    private String salt;

}