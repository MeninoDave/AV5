CREATE DATABASE av5;
CREATE TABLE cliente (ID int, NOME varchar(999), PRIMARY KEY(ID));
CREATE TABLE produto(ID int, NOME varchar(999), CATEGORIA varchar(999), PRECO double, PRIMARY KEY(ID));
CREATE TABLE notafiscal (NUMERO int, IDCliente int, IDProduto int, PRECO double, PRIMARY KEY(ID),FOREIGN KEY (IDCiente) REFERENCES cliente(ID),FOREIGN KEY (IDProduto) REFERENCES produto(ID));