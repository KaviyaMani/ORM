package com.nila.mapping.one_to_one_foreign_key_bi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/onefk")
public class FKController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/add")
    public String initDB() {
        userRepository.deleteAllInBatch();
        addressRepository.deleteAllInBatch();
        saveData("Kaviya", "Main", "Nkl");
        saveData("Kavin", "East", "Nkl");
        saveData("Nila", "Main", "Erode");
        return "data added successfully";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("No of users: "+users.size());
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/users/name/{name}")
    public Optional<User> getUserByName(@PathVariable String name) {
        return userRepository.findByuname(name);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/address")
    public List<Address> getAddress() {
        List<Address> addresses = addressRepository.findAll();
        System.out.println("No of users: "+addresses.size());
        return addresses;
    }

    private void saveData(String name, String street, String city) {
        Address address = new Address(street, city);
        User user = new User(name);
        // set child in parent
        user.setAddress(address);
        //set parent in child
        address.setUser(user);
        //saving parent will save the child
        userRepository.save(user);
        //saving child won't save the parent since it does not own the relationship
        //addressRepository.save(address);
    }
}
