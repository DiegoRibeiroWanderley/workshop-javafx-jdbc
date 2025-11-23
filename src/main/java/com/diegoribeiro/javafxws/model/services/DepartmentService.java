package com.diegoribeiro.javafxws.model.services;

import com.diegoribeiro.javafxws.model.dao.DaoFactory;
import com.diegoribeiro.javafxws.model.dao.DepartmentDao;
import com.diegoribeiro.javafxws.model.entities.Department;

import java.util.List;

public class DepartmentService {

    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    public void saveOrUpdate(Department obj) {
        if (obj.getId() == null) {
            departmentDao.insert(obj);
        } else {
            departmentDao.update(obj);
        }
    }

    public void delete(Department obj) {
        departmentDao.deleteById(obj.getId());
    }
}
