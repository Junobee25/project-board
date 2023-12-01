package com.fastcampus.projectboardstart.repository;

import com.fastcampus.projectboardstart.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
