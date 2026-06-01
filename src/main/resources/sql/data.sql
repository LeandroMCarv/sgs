USE sgs_db;

-- CARGA PARA SOLICITANTES
INSERT INTO solicitante (nome,cpf_cnpj) VALUES
('SergipeTec','06938508000111'),
('CONTABILIZE ASSESSORIA CONTABIL LTDA - ME','19801458000178'),
('L & A Informatica Limitada','10526704000156'),
('COLEGIO MAGNUS LTDA - ME','04545587000175'),
('Fundação Universidade Federal de Sergipe','13031547000104');

-- CARGA PARA SERVICOS
INSERT INTO categoria (nome) VALUES
('Software'),
('Contabilidade'),
('Ensino'),
('Hardware'),
('Licenças de Programas');

-- SELECTS PARA VISUALIZACAO DOS DADOS
SELECT * FROM solicitante;
SELECT * FROM categoria;