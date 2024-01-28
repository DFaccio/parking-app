# Fiap Tech Challenge Fase 2 -  Sistema de Parquímetro

---
## Sistema para cobrança por utilização de estacionamento

Uma aplicação desenvolvida para melhorar a experiência dos usuários em gramado, no quesito de estacionamento e controle de período utilizado.

---
### Documentação

A documentação com os detalhes técnicos e requisitos podem ser acessados no [notion da equipe](https://topaz-havarti-e3c.notion.site/76d9f7f44c7e4cb0bc96a787fa0b73db?v=1a84151f993b4de69bbfcd5746c07567&pvs=4).

Para uso do Swagger, basta executar a aplicação que a mesma estará disponível [neste link](http://localhost:8080/documentacao-parquimetro.html).

---
### Membros do grupo de desenvolvimento:

 - [Aydan Amorim](https://github.com/AydanAmorim/)
 - [Danilo Faccio](https://github.com/DFaccio/)
 - [Erick Ribeiro](https://github.com/erickmatheusribeiro)
 - [Isabela França](https://github.com/fysabelah)

---
### Itens a instalar

O projeto utiliza as tecnologias abaixo. Desta forma, será necessário instalações prévias.
- [Java versão 17](https://www.oracle.com/br/java/technologies/downloads/#java17);
- [Banco de dados PostgreSQL >= 14](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)

---
### Configurações

#### Executar localmente

* spring.datasource.url:

        jdbc:postgresql://localhost:{porta da instalação do PostgreSQL}/{banco criado para executar a aplicação}
* spring.datasource.username: altere *admin* para o usuário que deseja utilizar
* spring.datasource.password: altere *root* pela senha do usuário adicionada na propriedade anterior
* spring.mail.username: adicione o e-mail
* spring.mail.password: adicione a senha

### Executar através do Docker
Neste têm-se duas opções. Executar apenas a aplicação através do Docker ou a aplicação e o banco, utilizando o arquivo compose.

Caso deseja utilizar apenas a aplicação em docker, realize as mesmas configurações no application.properties como se fosse executar local. 
Faça build do arquivo e por fim crie o container utilizando network igual a host.

    docker build -t nome_imagem . 
    docker run --network="host" --name nome_imagem nome_container

Caso irá executar tudo em container, basta executar o comando abaixo.
    
    docker compose up

Lembrando que os comandos foram apresentados como se fossem executados a partir do root do diretório.

---
### Importante
O sistema emitirá e-mails, após a finalização do uso do estacionamento. 

Nossos membros optaram por utilizar o gmail (e-mail do google) para o desenvolvimento. No notion existe o link com a documentação oficial do google de com gerar a senha do app para utilização.

O processo de notificação dar-se a partir de uma schedule que verifica o tempo para adição de perído e fim de período.
Para adição será verificado se dentro de 5 ou 10 minutos o período atual se encerra. Já para períodos fixos, é verificado apenas se é menor que 10 minutos.

Períodos por hora não se encerram sozinhos (schedule). É necessário que seja desabilitado. Já perídos fixos se encerram com no mínimo 5 minutos de tolerância.

Quando encerrado manual, independente do tipo de perído, será emitido um comprovante. Já para períodos fixos que são encerrados através da schedule, será enviado e-mail.
