package com.diegoribeiro.javafxws.model.services;

import com.diegoribeiro.javafxws.model.dao.DaoFactory;
import com.diegoribeiro.javafxws.model.dao.SellerDao;
import com.diegoribeiro.javafxws.model.entities.Seller;

import java.util.List;

public class SellerService {

    private final SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return sellerDao.findAll();
    }

    public void saveOrUpdate(Seller obj) {
        if (obj.getId() == null) {
            sellerDao.insert(obj);
        } else {
            sellerDao.update(obj);
        }
    }

    public void delete(Seller obj) {
        sellerDao.deleteById(obj.getId());
    }
}
