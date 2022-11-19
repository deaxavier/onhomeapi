package br.com.eletriccompany.onehome.controllers;

import br.com.eletriccompany.onehome.domain.dto.requests.UserCreateRequest;
import br.com.eletriccompany.onehome.domain.enums.Role;
import br.com.eletriccompany.onehome.domain.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/info")
    public ResponseEntity<?> GetInfo(@AuthenticationPrincipal String loggedUser) {
        try {
            var user = userService.findByEmail(loggedUser);
            if (user == null)
                throw new IllegalArgumentException("Usuário não encontrado");

            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> Create(@Valid @RequestBody UserCreateRequest request,
                                    @AuthenticationPrincipal String loggedUser) {
        try {
            if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
                throw new AuthorizationServiceException("Você não possue permissão para usar essa função");
            var user = userService.findByEmail(loggedUser);
            if (user.getProfile().getId() < request.getProfile_id())
                throw new AccessDeniedException("Você não possue permissão para criar um usuário com esse perfil");
            request.setPassword(encoder.encode(request.getPassword()));
            request.setActive(true);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (AuthorizationServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
