package org.apiitalhrbe.services;

import org.apiitalhrbe.dtos.request.UserRequestDTO;
import org.apiitalhrbe.dtos.response.EmployeeResponseDTO;
import org.apiitalhrbe.dtos.response.TokenResponseDTO;
import org.apiitalhrbe.dtos.response.UserResponseDTO;
import org.apiitalhrbe.entities.nosql.UserHistoryEntity;
import org.apiitalhrbe.entities.sql.EmployeeEntity;
import org.apiitalhrbe.entities.sql.UserEntity;
import org.apiitalhrbe.repositories.nosql.UserHistoryRepository;
import org.apiitalhrbe.repositories.sql.UserRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.apiitalhrbe.utils.TokenGenerator.generateToken;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<UserResponseDTO> getAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(entity -> modelMapper.map(entity, UserResponseDTO.class))
                .toList();
    }

    public UserResponseDTO getByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityByUsername(username);
        if (userEntity.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return modelMapper.map(userEntity.get(), UserResponseDTO.class);
    }

    public UserResponseDTO getByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(email);
        return userEntity.map(entity -> modelMapper.map(entity, UserResponseDTO.class)).orElse(null);
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if(validate(userRequestDTO)) {
            return generateUser(userRequestDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data");
    }

    private UserResponseDTO generateUser(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        EmployeeResponseDTO employee = employeeService.getEmployeeById(userRequestDTO.employeeId());

        userEntity.setEmployee(new EmployeeEntity());
        userEntity.getEmployee().setId(userRequestDTO.employeeId());
        userEntity.setUsername(generateUsername(employee.getFirstName(), employee.getLastName()));
        // TODO: encriptar contraseña
        userEntity.setPassword(userRequestDTO.password());
        userEntity.setIsActive(true);
        userEntity.setRole(userRequestDTO.role());
        userEntity.setEmail(userRequestDTO.email());
        userEntity.setCreatedAt(LocalDate.now());

        UserEntity userEntitySaved = userRepository.save(userEntity);

        return modelMapper.map(userEntitySaved, UserResponseDTO.class);
    }

    private String generateUsername(String firstname, String lastname) {
        StringBuilder usernameBuilder = new StringBuilder();
        for (int k = 1; k <= firstname.length(); k++) {
            usernameBuilder.append(firstname, 0, k);
            String username = usernameBuilder.toString();
            if (username.equals(firstname) && usernameExists(username + "." + lastname)) {
                int counter = 0;
                do {
                    counter++;
                }
                while (usernameExists(username + "." + lastname + counter));
                usernameBuilder.append('.').append(lastname).append(counter);
                break;
            } else if (!usernameExists(username + "." + lastname)) {
                usernameBuilder.append('.').append(lastname);
                break;
            }
            usernameBuilder.setLength(0);
        }
        return usernameBuilder.toString().toLowerCase(Locale.US);
    }

    public UserResponseDTO changePassword(Long id, String password) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (isValidPassword(password) && !isPasswordRepeated(userEntity, password)) {
            userEntity.setPassword(password);
            saveUserHistory(userEntity, "PASSWORD_CHANGE", LocalDate.now(), null);
            UserEntity updatedUserEntity = userRepository.save(userEntity);
            return modelMapper.map(updatedUserEntity, UserResponseDTO.class);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password change is not allowed");
        }
    }

    private void saveUserHistory(UserEntity userEntity, String state, LocalDate from, LocalDate to) {
        UserHistoryEntity userHistoryEntity = new UserHistoryEntity();
        userHistoryEntity.setId(UUID.randomUUID());
        userHistoryEntity.setUserId(userEntity.getId());
        userHistoryEntity.setState(state);
        userHistoryEntity.setFrom(from);
        userHistoryEntity.setTo(to);
        userHistoryRepository.save(userHistoryEntity);
    }

    public TokenResponseDTO generateTokenByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String newToken = generateToken();
        userEntity.setToken(newToken);
        userEntity.setTokenCreationDate(LocalDateTime.now());
        userEntity.setTokenExpirationDate(userEntity.getTokenCreationDate().plusMinutes(5));

        userRepository.save(userEntity);

        boolean checkSend = emailService.sendEmail(email, newToken);

        if (!checkSend) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending email");
        }

        return getTokenResponseDTO(userEntity);
    }

    private TokenResponseDTO getTokenResponseDTO(UserEntity userEntity) {
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setUserId(userEntity.getId());
        tokenResponseDTO.setCreationDate(userEntity.getTokenCreationDate());
        tokenResponseDTO.setExpirationDate(userEntity.getTokenExpirationDate());
        return tokenResponseDTO;
    }

    public boolean validateToken(Long id, String token) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return isTokenValid(userEntity) && userEntity.getToken().equals(token);
    }

    private boolean isTokenValid(UserEntity userEntity) {
        return userEntity.getTokenExpirationDate().isAfter(LocalDateTime.now());
    }

    private Boolean usernameExists(String username) {
        return userRepository.findUserEntityByUsername(username).isPresent();
    }

    private Boolean validate(UserRequestDTO userRequestDTO) {
        if (!isValidRole(userRequestDTO.role())) {
            throw new IllegalArgumentException("Invalid role");
        }
        if (!isValidPassword(userRequestDTO.password())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return true;
    }

    private Boolean isValidRole(String role) {
        return role.equals("USER") || role.equals("ADMIN");
    }

    private Boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-_@#$%^&+=])(?=\\S+$).{8,}$");
    }

    private boolean isPasswordRepeated(UserEntity userEntity, String password) {
        return userEntity.getPassword().equals(password);
    }

}
