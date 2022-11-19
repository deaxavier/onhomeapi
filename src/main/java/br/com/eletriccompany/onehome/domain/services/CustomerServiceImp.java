package br.com.eletriccompany.onehome.domain.services;

import br.com.eletriccompany.onehome.domain.dto.requests.CreateCustomerFullRequest;
import br.com.eletriccompany.onehome.domain.dto.requests.CustomerUpdateRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.CustomerResponse;
import br.com.eletriccompany.onehome.domain.entities.*;
import br.com.eletriccompany.onehome.domain.services.interfaces.CustomerService;
import br.com.eletriccompany.onehome.infra.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final ClockRepository clockRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder encoder;

    public CustomerServiceImp(CustomerRepository customerRepository,
                              CustomerTypeRepository customerTypeRepository,
                              ClockRepository clockRepository,
                              UserRepository userRepository,
                              ProfileRepository profileRepository,
                              PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.customerTypeRepository = customerTypeRepository;
        this.clockRepository = clockRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.encoder = encoder;
    }

    @Override
    public CustomerResponse findByUserId(UUID id) {
        return customerRepository.findByUserId(id).map(this::_entityToDto).orElse(null);
    }

    @Override
    public CustomerResponse findById(UUID id) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null)
            throw new IllegalArgumentException("Cliente não encontrado");
        return _entityToDto(customer);
    }

    @Override
    public CustomerResponse Update(CustomerUpdateRequest requestDto, String loggedUser) {
        var customer
                = customerRepository.findById(requestDto.getId())
                .orElse(null);

        if (customer == null)
            throw new IllegalArgumentException("Cliente não encontrado");

        var user
                = userRepository.findByEmail(loggedUser)
                .orElse(null);


        if (user == null)
            throw new IllegalArgumentException("Usuario não encontrado");

        if (user.getId() != customer.getUser().getId())
            throw new IllegalArgumentException("Usuario não encontrado");

        customer.setCity(requestDto.getCity());
        customer.setAddress(requestDto.getAddress());
        customer.setState(requestDto.getState());
        customer.setZipcode(requestDto.getZipcode());
        customer.setName(requestDto.getName());
        var customerSaved = customerRepository.save(customer);

        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        userRepository.save(user);

        return _entityToDto(customerSaved);
    }

    @Override
    public Stream<CustomerResponse> getAll() {
        return customerRepository.findAll().stream().map(this::_entityToDto);
    }

    @Override
    public void Create(CreateCustomerFullRequest request) {
        var profile = profileRepository.findById(100).orElse(new ProfileEntity());
        var customerType = customerTypeRepository.findById(request.getCustomer_type_id()).orElse(new CustomerTypeEntity());

        var user = new UserEntity();
        user.setActive(true);
        user.setName(request.getName());
        user.setProfile(profile);
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        var userSaved = userRepository.save(user);

        var clock = new ClockEntity();
        clock.setActive(true);
        clock.setUser(userSaved);
        clock.setState(request.getUf());
        clock.setCity(request.getCity());
        clock.setAddress(request.getAdrees());
        clock.setZipcode(request.getZipcode());
        var clockSaved = clockRepository.save(clock);


        var customer = new CustomerEntity();
        customer.setZipcode(request.getZipcode());
        customer.setUser(userSaved);
        customer.setClock(clockSaved);
        customer.setCustomerType(customerType);
        customer.setActive(true);
        customer.setName(request.getName());
        customer.setState(request.getUf());
        customer.setCity(request.getCity());
        customer.setZipcode(request.getZipcode());
        customer.setAddress(request.getAdrees());
        customerRepository.save(customer);
    }

    @Override
    public void DisableEnable(UUID CustomerId) {
        var customer = customerRepository.findById(CustomerId).orElse(null);
        if (customer == null)
            throw new IllegalArgumentException();
        var user = userRepository.findById((customer.getUser().getId())).orElse(null);
        if (user == null)
            throw new IllegalArgumentException();
        customer.setActive(!customer.isActive());
        user.setActive((!user.isActive()));
        customerRepository.save(customer);
        userRepository.save(user);
    }

    private CustomerResponse _entityToDto(CustomerEntity customer) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
