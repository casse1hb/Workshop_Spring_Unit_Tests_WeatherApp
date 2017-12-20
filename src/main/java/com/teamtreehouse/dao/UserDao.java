
package com.teamtreehouse.dao;

import com.teamtreehouse.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// This is an interface extending the
// CrudRepository Interface.
//only interfaces in this package.
    //Because Spring data is going to
    // generate the implementations of these
    // interfaces when application is booted
    // up.

@Repository
public interface UserDao extends CrudRepository<User,Long> {
    User findByUsername(String username);
}