package com.luv2code.springboot.cruddemo.mappers;

import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.dto.AccountBasicData;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {
    AccountBasicData toAccountBasicData(Account account);
}
