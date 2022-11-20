package com.offliner.rust.rust_offliner.interfaces;


import java.util.Collection;
import java.util.stream.Collectors;

//convert from database object @Entity to DTO
//or the other way around
public interface IBaseDTOConverter<F, T> {

    public T convert(F from);

    default public Collection<T> convertAll(Collection<F> fElements){
        Collection<T> convertedElement =
                fElements.stream()
                        .map(element -> convert(element))
                        .collect(Collectors.toList());

        return convertedElement;
    }

}
