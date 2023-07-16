package com.laitravel.laitravelbe.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.laitravel.laitravelbe.db.UserRepository;
import com.laitravel.laitravelbe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationHours}")
    private int jwtExpirationHours;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // TODO
    public String registerUser(String userID, String displayName, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userID, displayName, encodedPassword, "");
        userRepository.save(user.toUserEntity());
        return generateJwtToken(userID);
    }

    private String generateJwtToken(String userID) {
        LocalDateTime expirationDateTime = LocalDateTime.now().plusHours(jwtExpirationHours);
        Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withSubject(userID)
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }
}




