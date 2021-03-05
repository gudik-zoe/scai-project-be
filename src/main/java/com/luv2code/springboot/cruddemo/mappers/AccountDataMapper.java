package com.luv2code.springboot.cruddemo.mappers;

import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.dto.AccountData;
import org.mapstruct.Mapper;

@Mapper
public interface AccountDataMapper {

    AccountData toAccountData(Account account);
}
