package cn.attackme.muleproject.controller;

import cn.attackme.muleproject.config.JwtTokenUtil;
import cn.attackme.muleproject.dto.UserDTO;
import cn.attackme.muleproject.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody String userData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userData);
            String username = jsonNode.get("username").textValue();
            String password = jsonNode.get("password").textValue();

            if (userService.findByUsername(username)!= null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名已存在");
            }
            // 尝试注册用户
            UserDTO userDTO = userService.convertToDTO(username, password);
            userService.save(userDTO);

            return ResponseEntity.ok().body("注册成功");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少参数："+e.getMessage());
        } catch (Exception e) {
            // 如果出现异常，则返回注册失败的响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String userData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userData);
            String username = jsonNode.get("username").textValue();
            String password = jsonNode.get("password").textValue();

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密码错误");
            }

            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(token);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少参数："+e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册失败: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Hello, World!";
    }
}
