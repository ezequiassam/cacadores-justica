# Cacadores da Justica
[Caçadores da Justiça](/#) tem finalidade de tornar os dados públicos de processos de primeiro grau dos Tribunais de Justiça de São Paulo (TJSP) e do Mato Grosso do Sul (TJMS), mais fáceis de serem encontrados.

## Instalação
Linux & Windows
- Mova o arquivo DockerFile para o diretório docker e execute o build
```sh
docker build --rm=true -t cacadores/justica:1.0 .
```
- Inicie o container configurando a porta do host local
```sh
docker run -i -t -p 8080:8080 cacadores/justica:1.0
```
- Digite em um navegador o seguinte caminho: 
```sh
http://localhost:8080/
```
## Configuração
- JDK 8 ou superior
- Docker (testado na versão 17)

## Meta
Ezequias Sampaio – ezequiassam@gmail.com