package com.offliner.rust.rust_offliner.interfaces;

import java.util.List;
import java.util.Optional;

public interface IDataAccessObject<T> {
    public boolean add(T t);
    public T update(T t);
    public boolean delete(T t);
    public Optional<T> get(long id);
    public List<T> getAll();
}
