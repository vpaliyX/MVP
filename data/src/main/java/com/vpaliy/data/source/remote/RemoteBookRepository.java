package com.vpaliy.data.source.remote;

import android.support.annotation.NonNull;

import com.vpaliy.data.entity.BookEntity;
import com.vpaliy.data.source.DataSource;
import com.vpaliy.data.specification.Specification;
import java.util.List;

public class RemoteBookRepository implements DataSource<BookEntity,Specification> {



    @Override
    public void add(BookEntity item) {

    }

    @Override
    public void add(Iterable<BookEntity> items) {

    }

    @Override
    public void update(BookEntity item) {

    }

    @Override
    public void update(@NonNull BookEntity item, @NonNull Specification specification) {

    }

    @Override
    public BookEntity get(@NonNull Specification specification) {
        return null;
    }

    @Override
    public List<BookEntity> query(@NonNull Specification specification) {
        return null;
    }
}
