package com.example.superpay.repository;

import com.example.superpay.entity.ShortLink;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository("ShortLinkRepository")
public interface ShortLinkRepository extends PagingAndSortingRepository<ShortLink, String> {
    ShortLink findAllById(String id);
    ShortLink findAllByLink(String link);
}
