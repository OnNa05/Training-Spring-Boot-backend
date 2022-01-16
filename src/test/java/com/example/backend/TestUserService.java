package com.example.backend;

import com.example.backend.entity.Address;
import com.example.backend.entity.Social;
import com.example.backend.entity.User;
import com.example.backend.exception.BaseException;
import com.example.backend.service.AddressService;
import com.example.backend.service.SocialService;
import com.example.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private SocialService socialService;

    @Autowired
    private AddressService addressService;

    @Order(1)
    @Test
    void testCreate() throws BaseException {
        User user = userService.create(
                TestCreateData.email,
                TestCreateData.pass,
                TestCreateData.name
        );

        // not null
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());

        // check equal
        Assertions.assertEquals(TestCreateData.email, user.getEmail());

        boolean isMatched = userService.matchPass(TestCreateData.pass, user.getPass());
        Assertions.assertTrue(isMatched);

        Assertions.assertEquals(TestCreateData.name, user.getName());

    }

    @Order(2)
    @Test
    void testUpdate() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        User updateUser = userService.updateName(user.getId(), TestUpdateData.name);

        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(TestUpdateData.name, updateUser.getName());

    }

    @Order(3)
    @Test
    void testCreateSocial() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        Social social = user.getSocial();
        Assertions.assertNull(social);

        social = socialService.create(
                user,
                SocialTestCreatData.facebook,
                SocialTestCreatData.line,
                SocialTestCreatData.instagram,
                SocialTestCreatData.tiktok
        );

        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestCreatData.facebook,social.getFacebook());


    }

    @Order(4)
    @Test
    void testCreatAddress() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        List<Address> addresses = user.getAddresses();
        Assertions.assertTrue(addresses.isEmpty());

        creatAddress(user,AddressTestCreatData.line1,AddressTestCreatData.line2,AddressTestCreatData.zipcode);
        creatAddress(user,AddressTestCreatData2.line1,AddressTestCreatData2.line2,AddressTestCreatData2.zipcode);

    }

    void creatAddress(User user,String line1, String line2, String zipcode) throws BaseException {
        Address address = addressService.create(
                user,
                line1,
                line2,
                zipcode
        );

        Assertions.assertNotNull(address);
        Assertions.assertEquals(line1,address.getLine1());
        Assertions.assertEquals(line2,address.getLine2());
        Assertions.assertEquals(zipcode,address.getZipcode());

    }

    @Order(9)
    @Test
    void testDelete() {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        userService.delete(user.getId());

        // check social
        Social social = user.getSocial();
        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestCreatData.facebook,social.getFacebook());

        // check address
        List<Address> addresses = user.getAddresses();
        Assertions.assertFalse(addresses.isEmpty());
        Assertions.assertEquals(2,addresses.size());

        Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(optDelete.isEmpty());
    }

    interface TestCreateData {

        String email = "apirat.t@test.com";

        String pass = "12345";

        String name = "Apirat";
    }

    interface TestUpdateData {

        String name = "T.Apirat";
    }

    interface SocialTestCreatData{

        String facebook = "apirat.F";

        String line = "apirat.L";

        String instagram = "apirat.I";

        String tiktok = "apirat.TT";

    }

    interface AddressTestCreatData {

        String line1 = "96/1";

        String line2 = "Muang";

        String zipcode = "73000";
    }

    interface AddressTestCreatData2 {

        String line1 = "96/2";

        String line2 = "NK";

        String zipcode = "73000";
    }

}
