package com.diegoribeiro.javafxws.model.dao;

import com.diegoribeiro.javafxws.db.DB;
import com.diegoribeiro.javafxws.model.dao.impl.DepartmentDaoJDBC;
import com.diegoribeiro.javafxws.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
