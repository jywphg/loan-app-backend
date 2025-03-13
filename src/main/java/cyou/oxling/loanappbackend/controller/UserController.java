package cyou.oxling.loanappbackend.controller;

import cyou.oxling.loanappbackend.dto.common.Result;
import cyou.oxling.loanappbackend.dto.user.LoginDTO;
import cyou.oxling.loanappbackend.dto.user.RegisterDTO;
import cyou.oxling.loanappbackend.dto.user.ThirdPartyLoginDTO;
import cyou.oxling.loanappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证与登录控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO registerDTO) {
        Long userId = userService.register(registerDTO);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        
        return Result.success("注册成功", data);
    }

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> loginResult = userService.login(loginDTO);
        return Result.success("登录成功", loginResult);
    }

    /**
     * 第三方登录
     * @param thirdPartyLoginDTO 第三方登录信息
     * @return 登录结果
     */
    @PostMapping("/third-party-login")
    public Result<?> thirdPartyLogin(@RequestBody ThirdPartyLoginDTO thirdPartyLoginDTO) {
        Map<String, Object> loginResult = userService.thirdPartyLogin(thirdPartyLoginDTO);
        return Result.success("登录成功", loginResult);
    }
} 