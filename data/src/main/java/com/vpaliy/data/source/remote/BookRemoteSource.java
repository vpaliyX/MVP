package com.vpaliy.data.source.remote;


import com.vpaliy.data.DataSource;
import com.vpaliy.data.entity.BookEntity;

import java.util.Collection;
import java.util.List;

import rx.Observable;

public class BookRemoteSource extends DataSource<BookEntity> {

    @Override
    public void deleteById(String ID) {

    }

    @Override
    public void delete(BookEntity item) {

    }

    @Override
    public void update(BookEntity item) {

    }

    @Override
    public Observable<List<BookEntity>> getList() {
        return null;
    }

    @Override
    public Observable<BookEntity> findById(String ID) {
        return null;
    }

    @Override
    public void add(BookEntity item) {

    }

    @Override
    public void add(Collection<BookEntity> collection) {

    }

    @Override
    public void clear() {

    }
}