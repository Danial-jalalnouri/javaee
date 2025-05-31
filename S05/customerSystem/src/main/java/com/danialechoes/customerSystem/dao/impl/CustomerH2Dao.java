package com.danialechoes.customerSystem.dao.impl;

import com.danialechoes.customerSystem.dao.CustomerDao;
import com.danialechoes.customerSystem.model.Customer;
import com.danialechoes.customerSystem.model.CustomerType;
import com.danialechoes.customerSystem.model.LegalCustomer;
import com.danialechoes.customerSystem.model.RealCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
@Primary
@Profile("h2")
public class CustomerH2Dao implements CustomerDao {

    private final JdbcTemplate jdbc;

    @Autowired
    public CustomerH2Dao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Customer save(Customer customer) {
        String customerSql = "INSERT INTO customer (name, phone_number, type) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(customerSql, new String[]{"id"});
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getType().name());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        customer.setId(id);

        if (customer instanceof RealCustomer realCustomer) {
            String realCustomerSql = "INSERT INTO real_customer (id, family) VALUES (?, ?)";
            jdbc.update(realCustomerSql, id, realCustomer.getFamily());
        } else if (customer instanceof LegalCustomer legalCustomer) {
            String legalCustomerSql = "INSERT INTO legal_customer (id, fax) VALUES (?, ?)";
            jdbc.update(legalCustomerSql, id, legalCustomer.getFax());
        }

        return customer;
    }

    public Customer update(Long id, Customer customer) {
        String customerSql = "UPDATE customer SET name = ?, phone_number = ?, type = ? WHERE id = ?";
        jdbc.update(customerSql, customer.getName(), customer.getPhoneNumber(), customer.getType().name(), id);

        if (customer instanceof RealCustomer realCustomer) {
            String realCustomerSql = "UPDATE real_customer SET family = ? WHERE id = ?";
            jdbc.update(realCustomerSql, realCustomer.getFamily(), id);
        } else if (customer instanceof LegalCustomer legalCustomer) {
            String legalCustomerSql = "UPDATE legal_customer SET fax = ? WHERE id = ?";
            jdbc.update(legalCustomerSql, legalCustomer.getFax(), id);
        }

        return customer;
    }

    public boolean delete(Long id) {
        String customerSql = "DELETE FROM customer WHERE id = ?";
        return jdbc.update(customerSql, id) > 0;
    }

    public Customer findById(Long id) {
        String customerSql = "SELECT * FROM customer WHERE id = ?";
        Map<String, Object> customerRow = jdbc.queryForMap(customerSql, id);

        CustomerType type = CustomerType.valueOf((String) customerRow.get("type"));
        Customer customer;

        if (type == CustomerType.REAL) {
            String realCustomerSql = "SELECT * FROM real_customer WHERE id = ?";
            Map<String, Object> realCustomerRow = jdbc.queryForMap(realCustomerSql, id);
            customer = RealCustomer.builder()
                    .id(id)
                    .name((String) customerRow.get("name"))
                    .phoneNumber((String) customerRow.get("phone_number"))
                    .type(type)
                    .family((String) realCustomerRow.get("family"))
                    .build();
        } else {
            String legalCustomerSql = "SELECT * FROM legal_customer WHERE id = ?";
            Map<String, Object> legalCustomerRow = jdbc.queryForMap(legalCustomerSql, id);
            customer = LegalCustomer.builder()
                    .id(id)
                    .name((String) customerRow.get("name"))
                    .phoneNumber((String) customerRow.get("phone_number"))
                    .type(type)
                    .fax((String) legalCustomerRow.get("fax"))
                    .build();
        }

        return customer;
    }

    public List<Customer> findAll() {
        String customerSql = "SELECT * FROM customer";
        return jdbc.query(customerSql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            CustomerType type = CustomerType.valueOf(rs.getString("type"));

            if (type == CustomerType.REAL) {
                String realCustomerSql = "SELECT * FROM real_customer WHERE id = ?";
                Map<String, Object> realCustomerRow = jdbc.queryForMap(realCustomerSql, id);
                return RealCustomer.builder()
                        .id(id)
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .type(type)
                        .family((String) realCustomerRow.get("family"))
                        .build();
            } else {
                String legalCustomerSql = "SELECT * FROM legal_customer WHERE id = ?";
                Map<String, Object> legalCustomerRow = jdbc.queryForMap(legalCustomerSql, id);
                return LegalCustomer.builder()
                        .id(id)
                        .name(rs.getString("name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .type(type)
                        .fax((String) legalCustomerRow.get("fax"))
                        .build();
            }
        });
    }

    public boolean existsById(Long id) {
        String customerSql = "SELECT COUNT(*) FROM customer WHERE id = ?";
        Integer count = jdbc.queryForObject(customerSql, Integer.class, id);
        return count != null && count > 0;
    }
}
