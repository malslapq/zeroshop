package com.zero.zeroshop.user.application;

import com.zero.zeroshop.user.client.MailgunClient;
import com.zero.zeroshop.user.client.mailgun.SendMailForm;
import com.zero.zeroshop.user.domain.SignUpForm;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.domain.model.Seller;
import com.zero.zeroshop.user.exception.CustomerException;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.exception.SellerException;
import com.zero.zeroshop.user.service.customer.SignUpCustomerService;
import com.zero.zeroshop.user.service.seller.SignUpSellerService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SignUpSellerService signUpSellerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public void sellerVerify(String email, String code) {
        signUpSellerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomerException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Customer customer = signUpCustomerService.signUp(form);
            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("malslapq@gmail.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), code,
                            "customer"))
                    .build();
            Response response = mailgunClient.sendEmail(sendMailForm);
            log.info("mailStatus => "+ response.status());
            signUpCustomerService.ChangeCustomerValidateEmail(customer.getId(), code);
            return "회원가입 성공";
        }
    }

    public String sellerSignUp(SignUpForm form) {
        if (signUpSellerService.isEmailExist(form.getEmail())) {
            throw new SellerException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Seller seller = signUpSellerService.signUp(form);
            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("malslapq@gmail.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), code,
                            "seller"))
                    .build();
            Response response = mailgunClient.sendEmail(sendMailForm);
            log.info("mailStatus => "+ response.status());
            signUpSellerService.ChangeSellerValidateEmail(seller.getId(), code);
            return "회원가입 성공";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code,
                                            String type) {
        StringBuilder sb = new StringBuilder();
        return sb.append("Hello ").append(name).append("! please Click Link for verification")
                .append("http://localhost:8081/signup/")
                .append(type)
                .append("/verify?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
