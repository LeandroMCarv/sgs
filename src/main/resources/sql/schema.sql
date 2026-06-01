CREATE DATABASE IF NOT EXISTS sgs_db;
USE sgs_db;

CREATE TABLE solicitante(
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE categoria(
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE solicitacao(
	id INT AUTO_INCREMENT PRIMARY KEY,
    solicitante_id INT NOT NULL,
    categoria_id INT NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_solicitacao DATETIME NOT NULL,
    status VARCHAR(15) NOT NULL,
    CONSTRAINT fk_solicitacao_solicitante FOREIGN KEY (solicitante_id) REFERENCES solicitante(id),
    CONSTRAINT fk_solicitacao_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);