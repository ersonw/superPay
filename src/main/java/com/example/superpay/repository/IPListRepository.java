package com.example.superpay.repository;

import com.example.superpay.entity.IpList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("IPListRepository")
public interface IPListRepository extends PagingAndSortingRepository<IpList, String> {
    public Page<IpList> findAllByUid(String uid, Pageable pageable);
    public IpList findAllById(String id);
    public void deleteAllByUid(String uid);
}
