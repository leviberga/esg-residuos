# language: pt
@ESG
Funcionalidade: Gestão de Resíduos e Pontos de Coleta
  Como um administrador do sistema
  Quero gerenciar pontos de coleta e registros
  Para garantir a conformidade ambiental e eficiência operacional

  Contexto:
    Dado que a API está disponível em "http://localhost:8080"

  Cenário: Cadastrar um novo ponto de coleta com sucesso (Pilar Governance)
    Dado que eu utilizo as credenciais de administrador "admin" e "admin"
    E possuo os dados de um ponto de coleta em "Itapevi" com tipo "Recicláveis"
    Quando eu enviar uma requisição POST para "/api/ponto-coleta"
    Então o status code deve ser 201
    E o contrato da resposta deve seguir o schema "ponto_coleta_schema.json"

  Cenário: Tentar cadastrar ponto de coleta sem autorização (Pilar Compliance)
    Dado que eu utilizo as credenciais de usuário comum "user" e "user"
    E possuo os dados de um ponto de coleta em "São Paulo" com tipo "Eletrônicos"
    Quando eu enviar uma requisição POST para "/api/ponto-coleta"
    Então o status code deve ser 403
    E a mensagem de erro deve conter "Forbidden"

  Cenário: Consultar alertas de coleta para otimização (Pilar Ambiental/Eficiência)
    Dado que eu estou autenticado no sistema
    Quando eu consultar os alertas em "/api/coletas/alertas"
    Então o status code deve ser 200
    E a lista de alertas não deve ser nula