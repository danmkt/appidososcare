# app-cuidado-idosos

Este é um aplicativo multiplataforma em desenvolvimento para auxiliar no controle de medicamentos, alimentação e ingestão de água para pessoas idosas.

## Status Atual (Protótipo Funcional)

O projeto agora possui uma interface de usuário funcional para o gerenciamento de medicamentos. As seguintes funcionalidades foram implementadas:

*   **Listagem de Medicamentos:** A tela principal exibe uma lista de todos os medicamentos cadastrados.
*   **Adicionar Medicamento:** Um formulário permite adicionar novos medicamentos, especificando nome, dosagem e frequência.
*   **Remover Medicamento:** É possível remover um medicamento da lista.
*   **Persistência de Dados:** As informações são salvas em um banco de dados SQLite local, garantindo que os dados não sejam perdidos ao fechar o aplicativo.

## Tecnologias

*   **Framework UI:** Compose Multiplatform (Kotlin)
*   **Linguagem:** Kotlin
*   **Banco de Dados:** SQLite (com SQLDelight para acesso type-safe)

## Como Compilar e Executar

Para compilar e executar o projeto, siga os passos abaixo:

### 1. Pré-requisitos

*   **Java Development Kit (JDK):** É necessário ter o JDK (versão 17 ou superior) instalado e configurado no seu sistema.
*   **Android SDK:** Para compilar a versão Android do aplicativo, você precisa do Android SDK.

### 2. Configuração do Android SDK

O Gradle precisa saber onde o seu Android SDK está localizado. Para isso:
1.  No diretório raiz do projeto, você encontrará um arquivo chamado `local.properties`.
2.  Abra este arquivo e substitua a linha `sdk.dir=/path/to/your/android/sdk` pelo caminho completo para o diretório do seu Android SDK.

### 3. Compilando o Projeto

Com o JDK e o Android SDK configurados, você pode compilar o projeto. Abra um terminal na raiz do projeto e execute o comando:

```bash
./gradlew build
```
Este comando irá baixar as dependências necessárias, gerar o código do SQLDelight e compilar as versões para Android e Desktop.

### 4. Executando o Aplicativo

*   **Desktop:** Para executar a versão para Desktop, utilize o comando:
    ```bash
    ./gradlew run
    ```
*   **Android:** Para instalar e executar em um dispositivo ou emulador Android, utilize:
    ```bash
    ./gradlew installDebug
    ```
    (Este comando instala o app, você precisará abri-lo manualmente no dispositivo).