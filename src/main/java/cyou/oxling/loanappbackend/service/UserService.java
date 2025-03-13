package cyou.oxling.loanappbackend.service;

import cyou.oxling.loanappbackend.dto.user.LoginDTO;
import cyou.oxling.loanappbackend.dto.user.RegisterDTO;
import cyou.oxling.loanappbackend.dto.user.ThirdPartyLoginDTO;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    Long register(RegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果（包含token和userId）
     */
    Map<String, Object> login(LoginDTO loginDTO);
    
    /**
     * 第三方登录
     * @param thirdPartyLoginDTO 第三方登录信息
     * @return 登录结果（包含token和userId）
     */
    Map<String, Object> thirdPartyLogin(ThirdPartyLoginDTO thirdPartyLoginDTO);
} 