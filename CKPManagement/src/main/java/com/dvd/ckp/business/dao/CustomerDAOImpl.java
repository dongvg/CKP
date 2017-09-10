/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Customers;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dmin
 */
@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Customers> getAllCustomer() {

        Query query = getCurrentSession().getNamedQuery("Customers.fillAllCustomer");
        return (List<Customers>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Customers> getCustomer(Customers customer) {

        StringBuilder vstrSql=new StringBuilder("FROM Customers c WHERE c.status=1 ");
        if(customer!=null){
            if(customer.getCustomerCode()!=null && !"".equals(customer.getCustomerCode())){
                vstrSql.append("AND c.customerCode = :customerCode");
            }
        }
        Query query = getCurrentSession().createQuery(vstrSql.toString());
        if(customer!=null){
            if(customer.getCustomerCode()!=null && !"".equals(customer.getCustomerCode())){
                query.setParameter("customerCode", customer.getCustomerCode());
            }
        }
        return (List<Customers>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertOrUpdateCustomer(Customers customer) {
        getCurrentSession().saveOrUpdate(customer);
    }

}
