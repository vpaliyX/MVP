package com.vpaliy.data.repository;

import android.support.annotation.NonNull;

import com.vpaliy.data.source.DataSource;
import com.vpaliy.data.source.annotation.Local;
import com.vpaliy.data.source.annotation.Remote;

import com.vpaliy.data.mapper.Mapper;
import com.vpaliy.domain.repository.IRepository;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Potential implementation of given pattern
 * @param <Fake> Entity that is located in data layer
 * @param <Real> Entity that is located in domain layer
 */

@Singleton
public class Repository<Fake,Real> implements IRepository<Real> {

    /**
     * Local data source
     */
    @Local
    private DataSource<Fake> localSource;

    /**
     * Remote data source
     */
    @Remote
    private DataSource<Fake> remoteSource;

    /**
     * Maps a fake entity into a real entity
     */
    private Mapper<Real,Fake> mapper;


    @Inject
    public Repository(@NonNull @Local DataSource<Fake> localSource,
                      @NonNull @Remote DataSource<Fake> remoteSource,
                      @NonNull Mapper<Real,Fake> mapper) {
        this.localSource=localSource;
        this.remoteSource=remoteSource;
        this.mapper=mapper;
    }

    @Override
    public Observable<List<Real>> getList() {
        //TODO set up the remote source
        return //Observable.concat(
                localSource.getList().flatMap(fakes->Observable.from(mapper.map(fakes)).toList());//l,
                //remoteSource.getList().flatMap(fakes->Observable.from(mapper.map(fakes)).toList()));
    }

    @Override
    public Observable<Real> findById(int ID) {
        //TODO set up the remote source
        return// Observable.concat(
                localSource.findById(ID).map(mapper::map);
               // remoteSource.findById(ID).map(mapper::map));
    }

    @Override
    public void update(Real item) {
        localSource.update(mapper.reverseMap(item));
        remoteSource.update(mapper.reverseMap(item));
    }

    @Override
    public void delete(Real item) {
        localSource.delete(mapper.reverseMap(item));
        remoteSource.delete(mapper.reverseMap(item));
    }

    @Override
    public void add(Real item) {
        localSource.add(mapper.reverseMap(item));
        remoteSource.add(mapper.reverseMap(item));
    }

    @Override
    public void deleteById(int ID) {
        localSource.deleteById(ID);
        remoteSource.deleteById(ID);
    }

    @Override
    public void clear() {
        localSource.clear();
        remoteSource.clear();
    }

    @Override
    public void add(Collection<Real> collection) {
        LinkedList<Real> result=new LinkedList<>(collection);
        localSource.add(mapper.reverseMap(result));
        remoteSource.add(mapper.reverseMap(result));
    }
}
