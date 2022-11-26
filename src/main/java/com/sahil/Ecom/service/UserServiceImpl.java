package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.FetchCustomerDTO;
import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.AccountNotActiveException;
import com.sahil.Ecom.exception.TokenExpiredException;
import com.sahil.Ecom.repository.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private UUIDTokenService UUIDTokenService;

    @Autowired
    private ResetPassTokenRepository resetPassTokenRepository;


    @Autowired
    SellerService sellerService;

    @Autowired
    MessageSource messageSource;


    @Autowired
    AddressRepository addressRepository;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String path;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public Customer login(Customer customer) {
        return null;
    }

    @Override
    public Seller login(Seller seller) {
        return null;
    }

    @Transactional
    @Override
    public User activateByEmail(String email) {

        User foundUser = userRepository.findByEmail(email).orElse(null);

        if (foundUser != null && !foundUser.isActive()) {

            foundUser.setActive(true);
            activationTokenRepository.deleteByUserEmail(email);
            return userRepository.save(foundUser);

        } else {
            throw new UsernameNotFoundException("Not found");
        }
    }

    @Override
    public boolean activateAccount(Long id) {

        //check if id exist
        //check if already true then no action return true
        //else change to true

        if (userRepository.existsById(id)) {

            User foundUser = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND"));

            if (foundUser.isActive()) {
                logger.info("------------Already Activated------------");
                return true;
            }

            foundUser.setActive(true);
            userRepository.save(foundUser);
            logger.info("---------------ACCOUNT ACTIVATED-------------------");
            //send acknowledgement email

            return true;

        }
        return false;
    }

    @Override
    public boolean deActivateAccount(Long id) {

        //check if id exist
        //check if already false then no action return true
        //else change to false

        if (userRepository.existsById(id)) {

            User foundUser = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND"));

            if (!foundUser.isActive()) {
                logger.info("------------Already Deactivated------------");
                return true;
            }

            foundUser.setActive(false);
            userRepository.save(foundUser);

            logger.info("---------------ACCOUNT DEACTIVATED-------------------");
            //send acknowledgement email
            return true;
        }
        return false;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<FetchCustomerDTO> getAllCustomers() {

        List<FetchCustomerDTO> fetchCustomerDTOList = new ArrayList<>();

//        return customerRepository.findAll();
        Iterable<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {

            FetchCustomerDTO fetchCustomerDTO = new FetchCustomerDTO();
            fetchCustomerDTO.setId(customer.getId());
            fetchCustomerDTO.setEmail(customer.getEmail());
            fetchCustomerDTO.setActive(customer.isActive());
            fetchCustomerDTO.setFullName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());

            fetchCustomerDTOList.add(fetchCustomerDTO);
        }
        return fetchCustomerDTOList;

    }

    @Override
    public List<FetchSellerDTO> getAllSellers() {

        List<FetchSellerDTO> fetchSellerDTOList = new ArrayList<>();


        Iterable<Seller> sellers = sellerRepository.findAll();

        for (Seller seller : sellers) {

            FetchSellerDTO fetchSellerDTO = new FetchSellerDTO();
            fetchSellerDTO.setId(seller.getId());
            fetchSellerDTO.setEmail(seller.getEmail());
            fetchSellerDTO.setActive(seller.isActive());
            fetchSellerDTO.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
            fetchSellerDTO.setCompanyName(seller.getCompanyName());
            fetchSellerDTO.setCompanyContact(seller.getCompanyContact());


            //only one address of seller is there
            Address address = seller.getAddresses().get(0);

            AddressDTO addressDTO = new AddressDTO();

            addressDTO.setAddressLine(address.getAddressLine());
            addressDTO.setCity(address.getCity());
            addressDTO.setLabel(address.getLabel());
            addressDTO.setZipCode(address.getZipCode());
            addressDTO.setCountry(address.getCountry());
            addressDTO.setState(address.getState());

            fetchSellerDTO.setCompanyAddress(addressDTO);

            fetchSellerDTOList.add(fetchSellerDTO);
        }
        return fetchSellerDTOList;

    }

    @Override
    public boolean checkUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void activationHelper(String email) {
//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ActivationToken activationToken = new ActivationToken();
        activationToken.setActivationToken(token);
        activationToken.setUserEmail(email);

        // setting time limits to the token
        LocalDateTime currentDateTime = LocalDateTime.now();
        activationToken.setTokenTimeLimit(currentDateTime.plusMinutes(1));

        activationTokenRepository.save(activationToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
            emailBody = "Activation Link: " + UUIDTokenService.generateActivationURL(token);
        } catch (MalformedURLException e) {
            logger.info("URL Error" + e);
            e.printStackTrace();
        }

        logger.info("ACTIVATION URL" + emailBody);

        //send email
        // emailSenderService.sendEmail(email,"Account activation",emailBody);
    }

    @Override
    public String validateActivationToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ActivationToken activationToken = activationTokenRepository.findByActivationToken(uuid);

        //check expiration
        //return email id if all good
        if (activationToken != null) {
            if (activationToken.getTokenTimeLimit().isBefore(LocalDateTime.now())) {

                logger.info("-------------------TIME LIMIT EXCEEDED---------------");

                //remove token from table
                //send new url if time limit exceeds and throw exception

                activationTokenRepository.deleteById(uuid);

                activationHelper(activationToken.getUserEmail());

                throw new TokenExpiredException(messageSource.getMessage("token.expired", null, "message", locale));

            }

            logger.info("-------------------UNDER TIME LIMIT---------------");
            return activationToken.getUserEmail();
        } else throw new UsernameNotFoundException("NO mail Found for token");
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String newPassword) {

        if (userRepository.existsByEmail(email)) {

            User foundUser = userRepository.findByEmail(email).get();

            if (foundUser.isActive()) {

                return userRepository.updatePassword(passwordEncoder.encode(newPassword),email) > 0;
//                foundUser.setPassword(passwordEncoder.encode(newPassword));
//                userRepository.save(foundUser);
//                return true;
            } else {
                throw new AccountNotActiveException("Account Not Active");
            }

        } else {
            throw new UsernameNotFoundException("Not found");
        }


    }

    public String validateResetPasswordToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ResetPasswordToken resetPasswordToken = resetPassTokenRepository.findByResetPassToken(uuid);

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
        throw new UsernameNotFoundException("NO mail Found for token");

    }

    @Override
    public void forgotPasswordHelper(String email) {
//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setResetPassToken(token);
        resetPasswordToken.setUserEmail(email);
        resetPasswordToken.setTokenTimeLimit(LocalDateTime.now().plusMinutes(1));
        resetPassTokenRepository.save(resetPasswordToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
            emailBody = "Reset Password Link: " + UUIDTokenService.generateResetPassURL(token);
        } catch (MalformedURLException e) {
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
//send email
        // emailSenderService.sendEmail(email,"Reset Password",emailBody);
        logger.info(emailBody);
    }

    @Override
    public void sendSellerAcknowledgement(String email) {

        String emailBody = "ACCOUNT CREATED WAITING FOR APPROVAL";
        emailSenderService.sendEmail(email, "Registration Successfully", emailBody);

    }

    @Override
    public boolean logout(String accessToken) {

        //add token to blacklist table
        blacklistTokenRepository.save(new BlacklistToken(accessToken));
        return true;

    }

    @Override
    public boolean updateAddress(Long id,Address newAddress) {

        if(addressRepository.existsById(id)){
            Address addressToBeUpdated =  addressRepository.findById(id).get();

            if(newAddress.getAddressLine() != null)
                addressToBeUpdated.setAddressLine(newAddress.getAddressLine());

            if(newAddress.getCity() != null)
                addressToBeUpdated.setCity(newAddress.getCity());

            if(newAddress.getLabel() != null)
                addressToBeUpdated.setLabel(newAddress.getLabel());

            if(newAddress.getZipCode() != null)
                addressToBeUpdated.setZipCode(newAddress.getZipCode());

            if(newAddress.getCountry() != null)
                addressToBeUpdated.setCountry(newAddress.getCountry());

            if(newAddress.getState() != null)
                addressToBeUpdated.setState(newAddress.getState());


            addressRepository.save(addressToBeUpdated);

            return true;
        }

        return false;

    }

    @Override
    public boolean saveUserImage(Long id, MultipartFile image) {
        try{

            String fileName = fileService.uploadImage(id,path,image);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public List<FetchCustomerDTO> getAllCustomersPaged(int page,int size,String sort) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(sort).ascending());

        Page<Customer> customerPage= customerRepository.findAll(pageable);

        List<Customer> customerList = customerPage.getContent();

        return customerList.stream().map(customer -> {

                    FetchCustomerDTO fetchCustomerDTO = new FetchCustomerDTO();
                    fetchCustomerDTO.setEmail(customer.getEmail());
                    fetchCustomerDTO.setId(customer.getId());
                    fetchCustomerDTO.setFullName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
                    fetchCustomerDTO.setContact(customer.getContact());
                    fetchCustomerDTO.setActive(customer.isActive());

                    return fetchCustomerDTO;

                }
        ).collect(Collectors.toList());

    }

    @Override
    public List<FetchSellerDTO> getAllSellersPaged(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(sort).ascending());

        Page<Seller> sellersPage= sellerRepository.findAll(pageable);

        List<Seller> sellerList = sellersPage.getContent();

        return sellerList.stream().map(seller -> {

                    FetchSellerDTO fetchSellerDTO = new FetchSellerDTO();
                    fetchSellerDTO.setEmail(seller.getEmail());
                    fetchSellerDTO.setId(seller.getId());
                    fetchSellerDTO.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
                    fetchSellerDTO.setCompanyContact(seller.getCompanyContact());
                    fetchSellerDTO.setGst(seller.getCompanyContact());

                    fetchSellerDTO.setCompanyAddress(seller.getAddresses().stream().map(address -> {

                        AddressDTO addressDTO = new AddressDTO();
                        addressDTO.setState(address.getState());
                        addressDTO.setCountry(address.getCountry());
                        addressDTO.setCity(address.getCity());
                        addressDTO.setZipCode(address.getZipCode());
                        addressDTO.setLabel(address.getLabel());

                        return addressDTO;
                    }).toList().get(0));

                    fetchSellerDTO.setActive(seller.isActive());

                    return fetchSellerDTO;

                }

        ).collect(Collectors.toList());

    }
}
