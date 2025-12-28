package com.ecommerce.Project.service;

import com.ecommerce.Project.exceptions.APIException;
import com.ecommerce.Project.model.Address;
import com.ecommerce.Project.model.User;
import com.ecommerce.Project.payload.AddressDTO;
import com.ecommerce.Project.repositories.AddressRepository;
import com.ecommerce.Project.repositories.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addresseList = user.getAddresses();
        addresseList.add(address);
        user.setAddresses(addresseList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddress() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new APIException("Address not found"));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {

        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

    }

    @Override
    public AddressDTO updateAddressById(Long addressId, @Valid AddressDTO addressDTO) {

        Address addressFromDb= addressRepository.findById(addressId)
                .orElseThrow(() -> new APIException("Address not found"));

        addressFromDb.setCity(addressDTO.getCity());
        addressFromDb.setStreet(addressDTO.getStreet());
        addressFromDb.setZipCode(addressDTO.getZipCode());
        addressFromDb.setCountry(addressDTO.getCountry());
        addressFromDb.setBuildingName(addressDTO.getBuildingName());
        addressFromDb.setState(addressDTO.getState());

        Address updatedAddress = addressRepository.save(addressFromDb);
        User user=addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);

    }

    @Override
    public String deleteAddress(Long addressId) {
    Address addressFromDb=addressRepository.findById(addressId)
            .orElseThrow(() -> new APIException("Address not found"));

    User user=addressFromDb.getUser();
    user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
    userRepository.save(user);

    addressRepository.delete(addressFromDb);
    return "Address has been deleted";

    }
}
