# Fiap Tech Challenge Fase 2 -  Sistema de Parquímetro

---
## Sistema para cobrança por utilização de estacionamento

Uma aplicação desenvolvida para melhorar a experiência dos usuários em gramado, no quesito de estacionamento e controle de período utilizado.

---
### Documentação

A documentação com os detalhes técnicos e requisitos podem ser acessados no [notion da equipe](https://topaz-havarti-e3c.notion.site/76d9f7f44c7e4cb0bc96a787fa0b73db?v=1a84151f993b4de69bbfcd5746c07567&pvs=4).

Para uso do Swagger, basta executar a aplicação
  * Container: [neste link](http://localhost:7080/documentacao-parquimetro.html).
  * Local: [neste link](http://localhost:8080/documentacao-parquimetro.html).

---
### Membros do grupo de desenvolvimento:

 - [Aydan Amorim](https://github.com/AydanAmorim/)
 - [Danilo Faccio](https://github.com/DFaccio/)
 - [Erick Ribeiro](https://github.com/erickmatheusribeiro)
 - [Isabela França](https://github.com/fysabelah)

---
### Itens a instalar

O projeto utiliza as tecnologias abaixo.

- [Java versão 17](https://www.oracle.com/br/java/technologies/downloads/#java17)
- [PostgreSQL >= 14](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)
- [RabbitMQ](https://www.rabbitmq.com/)

Tanto PostgreSQL quanto RabbitMQ serão instalados via Docker, portanto, basta ter o Java e o Docker instalados.

---
### Configurações

Para facilitar, optou-se pelo uso de docker. Desta forma, podemos ter formas diferentes de execução, mas em todos os casos precisamos informar as variáveis de ambiente.

Desta forma, criei um arquivo .env no root do projeto. Adicione valores como deseja. 

Para criação da chava de API para o e-mail, siga os passos informado na documentação do projeto.

```
DATABASE_HOST=localhost:7072
DATASOURCE_USERNAME=postgres
DATASOURCE_PASSWORD=root

SPRING_MAIL_USERNAME=coloque_seu_email
SPRING_MAIL_PASSWORD=coloque_a_chave_de_api

PARKING_EMAIL=coloque_seu_email
EMAIL_API_KEY=coloque_a_chave_de_api

RABBIT_USER=guest
RABBIT_PASSWORD=guest
RABBIT_HOST=localhost
RABBIT_PORT=5672
```

Note que algumas chaves que são necessárias no _application.properties_ para executar tudo via Docker, já foram setadas no compose.
O arquivo acima, ilustra o uso da aplicação rodando local e o PostgreSQL e RabbitMQ executando via container.

    Onde possui coloque_a_chave_de_api e coloque_seu_email é necessário atualizar para seu e-mail e sua chave de API criada.

#### Executar localmente

Execute o comando abaixo e pare o container de nome parking-app. Se nada foi alterado no compose, o arquivo .env já estaria configurado para tal.

    docker compose up -d

### Executar através do Docker

Neste, o que iria mudar seria o nome dos host, porém isso já foi configurado para está correto no arquivo compose, então basta executar o comando abaixo.

    docker compose up -d

Lembrando que os comandos foram apresentados como se fossem executados a partir do root do diretório.

---
### Importante
O sistema emitirá e-mails, após a finalização do uso, próximo ao encerramento do período ou adição de período de estacionamento. 

Nossos membros optaram por utilizar o gmail (e-mail do google) para o desenvolvimento. No notion existe o link com a documentação oficial do google de com gerar a senha do app para utilização.

O processo de notificação dar-se a partir de uma fila. Quando próximo a finalização, seja por hora ou período fixo, será enviado um e-mail. 
 * No caso de período por hora, ele não se encerra sozinho, então dez minutos antes de adicionar um novo período, é enviado um e-mail avisando e ao adicionar o perído também. 
 * Já nos casos de período fixo, cinco minutos antes do encerramento é enviado um aviso e ao finalizar, é enviado o e-mail de encerramento.

É uma regra de negócio que período por hora se encerre por ação manual, ou seja, é necessário receber uma requisição para encerramento.

Período fixo também podem ser encerrados manualmente, porém seu valor não será alterado. Em ambos os casos, será enviado um comprovante via e-mail.
