package com.fastcampus.boardserver.service.impl;

import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.exception.DuplicatedIdException;
import com.fastcampus.boardserver.mapper.UserProfileMapper;
import com.fastcampus.boardserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.fastcampus.boardserver.utils.SHA256Util.encryptSHA256;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public void register(UserDTO userProfile) {
        boolean dupleIdResult = isDuplicatedId(userProfile.getUserId());
        if (dupleIdResult) {
            throw new DuplicatedIdException("중복된 아이디 입니다.");
        }
        userProfile.setCreateTime(new Date());
        userProfile.setPassword(encryptSHA256(userProfile.getPassword()));

        int insertCount = userProfileMapper.register(userProfile);
        if (insertCount != 1) {
            log.error("insertMember error = {}", userProfile);
            throw new RuntimeException("insertUser ERROR! 회원가입 메서드를 확인해 주세요\n" + "Params : " + userProfile);
        }

    }

    @Override
    public UserDTO login(String id, String password) {
        String encryptedPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByUserIdAndPassword(id, encryptedPassword);

        return memberInfo;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.idCheck(id)==1;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String encryptedPassword = encryptSHA256(afterPassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, afterPassword);

        if (memberInfo != null) {
            memberInfo.setPassword(encryptedPassword);
        } else {
            log.error("updatePassword ERROR! {}", memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String encryptedPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, password);
        if (memberInfo != null) {
            userProfileMapper.deleteUserProfile(id);
        } else {
            log.error("deletedId ERROR! {}", memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }
}
