Caso de Uso 1: Cadastro de Novo Usuário
Objetivo:
Permitir que novos usuários (alunos ou professores) se cadastrem na aplicação. 
Deve ser possível que:
1.	O usuário cadastre-se fornecendo os seguintes dados obrigatórios:
•	Nome completo
•	E-mail válido
•	Senha segura (com critérios de complexidade)
•	Tipo de usuário (Aluno ou professor)
2.	O sistema valide o e-mail informado para garantir que está em um formato correto.
3.	O sistema verifique se o e-mail já está cadastrado antes de concluir o registro.
4.	O sistema armazene as informações do usuário em um banco de dados seguro.
5.	O sistema envie um e-mail de confirmação para o endereço fornecido pelo usuário após o cadastro.
6.	O usuário veja uma mensagem de sucesso ao concluir o cadastro.

Não deve ser possível que:
1.	O usuário se cadastre sem preencher todos os campos obrigatórios.
2.	O usuário se cadastre utilizando um e-mail que já está cadastrado no sistema.
3.	O sistema armazene senhas em texto simples; elas devem ser criptografadas.
4.	O cadastro seja concluído com uma senha que não atenda aos critérios de complexidade definidos (ex: comprimento mínimo, caracteres especiais).
5.	O sistema envie o e-mail de confirmação se a verificação dos dados do usuário falhar.
