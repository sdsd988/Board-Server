package com.fastcampus.boardserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {

    public enum  Status{
        DEFAULT, ADMIN, DELETED
    }

    private int id;
    private String userId;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private boolean isWithDraw;
    private Date createTime;
    private Date updateTime;
    private Status status;

    public UserDTO() {
    }

    public UserDTO(int id, String userId, String password, String nickName, boolean isAdmin, boolean isWithDraw, Date createTime, Date updateTime, Status status) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
        this.isAdmin = isAdmin;
        this.isWithDraw = isWithDraw;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getUserId() == null || userDTO.getPassword() == null || userDTO.getNickName() == null;
    }

}
