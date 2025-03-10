package com.example.im.service.impl;

import com.example.im.dao.FriendDao;
import com.example.im.entity.Friend;
import com.example.im.entity.Label;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.UserException;
import com.example.im.service.FriendService;
import com.example.im.service.LabelService;
import com.example.im.service.UserService;
import com.example.im.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 9:22 下午
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService {
    @Autowired
    private LabelService labelService;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendDao friendDao;
    @Override
    @Transactional(rollbackFor = {UserException.class})
    public Friend add(Friend friend) {
        if (userService.isExists(friend.getUserId()) && userService.isExists(friend.getFriendId())){
            friend.setId(KeyUtil.getUniqueKey());
            return friendDao.save(friend);
        } else {
            log.error("【添加好友】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public List<Friend> save(List<Friend> friendList) {
        friendList.forEach(o -> o.setId(KeyUtil.getUniqueKey()));
        return friendDao.saveAll(friendList);
    }

    @Override
    @Transactional(rollbackFor = {UserException.class, FriendException.class})
    public void remove(String userId, String friendId) {
        if (userService.isExists(userId) && userService.isExists(friendId)){
            Friend friend = findOne(userId, friendId);
            friendDao.deleteById(friend.getId());
        } else {
            log.error("【删除好友】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public Friend findOne(String userId, String friendId) {
        Friend friend = friendDao.find(userId, friendId);
        if (friend == null) {
            log.error("【查找好友】该好友不存在");
            throw new FriendException(ErrorCode.FRIEND_NOT_EXISTS);
        }
        return friend;
    }

    @Override
    public boolean isFriend(String userId, String friendId) {
        return friendDao.find(userId, friendId) != null;
    }

    @Override
    @Transactional(rollbackFor = {UserException.class, FriendException.class})
    public Friend blacklist(String userId, String friendId) {
        if (userService.isExists(userId) && userService.isExists(friendId)){
            Friend friend = findOne(userId, friendId);
            if (userId.equals(friend.getUserId())){
                if (friend.getIsFriendBlacklisted()){
                    log.error("【拉黑好友】该好友已拉黑");
                    throw new FriendException(ErrorCode.FRIEND_ALREADY_BLACKLISTED);
                }
                friend.setIsFriendBlacklisted(true);
            } else {
                if (friend.getIsUserBlacklisted()){
                    log.error("【拉黑好友】该好友已拉黑");
                    throw new FriendException(ErrorCode.FRIEND_ALREADY_BLACKLISTED);
                }
                friend.setIsUserBlacklisted(true);
            }
            return friendDao.save(friend);
        } else {
            log.error("【拉黑好友】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }
    @Override
    @Transactional(rollbackFor = {UserException.class, FriendException.class})
    public Friend unblacklist(String userId, String friendId) {
        if (userService.isExists(userId) && userService.isExists(friendId)){
            Friend friend = findOne(userId, friendId);
            if (userId.equals(friend.getUserId())){
                if (!friend.getIsFriendBlacklisted()){
                    log.error("【解除拉黑好友】该好友已解除拉黑");
                    throw new FriendException(ErrorCode.FRIEND_ALREADY_UNBLACKLISTED);
                }
                friend.setIsFriendBlacklisted(false);
            } else {
                if (!friend.getIsUserBlacklisted()){
                    log.error("【解除拉黑好友】该好友已解除拉黑");
                    throw new FriendException(ErrorCode.FRIEND_ALREADY_UNBLACKLISTED);
                }
                friend.setIsUserBlacklisted(false);
            }
            return friendDao.save(friend);
        } else {
            log.error("【解除拉黑好友】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public Friend move(String userId, String friendId, Integer labelId) {
        if (userService.isExists(userId) && userService.isExists(friendId)){
            Friend friend = findOne(userId, friendId);
            if (labelId == null) {
                if (userId.equals(friend.getUserId())){
                    if (friend.getFLabelId() == null){
                        log.error("【改变好友所在分组】移动前后分组不能相同");
                        throw new FriendException(ErrorCode.LABEL_MUST_DIFF);
                    }
                    friend.setFLabelId(labelId);
                } else {
                    if (friend.getULabelId() == null){
                        log.error("【改变好友所在分组】移动前后分组不能相同");
                        throw new FriendException(ErrorCode.LABEL_MUST_DIFF);
                    }
                    friend.setULabelId(labelId);
                }
            } else {
                Label label = labelService.findById(labelId);
                if (!userId.equals(label.getUserId())) {
                    log.error("【改变好友所在分组】分组不存在");
                    throw new FriendException(ErrorCode.LABEL_NOT_EXISTS);
                }
                if (userId.equals(friend.getUserId())){
                    if (labelId.equals(friend.getFLabelId())){
                        log.error("【改变好友所在分组】移动前后分组不能相同");
                        throw new FriendException(ErrorCode.LABEL_MUST_DIFF);
                    }
                    friend.setFLabelId(labelId);
                } else {
                    if (labelId.equals(friend.getULabelId())){
                        log.error("【改变好友所在分组】移动前后分组不能相同");
                        throw new FriendException(ErrorCode.LABEL_MUST_DIFF);
                    }
                    friend.setULabelId(labelId);
                }
            }
            return friendDao.save(friend);
        } else {
            log.error("【改变好友所在分组】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public Friend remark(String userId, String friendId, String remark) {
        if (userService.isExists(userId) && userService.isExists(friendId)){
            Friend friend = findOne(userId, friendId);
            if (userId.equals(friend.getUserId())){
                if (remark.equals(friend.getFRemark())){
                    log.error("【修改好友备注】修改前后备注不能相同");
                    throw new FriendException(ErrorCode.REMARK_NOT_SAME);
                }
                friend.setFRemark(remark);
            } else {
                if (remark.equals(friend.getURemark())){
                    log.error("【修改好友备注】修改前后备注不能相同");
                    throw new FriendException(ErrorCode.REMARK_NOT_SAME);
                }
                friend.setURemark(remark);
            }
            return friendDao.save(friend);
        } else {
            log.error("【修改好友备注】该用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public List<Friend> findAll(String userId) {
        return friendDao.findAll(userId);
    }

}
