package com.sahil.Ecom.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public class AuditorAwareImpl implements AuditorAware<String> {

    Logger logger  = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<String> getCurrentAuditor() {

        logger.info("-------------INSIDE AUDIT_____________");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.ofNullable("SYSTEM_GENERATED");
        }
        return Optional.ofNullable(authentication.getName());

    }


}
