# 🚀 Solução Rápida - Erro Java 25

## ❌ Erro
```
Unsupported class file major version 69
Starting BiDeskApplication using Java 25.0.1
```

## ✅ Solução em 3 Passos

### Passo 1: Limpar Projeto
```powershell
mvn clean
```

### Passo 2: Verificar/Configurar Java 17

Execute o script de verificação:
```powershell
.\verificar-java.ps1
```

Ou configure manualmente:

#### Se Java 17 estiver instalado:
```powershell
# Encontrar o caminho do Java 17 (ajuste conforme necessário)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verificar
java -version
```

#### Se Java 17 NÃO estiver instalado:
1. Baixe Java 17 de: https://adoptium.net/temurin/releases/?version=17
2. Instale o JDK 17
3. Configure JAVA_HOME:
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

### Passo 3: Recompilar e Executar
```powershell
# Recompilar
mvn clean compile

# Executar
mvn spring-boot:run
```

## 🔧 Configuração Permanente (Opcional)

Para configurar Java 17 permanentemente no Windows:

```powershell
# Configurar JAVA_HOME permanentemente
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'User')

# Adicionar ao PATH permanentemente
$currentPath = [System.Environment]::GetEnvironmentVariable('PATH', 'User')
$newPath = "$env:JAVA_HOME\bin;$currentPath"
[System.Environment]::SetEnvironmentVariable('PATH', $newPath, 'User')

# Reiniciar o terminal e verificar
java -version
```

## 📝 Verificação Rápida

Execute estes comandos para verificar:

```powershell
# 1. Verificar versão do Java
java -version
# Deve mostrar: openjdk version "17.x.x"

# 2. Verificar JAVA_HOME
echo $env:JAVA_HOME
# Deve mostrar o caminho para Java 17

# 3. Limpar e compilar
mvn clean compile
# Deve compilar sem erros

# 4. Executar
mvn spring-boot:run
# Deve iniciar a aplicação
```

## 🎯 Versões Compatíveis

| Componente | Versão Requerida | Versão Máxima |
|------------|------------------|---------------|
| Java       | 17               | 21            |
| Spring Boot| 3.2.0            | 3.2.x         |

**Java 25 NÃO é compatível com Spring Boot 3.2.0**

## ⚠️ Importante

1. **Sempre execute `mvn clean` antes de recompilar** para remover classes antigas
2. **Verifique a versão do Java** antes de compilar: `java -version`
3. **Configure JAVA_HOME** para apontar para Java 17
4. **Reinicie o terminal** após configurar variáveis de ambiente

## 🐛 Problemas Comuns

### "java: command not found"
- Java não está no PATH
- Configure: `$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"`

### "Unsupported class file major version 69"
- Projeto foi compilado com Java 25
- Execute: `mvn clean` e recompile com Java 17

### "JAVA_HOME not set"
- Configure JAVA_HOME: `$env:JAVA_HOME = "C:\caminho\para\java17"`

## 📚 Mais Informações

Consulte `RESOLUCAO_ERRO_JAVA25.md` para instruções detalhadas.


