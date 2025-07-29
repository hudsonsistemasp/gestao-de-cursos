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


Caso de Uso 2: Cadastro de um novo curso
Pós condições:
  •  Um novo curso é criado e registrado no banco de dados
  •  O curso está disponível na lista de cursos e pode ser acessado e gerenciado posteriormente.

Requisitos Funcionais: 

Deve ser possível que:
1. O professor acessar a interface para criar um novo curso.
2. O professor preencher os campos obrigatórios no formulário de criação do curso, como título e descrição.
3. O sistema validar as informações inseridas e permitir a criação do curso, se todos os dados estiverem corretos e completos.
4. O sistema salvar o novo curso no banco de dados.
5. O sistema exibir uma mensagem de sucesso após a criação do curso.
6. O novo curso ser visível e acessível na lista de cursos existentes.

Não deve ser possível que:
1. O professor criar um curso sem preencher todos os campos obrigatórios ou com informações inválidas.
2. O sistema permitir a criação do curso, se as informações inseridas não atenderem aos critérios de validação, como formato incorreto ou dados ausentes
3. O sistema salvar o curso se ocorrer um erro durante o processo de criação ou salvamento, e o administrador não receber uma mensagem de erro apropriada.
4. O curso não deve aparecer na lista de cursos se a criação falhar ou houver um erro durante o processo.