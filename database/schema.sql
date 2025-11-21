CREATE DATABASE IF NOT EXISTS bidesk_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'dev'@'%' IDENTIFIED BY 'senha123';
GRANT ALL PRIVILEGES ON *.* TO 'dev'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE bidesk_db;

-- Tabela de Materiais (Estoque)
CREATE TABLE IF NOT EXISTS materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    unidade VARCHAR(20) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    status ENUM('VAZIO', 'BAIXO', 'ALTO') NOT NULL DEFAULT 'VAZIO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de Registros Financeiros
CREATE TABLE IF NOT EXISTS registros_financeiros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    numero VARCHAR(50),
    data DATE NOT NULL,
    registro VARCHAR(100),
    pago DECIMAL(10, 2) DEFAULT 0.00,
    deve DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Tabela de Despesas (Financeiro)
CREATE TABLE IF NOT EXISTS despesas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2),
    cliente_id INT,
    despesa DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Tabela de Despesas de Materiais
CREATE TABLE IF NOT EXISTS despesas_materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    gasto DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);

-- Tabela de Manutenções
CREATE TABLE IF NOT EXISTS manutencoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Inserir dados de exemplo
INSERT INTO materiais (nome, unidade, quantidade, status) VALUES
('Taco', 'un', 0, 'VAZIO'),
('Jogo de bolas', 'un', 4, 'BAIXO'),
('Cabeça de taco', 'un', 0, 'VAZIO'),
('Giz', 'un', 12, 'ALTO'),
('Giz azul', 'un', 15, 'ALTO'),
('Bola branca', 'un', 0, 'VAZIO');

INSERT INTO clientes (nome, endereco, cidade) VALUES
('Natalino Varela', 'Rua 1 - Bairro 2 - 123', 'Cidade aleatória - AL'),
('Alexandre de Moraes', 'Rua 2 - Bairro 2 - 222', 'Cidade aleatória - PE'),
('Paulo de Lima', 'Rua 3 - Bairro 4 - 412', 'Paulo Afonso - BA'),
('Pedro Oliveira', 'Rua 5 - Bairro 2 - 22', 'Paulo Afonso - BA');

INSERT INTO registros_financeiros (cliente_id, numero, data, registro, pago, deve) VALUES
(1, '123', '2025-02-12', '12345', 100.00, 0.00),
(2, '321', '2025-02-24', '41234', 300.00, 700.00);

INSERT INTO despesas (data, cidade, despesa, total) VALUES
('1945-07-10', 'Paulo Afonso - BA', 0.01, 0.01),
('1945-07-17', 'Tilambuco - CA', 0.01, 0.01);

INSERT INTO manutencoes (cliente_id, titulo, descricao) VALUES
(1, 'Jogo de bolas com defeito', 'Bola 6 e 9 quebradas'),
(2, 'Pano rasgado', 'Pano rasgado e velho');


