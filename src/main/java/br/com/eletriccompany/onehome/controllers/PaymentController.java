package br.com.eletriccompany.onehome.controllers;

import br.com.eletriccompany.onehome.domain.dto.requests.PaymentPayRequest;
import br.com.eletriccompany.onehome.domain.enums.Role;
import br.com.eletriccompany.onehome.domain.services.interfaces.CustomerService;
import br.com.eletriccompany.onehome.domain.services.interfaces.PaymentService;
import br.com.eletriccompany.onehome.domain.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final CustomerService customerService;

    public PaymentController(PaymentService paymentService,
                             UserService userService,
                             CustomerService customerService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> GetCustomerPayments(@AuthenticationPrincipal String loggedUser) {
        try {
            if (!userService.mustBe(loggedUser, Role.Customer))
                throw new AccessDeniedException("Você não tem permissão para ver as faturas");

            var user = userService.findByEmail(loggedUser);
            if (user == null)
                throw new IllegalArgumentException("Usuário não encontrado");
            var customer = customerService.findByUserId(user.getId());
            if (customer == null)
                throw new IllegalArgumentException("Cliente não encontrado");

            return ResponseEntity.ok(paymentService.findAllByCustomerIdOrderByDueDateAsc(customer.getId()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> MakePaymentPay(@AuthenticationPrincipal String loggedUser,
                                            @RequestBody PaymentPayRequest request) {
        if (!userService.haveProfileGrantedOrEqual(loggedUser, Role.Operator))
            throw new AccessDeniedException("Você deve ser operador para baixar faturas");

        paymentService.informPaymentPay(request.getId());
        return ResponseEntity.ok("Pagamento informado com sucesso");
    }
}
