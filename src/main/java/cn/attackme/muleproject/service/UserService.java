package cn.attackme.muleproject.service;

import cn.attackme.muleproject.dto.UserDTO;
import cn.attackme.muleproject.entity.User;
import cn.attackme.muleproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
     UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(UserDTO userDTO) {
        userRepository.save(convertToUser(userDTO));
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public UserDTO convertToDTO(String username, String password){
        UserDTO userDTO = new UserDTO();
        password = passwordEncoder.encode(password);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        return userDTO;
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
