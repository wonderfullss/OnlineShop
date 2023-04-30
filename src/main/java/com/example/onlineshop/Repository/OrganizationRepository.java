package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    Organization findByName(String name);

    Organization findOrganizationById(Long id);
}
