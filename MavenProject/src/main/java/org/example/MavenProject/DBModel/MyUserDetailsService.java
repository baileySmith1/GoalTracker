package org.example.MavenProject.DBModel;


import org.example.MavenProject.DBRepository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo repository;

    @Override
    public UserDetails loadUserByUsername(String usrname) throws UsernameNotFoundException {
        Optional <Users> user = repository.lookupUser(usrname);
        if (user.isPresent()) {
            Users userDetails = user.get();
            return User.builder().username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .roles("USER").build();
        }
        else{
            throw new UsernameNotFoundException(usrname);
        }
    }
}
