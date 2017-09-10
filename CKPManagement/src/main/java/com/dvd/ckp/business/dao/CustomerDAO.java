/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Customers;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface CustomerDAO {

    List<Customers> getAllCustomer();

    void insertOrUpdateCustomer(Customers customer);

    List<Customers> getCustomer(Customers customer);
}
