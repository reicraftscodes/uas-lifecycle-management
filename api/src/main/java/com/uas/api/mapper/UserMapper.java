package com.uas.api.mapper;

import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /**
     * UserMapper to UserDTO.
     * @param user user details.
     */
    public UserDTO toUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
