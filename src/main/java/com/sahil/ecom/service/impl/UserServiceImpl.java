package com.sahil.ecom.service.impl;

import com.sahil.ecom.dto.customer.FetchCustomerDTO;
import com.sahil.ecom.dto.seller.FetchSellerDTO;
import com.sahil.ecom.entity.*;
import com.sahil.ecom.exception.*;
import com.sahil.ecom.repository.*;
import com.sahil.ecom.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    private com.sahil.ecom.service.impl.UUIDTokenService UUIDTokenService;

    @Autowired
    private ResetPassTokenRepository resetPassTokenRepository;


    @Autowired
    private SellerService sellerService;

    @Autowired
    private MessageSource messageSource;


    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private LoginService loginService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private GeneralMailService generalMailService;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    //customer activation through Activation Link
    @Transactional
    @Override
    public boolean activateByEmail(String email) {

        //if email valid
        //delete activation token
        //update isActive

        if (userRepository.existsByEmail(email)) {
            activationTokenRepository.deleteByUserEmail(email);
            return userRepository.updateIsActive(true, email) > 0;
        }
        throw new UserEmailNotFoundException();

    }


    //admin activating accounts for seller and customer
    @Override
    @Transactional
    public boolean activateAccount(Long id) {

        //check if id exist
        //check if already true then no action return true
        //else change to true

        if (userRepository.existsById(id)) {

            User foundUser = userRepository.findById(id).orElseThrow(IdNotFoundException::new);

            if(foundUser.getRoles().get(0).getAuthority().equals("ROLE_ADMIN")){
                logger.info("ADMIN ACCOUNT CANNOT BE MODIFIED");
                throw new IdNotFoundException();
            }


            if (foundUser.isActive()) {
                logger.info("------------Already Activated------------");
                return true;
            }


            userRepository.updateIsActive(true,foundUser.getEmail());
            logger.info("---------------ACCOUNT ACTIVATED-------------------");
            //send acknowledgement email
            generalMailService.sendAccountActivationAck(foundUser.getEmail());
            return true;

        }
        return false;
    }


    // admin deactivating accounts for customer
    @Override
    @Transactional
    public boolean deActivateAccount(Long id) {

        //check if id exist
        //check if already false then no action return true
        //else change to false

        if (userRepository.existsById(id)) {

            User foundUser = userRepository.findById(id).orElseThrow(IdNotFoundException::new);

            //check for admin account
            if(foundUser.getRoles().get(0).getAuthority().equals("ROLE_ADMIN")){
                logger.info("ADMIN ACCOUNT CANNOT BE MODIFIED");
                throw new IdNotFoundException();

            }

            if (!foundUser.isActive()) {
                logger.info("------------Already Deactivated------------");
                return true;
            }

//            foundUser.setActive(false);
//            userRepository.save(foundUser);
            userRepository.updateIsActive(false,foundUser.getEmail());
            logger.info("---------------ACCOUNT DEACTIVATED-------------------");
            //send acknowledgement email
            generalMailService.sendAccountDeActivationAck(foundUser.getEmail());
            return true;
        }
        return false;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void activationHelper(String email)  {
//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken(token);
        activationToken.setUserEmail(email);

        // setting time limits to the token
        LocalDateTime currentDateTime = LocalDateTime.now();
        activationToken.setTokenTimeLimit(currentDateTime.plusMinutes(1));
//        activationToken.setTokenTimeLimit(currentDateTime.plusHours(3));

        activationTokenRepository.save(activationToken);


        try {
            String url = UUIDTokenService.generateActivationURL(token);
            generalMailService.sendAccountActivationUrlCustomer(email,url);
        } catch (MalformedURLException e) {
            logger.info("URL Error" + e);
            e.printStackTrace();
        }

        //generate url
//        String emailBody = "";
//        try {
//            emailBody = "Activation Link: " + UUIDTokenService.generateActivationURL(token);
//        } catch (MalformedURLException e) {
//            logger.info("URL Error" + e);
//            e.printStackTrace();
//        }
//
//        logger.info("ACTIVATION URL" + emailBody);

//        generalMailService.sendAccountActivationUrl(email,url);
        //send email
         //emailSenderService.sendEmail(email,"Account activation",emailBody);
    }

    @Override
    public String validateActivationToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ActivationToken activationToken = activationTokenRepository.findByToken(uuid);

        //check expiration
        //return email id if all good
        if (activationToken != null) {
            if (activationToken.getTokenTimeLimit().isBefore(LocalDateTime.now())) {

                logger.info("-------------------TIME LIMIT EXCEEDED---------------");

                //remove token from table
                //send new url if time limit exceeds and throw exception

                activationTokenRepository.deleteById(uuid);

                activationHelper(activationToken.getUserEmail());

                throw new TokenExpiredException();

            }

            logger.info("-------------------UNDER TIME LIMIT---------------");
            return activationToken.getUserEmail();

        } else throw new UserEmailNotFoundException();
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String newPassword) {


            User foundUser = userRepository.findByEmail(email).orElseThrow(UserEmailNotFoundException::new);

            if (foundUser.isActive()) {
                return userRepository.updatePassword(passwordEncoder.encode(newPassword), email) > 0;
            } else {
                throw new AccountNotActiveException();
            }


    }

    public String validateResetPasswordToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ResetPasswordToken resetPasswordToken = resetPassTokenRepository.findByToken(uuid);

        //check expiration
        //return email id if all good
        if (resetPasswordToken != null) {

            if (resetPasswordToken.getTokenTimeLimit().isBefore(LocalDateTime.now())) {

                logger.info("-------------------TIME LIMIT EXCEEDED---------------");

                //remove token from table
                //send new url if time limit exceeds and throw exception

                resetPassTokenRepository.deleteById(uuid);

//                activationHelper(userAccessToken.getEmail());

                throw new TokenExpiredException(messageSource.getMessage("token.expired", null, "message", locale));

            }

            logger.info("-------------------UNDER TIME LIMIT---------------");
            return resetPasswordToken.getUserEmail();
        }


        logger.info("-------------------UNDER TIME LIMIT STILL HERE--------------");

        throw new UserEmailNotFoundException();

    }

    @Override
    public boolean forgotPasswordHelper(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(UserEmailNotFoundException::new);

        if (!user.isActive()) {
            throw new AccountNotActiveException();
        }

//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setToken(token);
        resetPasswordToken.setUserEmail(email);
        resetPasswordToken.setTokenTimeLimit(LocalDateTime.now().plusMinutes(1));
        resetPassTokenRepository.save(resetPasswordToken);


        try {
            String url = UUIDTokenService.generateResetPassURL(token);
            generalMailService.sendForgotPasswordEmail(email, url);

        } catch (MalformedURLException e) {
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
        //generate url
//            String emailBody = "";
//            try {
//                emailBody = "Reset Password Link: " + UUIDTokenService.generateResetPassURL(token);
//            } catch (MalformedURLException e) {
//                logger.info("URL Error" + e);
//                e.printStackTrace();
//            }

//            generalMailService.sendForgotPasswordEmail(email,url);

        //send email
        // emailSenderService.sendEmail(email,"Reset Password",emailBody);
//            logger.info(emailBody);

        return true;
    }




    @Override
    public boolean logoutHelper(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(
                ()->new UsernameNotFoundException(messageSource.getMessage("user.not.found",null,"message",LocaleContextHolder.getLocale())));

        String accessToken = user.getJwtAccessToken().getAccessToken();
        String refreshToken = user.getJwtRefreshToken().getRefreshToken();

        //add token to blacklist table
        blacklistTokenRepository.save(new BlacklistToken(accessToken));
        blacklistTokenRepository.save(new BlacklistToken(refreshToken));

        loginService.removeAlreadyGeneratedTokens(username);

        user.setJwtAccessToken(null);
        user.setJwtRefreshToken(null);

        return true;

    }

    @Override
    public void updateAddress(Long id, Address newAddress,String username) {

        User user = userRepository.findByEmail(username)
                    .orElseThrow(UserEmailNotFoundException::new);


        //check address already exist
        user.getAddresses().forEach(address -> {
            if(address.getLabel().equalsIgnoreCase(newAddress.getLabel())){
                throw new UniqueFieldException(
                        messageSource.getMessage("same.address.exist",null,"message", LocaleContextHolder.getLocale()
                        )
                );
            }
        });


        Address addressToBeUpdated = user.getAddresses()
                .stream()
                .filter(address -> address.getId().equals(id))
                .findFirst().orElseThrow(IdNotFoundException::new);

//            Address addressToBeUpdated = addressRepository.findById(id).orElseThrow(IdNotFoundException::new);

            if (newAddress.getAddressLine() != null)
                addressToBeUpdated.setAddressLine(newAddress.getAddressLine());

            if (newAddress.getCity() != null)
                addressToBeUpdated.setCity(newAddress.getCity());

            if (newAddress.getLabel() != null)
                addressToBeUpdated.setLabel(newAddress.getLabel());

            if (newAddress.getZipCode() != null)
                addressToBeUpdated.setZipCode(newAddress.getZipCode());

            if (newAddress.getCountry() != null)
                addressToBeUpdated.setCountry(newAddress.getCountry());

            if (newAddress.getState() != null)
                addressToBeUpdated.setState(newAddress.getState());


            addressRepository.save(addressToBeUpdated);


    }

    @Override
    public boolean saveUserImage(Long id, MultipartFile image) {
        try {

            String fileName = fileService.uploadImage(id, path, image);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public List<FetchCustomerDTO> getAllCustomersPaged(int page, int size, String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<Customer> customerList = customerPage.getContent();

        //Constructor reference
        return customerList.stream().map(FetchCustomerDTO::new).collect(Collectors.toList());

    }

    @Override
    public List<FetchSellerDTO> getAllSellersPaged(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        Page<Seller> sellersPage = sellerRepository.findAll(pageable);

        List<Seller> sellerList = sellersPage.getContent();

        return sellerList.stream().map(FetchSellerDTO::new).collect(Collectors.toList());

    }

    @Override
    public FetchCustomerDTO getCustomer(String email) {
        return new FetchCustomerDTO(customerRepository.findByEmail(email).orElseThrow(UserEmailNotFoundException::new));
    }

    @Override
    public FetchSellerDTO getSeller(String email) {
        return new FetchSellerDTO(sellerRepository.findByEmail(email).orElseThrow(UserEmailNotFoundException::new));
    }

    @Override
    public String getRole(String username) {

        User user =  userRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);


        return user.getRoles().get(0).getAuthority();

    }
}