package com.inssider.api.common.service;

public interface VerifyService {
    default boolean validateId(Long ownerId, Long reqId){
        return ownerId.equals(reqId);
    }
}
