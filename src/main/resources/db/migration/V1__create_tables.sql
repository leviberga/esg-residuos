-- V1__create_tables.sql
-- Criação de todas as tabelas e sequências do projeto com prefixo 'esg_'

-- ---
-- Tabela de Usuários (para autenticação)
-- ---
CREATE SEQUENCE esg_seq_usuario START WITH 1 INCREMENT BY 1;

CREATE TABLE esg_usuarios (
                              id NUMBER(19,0) NOT NULL PRIMARY KEY,
                              email VARCHAR2(255) NOT NULL UNIQUE,
                              senha VARCHAR2(255) NOT NULL,
                              role VARCHAR2(50) NOT NULL
);

-- ---
-- Tabela de Pontos de Coleta
-- ---
CREATE SEQUENCE esg_seq_ponto_coleta START WITH 1 INCREMENT BY 1;

CREATE TABLE esg_pontos_coleta (
                                   id NUMBER(19,0) NOT NULL PRIMARY KEY,
                                   nome VARCHAR2(255) NOT NULL,
                                   endereco VARCHAR2(500),
                                   cidade VARCHAR2(100) NOT NULL,
                                   tipo VARCHAR2(100) NOT NULL,
                                   volume_maximo NUMBER(10,2)
);

-- ---
-- Tabela de Registros de Coleta
-- ---
CREATE SEQUENCE esg_seq_registro_coleta START WITH 1 INCREMENT BY 1;

CREATE TABLE esg_registros_coleta (
                                      id NUMBER(19,0) NOT NULL PRIMARY KEY,
                                      ponto_coleta_id NUMBER(19,0) NOT NULL,
                                      volume_coletado NUMBER(10,2) NOT NULL,
                                      data_coleta TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Chave estrangeira referenciando a nova tabela 'esg_pontos_coleta'
                                      CONSTRAINT fk_esg_ponto_coleta
                                          FOREIGN KEY (ponto_coleta_id)
                                              REFERENCES esg_pontos_coleta(id)
);