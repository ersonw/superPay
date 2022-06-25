package com.example.superpay.repository;

import com.example.superpay.entity.ThirdParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ThirdPartyRepository")
public interface ThirdPartyRepository extends PagingAndSortingRepository<ThirdParty, String> {
    //分页查询
    public Page<ThirdParty> findAll(Pageable pageable);
    public ThirdParty findAllById(String id);
//    public ThirdParty findAllByMchId(String mchId);
    public List<ThirdParty> findAllByMchId(String mchId);
    public List<ThirdParty> findAllByTypeIdAndState(String typeId, int state);
    public Page<ThirdParty> findAllByTypeIdAndState(String typeId, int state, Pageable pageable);
}
