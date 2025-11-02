-- V2__create_indexes.sql
-- Criação de índices para melhorar consultas por cidade e tipo

-- Índice em cidade
CREATE INDEX idx_esg_pontos_coleta_cidade ON esg_pontos_coleta(cidade);

-- Índice em tipo
CREATE INDEX idx_esg_pontos_coleta_tipo ON esg_pontos_coleta(tipo);

-- Índice para acelerar buscas por ponto_coleta_id na tabela de registros
CREATE INDEX idx_esg_registros_coleta_ponto_id ON esg_registros_coleta(ponto_coleta_id);

COMMIT;