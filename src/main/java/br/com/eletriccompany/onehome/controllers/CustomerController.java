package br.com.eletriccompany.onehome.controllers;

import br.com.eletriccompany.onehome.domain.dto.requests.CreateCustomerFullRequest;
import br.com.eletriccompany.onehome.domain.dto.requests.CustomerUpdateRequest;
import br.com.eletriccompany.onehome.domain.enums.Role;
import br.com.eletriccompany.onehome.domain.services.interfaces.CustomerService;
import br.com.eletriccompany.onehome.domain.services.interfaces.PaymentService;
import br.com.eletriccompany.onehome.domain.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;
    private final PaymentService paymentService;

    public CustomerController(CustomerService customerService,
                              UserService userService,
                              PaymentService paymentService) {
        this.customerService = customerService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity<?> GetAll(@AuthenticationPrincipal String loggedUser) {
        try {
            if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
                throw new AccessDeniedException("Você deve ser operador para ver lista de clientes");
            var customers = customerService.getAll();
            return ResponseEntity.ok(customers);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> GetCustomerInfo(@AuthenticationPrincipal String loggedUser) {
        try {
            var user = userService.findByEmail(loggedUser);
            if (user == null)
                throw new IllegalArgumentException("Usuário não encontrado");

            return ResponseEntity.ok(customerService.findByUserId(user.getId()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> Update(@RequestBody CustomerUpdateRequest request,
                                    @AuthenticationPrincipal String loggedUser) {
        try {
            var customerResponse = customerService.Update(request, loggedUser);
            return ResponseEntity.ok(customerResponse);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("{id}/payments")
    public ResponseEntity<?> GetPaymentsForACustomer(@AuthenticationPrincipal String loggedUser,
                                                     @PathVariable("id") UUID id) {
        try {
            if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
                throw new AccessDeniedException("Você deve ser operador para ver faturas de clientes");

            var customer = customerService.findById(id);
            return ResponseEntity.ok(paymentService.findAllByCustomerIdOrderByDueDateDesc(customer.getId()));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping("/changeactived/{id}")
    public ResponseEntity<?> ChangeActived(@AuthenticationPrincipal String loggedUser,
                                           @PathVariable("id") UUID id) {
        try {
            if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
                throw new AccessDeniedException("Você deve ser operador para alterar status de clientes");

            customerService.DisableEnable(id);
            return ResponseEntity.ok("Cliente alterado com sucesso");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @PostMapping()
    public ResponseEntity<?> Create(@AuthenticationPrincipal String loggedUser,
                                    @RequestBody CreateCustomerFullRequest request) {
        try {
            if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
                throw new AccessDeniedException("Você deve ser operador para criar clientes");

            customerService.Create(request);
            return ResponseEntity.ok("Cliente criado com sucesso");

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
