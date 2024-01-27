# Fiap Tech Challenge Fase 2 -  Sistema de Parquímetro
---
## Sistema para cobrança por útilização de estacionamento

Uma aplicação desenvolvida para melhorar a experiência dos usuários em gramado, facilitando o maneira de pagar pelo tempo de utilização por estacionar o veículo.

---
### Documentação

A documentação com os detalhes técnicos, requisitos podem ser acessados no [notion da equipe](https://www.notion.so/76d9f7f44c7e4cb0bc96a787fa0b73db?v=1a84151f993b4de69bbfcd5746c07567&pvs=4)

---
### Membros do grupo de desenvolvimento:

 - [Aydan Amorim](https://github.com/AydanAmorim/)
 - [Danilo Faccio](https://github.com/DFaccio/)
 - [Erick Ribeiro](https://github.com/erickmatheusribeiro)
 - [Isabela França](https://github.com/fysabelah)

---
### Itens a instalar

O projeto utiliza as seguintes tecnologias. Desta forma, será necessário instalações prévias.
- [Java versão 17](https://www.oracle.com/br/java/technologies/downloads/#java17);
- [Banco de dados PostgreSQL >= 14](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)

---
### Configurações
Realize as seguintes configurações no application.properties:

* spring.datasource.url:

        jdbc:postgresql://localhost:{porta da instalação do PostgreSQL}/{banco criado para executar a aplicação}
* spring.datasource.username: altere *admin* para o usuário que deseja utilizar
* spring.datasource.password: altere *root* pela senha do usuário adicionada na propriedade anterior
---
### Importante
* o sistema emitirá e-mails para as pessoas cadastradas, após a finalização do uso do estacionamento e efetivo pagamento realizado, nossos membros optaram por utilizar o gmail (e-mail do google) para o desenvolvimento, no notion existe o link com a documentação oficial do google de com gerar a senha do app para utilização.
