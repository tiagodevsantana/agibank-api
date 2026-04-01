# Dog API – QA Automation

Projeto de testes automatizados REST para a [Dog CEO API](https://dog.ceo/api) como parte do processo seletivo QA – Agibank.

## Stack

| Tecnologia | Versão |
|---|---|
| Java | 11 (target) / 20 (local) |
| REST Assured | 5.3.2 |
| JUnit Jupiter | 5.10.1 |
| Allure Report | 2.25.0 |
| Maven Wrapper | 3.9.6 |

## Pré-requisitos

- JDK 11+ instalado e `JAVA_HOME` configurado
- Acesso à internet (testes fazem chamadas reais à API)

## Como executar

bash
# Clonar o repositório
git clone https://github.com/tiagodevsantana/DogApi.git
cd DogApi

# Linux/macOS
chmod +x mvnw
./mvnw test

# Windows
mvnw.cmd test


## Gerar e abrir relatório Allure

bash
# Gerar HTML
./mvnw allure:report

# Abrir no navegador (Windows)
start target/site/allure-maven-plugin/index.html

## Cenários de teste

### BreedsListAll – `GET /breeds/list/all`

| ID | Descrição |
|---|---|
| CT01 | Status HTTP 200 |
| CT02 | Campo `status` igual a `"success"` |
| CT03 | Content-Type é `application/json` |
| CT04 | Campo `message` não está vazio |
| CT05 | Raças conhecidas (`labrador`, `husky`, `poodle`) presentes |
| CT06 | Lista contém mais de 50 raças |

### BreedImages – `GET /breed/{breed}/images`

| ID | Descrição |
|---|---|
| CT07 | Raça válida retorna HTTP 200 |
| CT08 | Campo `status` é `"success"` para raça válida |
| CT09 | Campo `message` é lista não vazia |
| CT10 | URLs das imagens têm extensão `.jpg`/`.jpeg`/`.png` |
| CT11 | Parametrizado: 5 raças (`husky`, `poodle`, `beagle`, `boxer`, `dalmatian`) |
| CT12 | Raça inválida retorna HTTP 404 |
| CT13 | Raça inválida retorna `status` = `"error"` |

### RandomImage – `GET /breeds/image/random`

| ID | Descrição |
|---|---|
| CT14 | Status HTTP 200 |
| CT15 | Campo `status` é `"success"` |
| CT16 | Campo `message` é URL válida (regex) |
| CT17 | URL pertence ao domínio `dog.ceo` |
| CT18 | `@RepeatedTest(3)` – 3 chamadas consecutivas retornam URLs válidas |

**Total: 18 execuções** (13 testes + 5 parametrizados de CT11 + 3 repetições de CT18 = 21 execuções no relatório do Surefire, mas 18 cenários distintos)

## CI/CD

O pipeline GitHub Actions (`/.github/workflows/ci.yml`) executa:

1. Matrix com Java 11, 17 e 21
2. Cache do repositório Maven
3. `./mvnw test`
4. Geração do relatório Allure
5. Upload dos artefatos (`allure-results`, `allure-report`)
6. Summary no PR via `dorny/test-reporter`

## Estrutura do projeto

```
DogApi/
├── .github/
│   └── workflows/
│       └── ci.yml
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   └── test/
│       └── java/
│           └── br/com/dogapi/
│               ├── base/
│               │   └── BaseTest.java
│               └── tests/
│                   ├── BreedsListAllTest.java
│                   ├── BreedImagesTest.java
│                   └── RandomImageTest.java
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```
