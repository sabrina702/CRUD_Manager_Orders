<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Inserir Pedido</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${action eq "insert" ? "Adicionar" : "Editar"} Pedido</h3>
		
		<form action="${pageContext.request.contextPath}/orders/${action}" method="POST">
			<input type="hidden" name="orders_id" value="${orders.getId()}">
			
			<div class="row">
				<div class="form-group col-md-4">
					<label for="start">Data</label>
					<input type="date" class="form-control" id="date" name="date" 
						   autofocus="autofocus" placeholder="Data do pedido" 
						   required 
						   oninvalid="this.setCustomValidity('Por favor, informe a data do pedido.')"
						   oninput="setCustomValidity('')"  value="${orders.getDate()}" />
				</div>
				
				<div class="form-group col-md-6">
					<label for="content">Valor</label> <input type="text"
						class="form-control" id="amount" name="amount"
						autofocus="autofocus" placeholder="Valor do pedido" required
						oninvalid="this.setCustomValidity('Por favor, informe o Valor do pedido.')"
						oninput="setCustomValidity('')" value="${orders.getAmount()}">
				</div>
				
				<div class="form-group col-md-6">
					<label for="content">Descrição</label> <input type="text"
						class="form-control" id="description" name="description"
						autofocus="autofocus" placeholder="Descrição do pedido" required
						oninvalid="this.setCustomValidity('Por favor, informe a Descrição do pedido.')"
						oninput="setCustomValidity('')" value="${orders.getDescription()}">
				</div>
				
				<div class="form-group col-md-6">
					<label for="user">Usuário</label>
					<select id="user" class="form-control selectpicker" name="user" 
						    required oninvalid="this.setCustomValidity('Por favor, informe o usuário.')"
						    oninput="setCustomValidity('')">
					  <option value="">Selecione um usuário</option>
					  <c:forEach var="user" items="${users}">
					  	<option value="${user.getId()}">
					  		${user.getName()}
					  	</option>	
					  </c:forEach>
					</select>
				</div>
			</div>
			<hr />

			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/orders"
						class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">${action eq "insert" ? "Criar" : "Editar"}Pedido</button>
				</div>
			</div>
		
		</form>
	</div>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>
</html>