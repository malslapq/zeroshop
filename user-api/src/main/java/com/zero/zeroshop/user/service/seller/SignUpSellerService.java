package com.zero.zeroshop.user.service.seller;

import com.zero.zeroshop.user.domain.SignUpForm;
import com.zero.zeroshop.user.domain.model.Seller;
import com.zero.zeroshop.user.domain.repository.SellerRepository;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.exception.SellerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpSellerService {

    private final SellerRepository sellerRepository;

    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    public boolean isEmailExist(String email) {
        return sellerRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(
                () -> new SellerException(ErrorCode.NOT_FOUND_USER));
        if (seller.isVerify()) {
            throw new SellerException(ErrorCode.ALREADY_VERIFY);
        }
        if (!seller.getVerificationCode().equals(code)) {
            throw new SellerException(ErrorCode.WRONG_VERIFICATION);
        }
        if (seller.getVerifyExpireAt().isBefore(LocalDateTime.now())) {
            throw new SellerException(ErrorCode.EXPIRE_CODE);
        }
        seller.verificationCompleted();
    }

    @Transactional
    public void ChangeSellerValidateEmail(Long sellerId, String verificationCode) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(
                () -> new SellerException(ErrorCode.NOT_FOUND_USER));
        seller.changeVerifications(verificationCode);
    }

}
