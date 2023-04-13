package com.universal_yazilim.bid.ysm.gateway_app.security.model;

import com.universal_yazilim.bid.ysm.gateway_app.model.entity.User;
import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractUserService;
import com.universal_yazilim.bid.ysm.gateway_app.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private AbstractUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = new User();

        try
        {
            // "Bu kullanıcı adındaki kullanıcı yoksa" durumu, orElseThrow() metodu ile yönetildi.
            user = userService.findByUsername(username).orElseThrow(()
                    -> new UsernameNotFoundException("User with " + username + " is not found."));
        }
       catch (UsernameNotFoundException e)
       {
           Util.showGeneralExceptionInfo(e);
       }

        return new UserPrincipal(user.getUserID(), user.getUsername(), user.getPassword());
    }
}
