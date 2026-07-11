package com.acme.legacy.repository;

import com.acme.legacy.domain.Customer;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.util.List;

@SuppressWarnings("deprecation")
public class HibernateCustomerDao extends HibernateDaoSupport implements CustomerDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> findAll() {
        return (List<Customer>) getHibernateTemplate().find("from Customer");
    }

    @Override
    public Customer findById(Long id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class)
                .add(Restrictions.eq("id", id));
        return (Customer) getHibernateTemplate().findByCriteria(criteria).stream().findFirst().orElse(null);
    }

    @Override
    public void save(Customer customer) {
        getHibernateTemplate().saveOrUpdate(customer);
    }
}
