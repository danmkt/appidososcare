# Cuidado Idosos

## Visão geral
Este é um **aplicativo multiplataforma** (Android + Desktop JVM) desenvolvido com **Kotlin Compose Multiplatform**. Ele ajuda cuidadores e idosos a monitorar:
- Medicação
- Ingestão de água
- Alimentação

O foco está na **acessibilidade**: cores de alto contraste, tipografia legível e componentes com tamanho adequado para dedos mais grossos.

## Requisitos
- JDK 17+ 
- Android SDK (para compilação Android) 
- Gradle Wrapper (já incluído)

## Como rodar (Desktop)
```bash
./gradlew run
```

## Como rodar (Android)
1. Conecte um dispositivo ou inicie um emulador.
2. Execute:
```bash
./gradlew assembleDebug
adb install -r app-cuidado-idosos/build/outputs/apk/debug/app-cuidado-idosos-debug.apk
```

## Testes
```bash
./gradlew test
```

## Contribuição
1. Fork o repositório
2. Crie uma branch `feature/nomedafuncionalidade`
3. Envie pull request

## Licença
Este projeto está licenciado sob a MIT License.