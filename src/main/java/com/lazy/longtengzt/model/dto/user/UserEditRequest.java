package com.lazy.longtengzt.model.dto.user;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: longtengzhitu-backend
 * @description: 用户编辑资料请求
 * @author: Lazy
 * @create: 2025-06-29 16:26
 **/
@Data
public class UserEditRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年级
     */
    private String grade;

    /**
     * 工作经验
     */
    private String workExperience;

    /**
     * 擅长方向
     */
    private String expertiseDirection;

}