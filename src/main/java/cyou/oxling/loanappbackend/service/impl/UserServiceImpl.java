package cyou.oxling.loanappbackend.service.impl;

import cyou.oxling.loanappbackend.dao.UserDao;
import cyou.oxling.loanappbackend.dto.user.LoginDTO;
import cyou.oxling.loanappbackend.dto.user.RegisterDTO;
import cyou.oxling.loanappbackend.dto.user.ThirdPartyLoginDTO;
import cyou.oxling.loanappbackend.model.user.UserCredit;
import cyou.oxling.loanappbackend.model.user.UserInfo;
import cyou.oxling.loanappbackend.service.UserService;
import cyou.oxling.loanappbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterDTO registerDTO) {
        // 1. 验证验证码
        validateCaptcha(registerDTO.getCaptcha());
        
        // 2. 验证短信验证码
        validateSmsCaptcha(registerDTO.getPhone(), registerDTO.getSmsCaptcha());
        
        // 3. 检查手机号是否已注册
        UserInfo existUser = userDao.findByPhone(registerDTO.getPhone());
        if (existUser != null) {
            throw new RuntimeException("该手机号已注册");
        }
        
        // 4. 创建用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(registerDTO.getPhone());
        // 密码加密存储
        userInfo.setPassword(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes()));
        userInfo.setStatus(1); // 正常状态
        userInfo.setDeleted(0); // 未删除
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        
        // 5. 保存用户信息
        userDao.saveUserInfo(userInfo);
        
        // 6. 创建用户信用信息
        UserCredit userCredit = new UserCredit();
        userCredit.setUserId(userInfo.getId());
        userCredit.setCreditScore(50); // 初始信用分
        userCredit.setCreditLimit(BigDecimal.ZERO); // 初始额度为0
        userCredit.setUsedCredit(BigDecimal.ZERO);
        userCredit.setAvailableCredit(BigDecimal.ZERO);
        userCredit.setRemark("新用户注册");
        userCredit.setCreateTime(new Date());
        userCredit.setUpdateTime(new Date());
        
        // 7. 保存用户信用信息
        userDao.saveUserCredit(userCredit);
        
        return userInfo.getId();
    }

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果（包含token和userId）
     */
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        // 1. 根据手机号查询用户
        UserInfo userInfo = userDao.findByPhone(loginDTO.getPhone());
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 验证用户状态
        if (userInfo.getStatus() != 1) {
            throw new RuntimeException("账号状态异常");
        }
        
        // 3. 根据登录类型验证密码或短信验证码
        if ("password".equals(loginDTO.getLoginType())) {
            // 密码登录
            String encryptedPassword = DigestUtils.md5DigestAsHex(loginDTO.getPasswordOrSmsCode().getBytes());
            if (!encryptedPassword.equals(userInfo.getPassword())) {
                throw new RuntimeException("密码错误");
            }
        } else if ("sms".equals(loginDTO.getLoginType())) {
            // 短信验证码登录
            validateSmsCaptcha(loginDTO.getPhone(), loginDTO.getPasswordOrSmsCode());
        } else {
            throw new RuntimeException("不支持的登录类型");
        }
        
        // 4. 更新最后登录时间
        userDao.updateLastLoginTime(userInfo.getId());
        
        // 5. 生成JWT token
        String token = jwtUtil.generateToken(userInfo.getId());
        
        // 6. 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", userInfo.getId());
        
        return result;
    }

    /**
     * 第三方登录
     * @param thirdPartyLoginDTO 第三方登录信息
     * @return 登录结果（包含token和userId）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> thirdPartyLogin(ThirdPartyLoginDTO thirdPartyLoginDTO) {
        // 1. 验证第三方token有效性（实际项目中需调用第三方API验证）
        validateThirdPartyToken(thirdPartyLoginDTO.getOpenId(), thirdPartyLoginDTO.getAccessToken(), thirdPartyLoginDTO.getType());
        
        // 2. 查询是否已有绑定用户
        UserInfo userInfo = userDao.findByOpenId(thirdPartyLoginDTO.getOpenId(), thirdPartyLoginDTO.getType());
        
        // 3. 如果没有绑定用户，需要创建新用户（实际项目中可能需要先绑定手机号）
        if (userInfo == null) {
            // 创建新用户（此处简化处理，实际项目中可能需要先引导用户绑定手机号）
            userInfo = new UserInfo();
            userInfo.setPhone(""); // 第三方登录可能没有手机号，需要后续绑定
            userInfo.setStatus(1); // 正常状态
            userInfo.setDeleted(0); // 未删除
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());
            
            // 保存用户信息
            userDao.saveUserInfo(userInfo);
            
            // 创建用户信用信息
            UserCredit userCredit = new UserCredit();
            userCredit.setUserId(userInfo.getId());
            userCredit.setCreditScore(50); // 初始信用分
            userCredit.setCreditLimit(BigDecimal.ZERO); // 初始额度为0
            userCredit.setUsedCredit(BigDecimal.ZERO);
            userCredit.setAvailableCredit(BigDecimal.ZERO);
            userCredit.setRemark("第三方账号注册");
            userCredit.setCreateTime(new Date());
            userCredit.setUpdateTime(new Date());
            
            // 保存用户信用信息
            userDao.saveUserCredit(userCredit);
        }
        
        // 4. 更新最后登录时间
        userDao.updateLastLoginTime(userInfo.getId());
        
        // 5. 生成JWT token
        String token = jwtUtil.generateToken(userInfo.getId());
        
        // 6. 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", userInfo.getId());
        
        return result;
    }
    
    /**
     * 验证图形验证码
     * @param captcha 验证码
     */
    private void validateCaptcha(String captcha) {
        // 实际项目中需要从缓存中获取验证码并比对
        // 此处简化处理
        if (!"XXXX".equals(captcha)) {
            throw new RuntimeException("验证码错误");
        }
    }
    
    /**
     * 验证短信验证码
     * @param phone 手机号
     * @param smsCaptcha 短信验证码
     */
    private void validateSmsCaptcha(String phone, String smsCaptcha) {
        // 实际项目中需要从缓存中获取验证码并比对
        // 此处简化处理
        if (!"123456".equals(smsCaptcha)) {
            throw new RuntimeException("短信验证码错误");
        }
    }
    
    /**
     * 验证第三方token有效性
     * @param openId 第三方openId
     * @param accessToken 第三方accessToken
     * @param type 第三方类型
     */
    private void validateThirdPartyToken(String openId, String accessToken, String type) {
        // 实际项目中需要调用第三方API验证token有效性
        // 此处简化处理，假设token有效
    }
} 