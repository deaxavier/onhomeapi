package br.com.eletriccompany.onehome.controllers;

import br.com.eletriccompany.onehome.domain.dto.requests.ClockEventRequest;
import br.com.eletriccompany.onehome.domain.dto.requests.ResumeClockEventRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.ClockEventResponse;
import br.com.eletriccompany.onehome.domain.dto.responses.ResumeClockEventResponse;
import br.com.eletriccompany.onehome.domain.enums.Role;
import br.com.eletriccompany.onehome.domain.services.interfaces.ClockService;
import br.com.eletriccompany.onehome.domain.services.interfaces.CustomerService;
import br.com.eletriccompany.onehome.domain.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/clock")
public class ClockController {
    private final ClockService clockService;
    private final UserService userService;
    private final CustomerService customerService;

    public ClockController(ClockService clockService,
                           UserService userService,
                           CustomerService customerService) {
        this.clockService = clockService;
        this.userService = userService;
        this.customerService = customerService;
    }

    @PostMapping("events")
    public ResponseEntity<?> GetEvents(@AuthenticationPrincipal String loggedUser,
                                       @RequestBody ClockEventRequest form) {
        try {
            var clockId = getClockIdByEmailOrUserId(loggedUser, form.getUserid());
            return ResponseEntity.ok(clockService.getRegisters(clockId, form.getTo(), form.getFrom()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("events-resume-month")
    public ResponseEntity<?> GetMonthEvents(@AuthenticationPrincipal String loggedUser,
                                            @RequestBody ResumeClockEventRequest form) {
        try {
            var clockId = getClockIdByEmailOrUserId(loggedUser, form.getUserid());
            return ResponseEntity.ok(clockService.getMonthResume(clockId));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("events-resume")
    public ResponseEntity<?> GetResumeEvents(@AuthenticationPrincipal String loggedUser,
                                             @RequestBody ResumeClockEventRequest form) {
        try {
            var clockId = getClockIdByEmailOrUserId(loggedUser, form.getUserid());

            float kwh = 0.00F;
            float cost = 0.00F;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            var from = new Timestamp(calendar.getTimeInMillis());
            var to = new Timestamp(System.currentTimeMillis());

            var items = clockService.getRegisters(clockId, to, from);

            for (ClockEventResponse item : items.toList()) {
                kwh += item.getKwh();
                cost += item.getPartial_cost();
            }
            var response = new ResumeClockEventResponse();
            response.setKwh(kwh);
            response.setPartial_cost(cost);
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("events-resume-seven-days")
    public ResponseEntity<?> GetSevenDaysEvents(@AuthenticationPrincipal String loggedUser,
                                                @RequestBody ResumeClockEventRequest form) {
        try {
            var clockId = getClockIdByEmailOrUserId(loggedUser, form.getUserid());

            return ResponseEntity.ok(clockService.getSevenDaysResume(clockId));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private UUID getClockIdByEmailOrUserId(String email, UUID userId) {

        if (!userService.mustBe(email, Role.Customer))
            throw new AuthorizationServiceException("Você não possue permissão para usar essa função");

        var user = userService.findByEmail(email);
        if (user == null)
            throw new IllegalArgumentException("Usuário não associoado à um relógio");

        var user_Id = user.getId();
        if (userId != null) {
            if (!userId.equals(user_Id)) {
                if (!userService.mustBe(email, Role.Operator))
                    throw new IllegalArgumentException("Você não tem permissão para ver eventos de outros clientes");

                user_Id = userId;
            }
        }

        var customer = customerService.findByUserId(user_Id);

        var clockId = customer.getClockId();
        if (clockId == null)
            throw new IllegalArgumentException("Relógio não cadastrado");

        return clockId;
    }
}
