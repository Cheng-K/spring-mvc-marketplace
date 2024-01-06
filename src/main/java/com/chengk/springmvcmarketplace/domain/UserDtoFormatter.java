package com.chengk.springmvcmarketplace.domain;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDtoFormatter implements Formatter<UserDto> {

    @Override
    public String print(UserDto object, Locale locale) {
        return object.toString();
    }

    @Override
    public UserDto parse(String text, Locale locale) throws ParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        UserDto parsed = null;
        try {
            parsed = mapper.readValue(text, UserDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return parsed;
    }

}
