package com.BlogProject.service;

import com.BlogProject.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Type getType(Type type);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listAllType();

    List<Type> topTypeList(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);
}
