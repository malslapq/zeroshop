package com.zero.zeroshop.user.service.seller;

import com.zero.zeroshop.user.domain.model.Seller;
import com.zero.zeroshop.user.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmail(email).stream()
                .filter(seller -> seller.getPassword().equals(password) && seller.isVerify())
                .findFirst();
    }
}
