package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Orders;
import model.User;

public class MySQLOrdersDAO implements OrdersDAO {

	@Override
	public boolean save(Orders orders) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO orders VALUES (DEFAULT, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		 // Verificar se o ID do usuário é válido
	    if (orders.getUser() == null || orders.getUser().getId() == 0) {
	        throw new ModelException("Usuário não selecionado ou ID inválido.");
	    }
	
		db.setDate(1, orders.getDate());
		db.setString(2, orders.getAmount());
		db.setString(3, orders.getDescription());
		db.setInt(4, orders.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Orders orders) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE orders SET date = ?, amount = ?, description = ?, user_id = ? where id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setDate(1, orders.getDate());
		db.setString(2, orders.getAmount());
		db.setString(3, orders.getDescription());
		db.setInt(4, orders.getUser().getId());
		db.setInt(5, orders.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Orders orders) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM orders WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, orders.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Orders> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Orders> orders = new ArrayList<Orders>();
			
		// Declara uma instrução SQL
		String sqlQuery =  "SELECT o.id, o.date, o.amount, o.description, o.user_id, u.nome FROM orders o INNER JOIN users u ON o.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			Orders order = createOrders(db);
			orders.add(order);
		

		}
		
		return orders;
	}

	@Override
	public Orders findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM orders WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Orders o = null;
		while (db.next()) {
			o = createOrders(db);			
			break;
		}
		
		return o;
	}
	
	private Orders createOrders(DBHandler db) throws ModelException {
		Orders o = new Orders(db.getInt("id"));
		
		o.setDate(db.getDate("date"));
	    o.setAmount(db.getString("amount"));
	    o.setDescription(db.getString("description"));
		
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
		User user = userDAO.findById(db.getInt("user_id"));
		o.setUser(user);
		return o;
	}
}
