/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.controller;

import java.util.NoSuchElementException;
import org.cash.count.dto.AccountCreationDto;
import org.cash.count.service.IAccountManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rafael
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    private final IAccountManager accountManager;
    
    public AccountController(IAccountManager accountManager){
        this.accountManager = accountManager;
    }
    
    @PostMapping
    public ResponseEntity<String> create(AccountCreationDto account){
        try{
            accountManager.create(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(NoSuchElementException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
