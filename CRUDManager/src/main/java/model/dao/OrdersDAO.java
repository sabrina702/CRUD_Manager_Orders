package model.dao;

import java.util.List;

import model.ModelException;
import model.Orders;

public interface OrdersDAO {
	boolean save(Orders Orders) throws ModelException;
	boolean update(Orders Orders) throws ModelException;
	boolean delete(Orders Orders) throws ModelException;
	List<Orders> listAll() throws ModelException;
	Orders findById(int id) throws ModelException;
}

