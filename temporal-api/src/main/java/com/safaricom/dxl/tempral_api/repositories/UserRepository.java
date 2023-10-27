package com.safaricom.dxl.tempral_api.repositories;


import com.safaricom.dxl.tempral_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
