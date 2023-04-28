package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.Organization;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.NotificationRepository;
import com.example.onlineshop.Repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NotificationRepository notificationRepository;

    private final OrganizationRepository organizationRepository;

    public ResponseEntity<?> allNotificationsForUser(Long id) {
        return new ResponseEntity<>(notificationRepository.findAllByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<?> registerOrganization(Organization organization) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        organization.setUser(user);
        organizationRepository.save(organization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
