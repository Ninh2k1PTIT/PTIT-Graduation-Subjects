package com.example.socialnetwork.controller;

import com.example.socialnetwork.converter.UserConverter;
import com.example.socialnetwork.dto.JwtDto;
import com.example.socialnetwork.dto.request.GoogleLoginRequest;
import com.example.socialnetwork.dto.request.LoginRequest;
import com.example.socialnetwork.dto.request.SignupRequest;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.dto.response.MessageResponse;
import com.example.socialnetwork.exception.BadRequestException;
import com.example.socialnetwork.model.EProvider;
import com.example.socialnetwork.model.ERole;
import com.example.socialnetwork.model.Role;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.RoleRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.security.JwtUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";// cấu trúc 1 email thông thường
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private UserConverter userConverter;
    private JwtUtils jwtUtils;
    @Value("${google.clientId}")
    private String googleClientId;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, UserConverter userConverter) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userConverter = userConverter;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            JwtDto jwtDto = new JwtDto();
            jwtDto.setAccessToken(jwt);
            return ResponseEntity.ok(new BaseResponse<JwtDto>(jwtDto, true, null, null));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(new BaseResponse<String>(null, false, "Email hoặc mật khẩu chưa chính xác!", e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (!Pattern.compile(EMAIL_PATTERN).matcher(signUpRequest.getEmail()).matches())
            throw new BadRequestException("Email không hợp lệ");


        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new BadRequestException("Email đã tồn tại");

        User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getUsername(), EProvider.LOCAL);
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setAvatar("https://www.facebook.com/images/fb_icon_325x325.png");
        userRepository.save(user);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/googleSignIn")
    public ResponseEntity<?> registerUserGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) throws GeneralSecurityException, IOException {
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(googleLoginRequest.getCredential());
        if (googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            if (!userRepository.existsByEmail(payload.getEmail())) {
                User user = new User(payload.getEmail(), encoder.encode(payload.getSubject()), payload.get("name").toString(), EProvider.GOOGLE);
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
                user.setRoles(roles);
                user.setAvatar("https://www.facebook.com/images/fb_icon_325x325.png");
                userRepository.save(user);
            } else if (userRepository.findByEmail(payload.getEmail()).get().getProvider() != EProvider.GOOGLE)
                return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã tồn tại"));

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(payload.getEmail());
            loginRequest.setPassword(payload.getSubject());

            return authenticateUser(loginRequest);
        } else return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản không tồn tại"));
    }
}
