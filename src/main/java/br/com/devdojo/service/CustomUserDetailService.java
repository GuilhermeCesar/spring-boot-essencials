package br.com.devdojo.service;

import br.com.devdojo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        br.com.devdojo.model.User user = Optional.ofNullable(this.userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authoritiesAdmin =
                AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");

        List<GrantedAuthority> authoritiesListUser =
                AuthorityUtils.createAuthorityList("ROLE_USER");

        return new User(user.getUsername(), user.getPassword(),
                user.isAdmin() ? authoritiesAdmin : authoritiesListUser);
    }
}
