package com.universal_yazilim.bid.ysm.gateway_app.model.repository;

import com.universal_yazilim.bid.ysm.gateway_app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>
{
    /*
        Optional is primarily intended for use as a method return type
        where there is a clear need to represent "no result,"
        and where using null is likely to cause errors.
        A variable whose type is Optional should never itself be null;
        it should always point to an Optional instance.
     */
    // SELECT * FROM USERS WHERE USERNAME = ?;
    Optional<User> findByUsername(String username);
}
