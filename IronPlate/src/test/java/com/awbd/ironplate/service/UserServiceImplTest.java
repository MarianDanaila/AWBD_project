package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.UserRepository;
import com.awbd.ironplate.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("athlete");
        user.setEmail("athlete@ironplate.com");
        user.setPassword("encoded_password");
        user.setRole(User.Role.ATHLETE);
    }

    @Test
    void save_shouldReturnSavedUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertThat(result).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertThat(result.getUsername()).isEqualTo("athlete");
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findByUsername_shouldReturnOptional() {
        when(userRepository.findByUsername("athlete")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("athlete");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("athlete@ironplate.com");
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenExists() {
        when(userRepository.existsByUsername("athlete")).thenReturn(true);

        assertThat(userService.existsByUsername("athlete")).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenExists() {
        when(userRepository.existsByEmail("athlete@ironplate.com")).thenReturn(true);

        assertThat(userService.existsByEmail("athlete@ironplate.com")).isTrue();
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenExists() {
        when(userRepository.findByUsername("athlete")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("athlete");

        assertThat(details.getUsername()).isEqualTo("athlete");
        assertThat(details.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_ATHLETE"));
    }

    @Test
    void loadUserByUsername_shouldThrow_whenNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
