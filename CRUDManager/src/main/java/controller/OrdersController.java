package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Orders;
import model.User;
import model.dao.DAOFactory;
import model.dao.OrdersDAO;
import model.dao.UserDAO;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/orders", "/orders/form", "/orders/insert", "/orders/delete", "/orders/update" })
public class OrdersController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/CRUDManager/orders/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-orders.jsp");
			break;
		}
		case "/CRUDManager/orders/update": {
			CommonsController.listUsers(req);
			loadOrders(req);
			loadUsers(req);
			req.setAttribute("action", "update");
			ControllerUtil.forward(req, resp, "/form-orders.jsp");
			break;
			
		}
		default:
			listOrders(req);
			ControllerUtil.transferSessionMessagesToRequest(req);
			ControllerUtil.forward(req, resp, "/orders.jsp");
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/CRUDManager/orders/insert": {
			insertOrders(req);
			ControllerUtil.redirect(resp, req.getContextPath() + "/orders");
			break;
		}
		case "/CRUDManager/orders/delete": {
			deleteOrders(req);
			ControllerUtil.redirect(resp, req.getContextPath() + "/orders");
			break;
		}
		case "/CRUDManager/orders/update":
			updateOrders(req);
			ControllerUtil.redirect(resp, req.getContextPath() + "/orders");
			break;
		default:
			System.out.println("URL inválida " + action);
		}
	}

	private void updateOrders(HttpServletRequest req) {
		String ordersIdStr = req.getParameter("orders_id");
		int ordersId = Integer.parseInt(ordersIdStr);

		Orders orders = createOrders(req, ordersId);

		OrdersDAO dao = DAOFactory.createDAO(OrdersDAO.class);

		try {
			if (dao.update(orders)) {
				ControllerUtil.sucessMessage(req, "Pedido " + ordersId + " alterado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido" + ordersId  + " não pode ser alterado.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void deleteOrders(HttpServletRequest req) {
		String ordersIdParameter = req.getParameter("id");

		int ordersId = Integer.parseInt(ordersIdParameter);

		OrdersDAO dao = DAOFactory.createDAO(OrdersDAO.class);

		try {
			if (dao.delete(new Orders(ordersId))) {
				ControllerUtil.sucessMessage(req, "Pedido" + ordersIdParameter + " excluído com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido " + ordersIdParameter + " não pode ser excluído.");
			}
		} catch (ModelException e) {
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void insertOrders(HttpServletRequest req) {
		
		Orders orders = createOrders(req, 0);

		OrdersDAO dao = DAOFactory.createDAO(OrdersDAO.class);

		try {
			if (dao.save(orders)) {
				ControllerUtil.sucessMessage(req, "Pedido " + orders.getId() + " salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido " + orders.getId() + " não pode ser salvo.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
	
	private void loadOrders(HttpServletRequest req) {
		String ordersIdStr = req.getParameter("ordersId");
		int ordersId = Integer.parseInt(ordersIdStr);

		OrdersDAO dao = DAOFactory.createDAO(OrdersDAO.class);

		Orders orders = new Orders(0);

		try {
			orders = dao.findById(ordersId);
		} catch (ModelException e) {
			ControllerUtil.errorMessage(req, "Erro ao carregar pedido para edição.");
		}

		req.setAttribute("orders", orders);
		//return orders;
	}

	private void loadUsers(HttpServletRequest req) {
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		List<User> users = new ArrayList<>();
		try {
			users = dao.listAll();
		} catch (ModelException e) {
			ControllerUtil.errorMessage(req, "Erro ao carregar as usuário.");
		}
		req.setAttribute("user", users);
		//return users;
	}
	
	private void listOrders(HttpServletRequest req) {
		OrdersDAO dao = DAOFactory.createDAO(OrdersDAO.class);

		List<Orders> orders = null;
		try {
			orders = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}

		if (orders != null)
			req.setAttribute("orders", orders);
	}

	private Orders createOrders(HttpServletRequest req, int ordersId) {

		String date = req.getParameter("date");
		String amount = req.getParameter("amount");
		String description = req.getParameter("description");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		
		Orders orders;
		if (ordersId == 0) {
			orders = new Orders();
		} else {
			orders = new Orders(ordersId);
		}
		orders.setDate(ControllerUtil.formatDate(date));
		orders.setAmount(amount);
		orders.setDescription(description);
		orders.setUser(new User(userId));

		return orders;
	}

	
	

}
