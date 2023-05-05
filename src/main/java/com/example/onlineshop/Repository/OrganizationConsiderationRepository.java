package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Organization;
import com.example.onlineshop.Entity.OrganizationConsideration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationConsiderationRepository extends CrudRepository<OrganizationConsideration, Long> {
    OrganizationConsideration findByName(String name);
}
