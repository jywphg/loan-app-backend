package cyou.oxling.loanappbackend.dao;

import cyou.oxling.loanappbackend.model.user.UserCredit;
import cyou.oxling.loanappbackend.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问接口
 */
@Mapper
public interface UserDao {
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo findByPhone(@Param("phone") String phone);
    
    /**
     * 保存用户信息
     * @param userInfo 用户信息
     * @return 影响行数
     */
    int saveUserInfo(UserInfo userInfo);
    
    /**
     * 保存用户信用信息
     * @param userCredit 用户信用信息
     * @return 影响行数
     */
    int saveUserCredit(UserCredit userCredit);
    
    /**
     * 根据第三方openId查询用户
     * @param openId 第三方openId
     * @param type 第三方类型
     * @return 用户信息
     */
    UserInfo findByOpenId(@Param("openId") String openId, @Param("type") String type);
    
    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     * @return 影响行数
     */
    int updateLastLoginTime(@Param("userId") Long userId);
} 