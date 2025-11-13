# Ajuste de Versões - Java e Spring Boot

## 🔧 Problema Identificado

O projeto estava configurado com **Java 11**, mas o **Spring Boot 3.2.0** requer **Java 17** como mínimo.

## ✅ Solução Aplicada

Atualizei o `pom.xml` para usar **Java 17**, que é compatível com Spring Boot 3.2.0.

### Alterações Realizadas:
- `java.version`: 11 → **17**
- `maven.compiler.source`: 11 → **17**
- `maven.compiler.target`: 11 → **17**

## 📋 Compatibilidade de Versões

### Spring Boot 3.2.0
- **Java mínimo requerido**: 17
- **Java recomendado**: 17 ou superior (até 21)
- **Jakarta EE**: Spring Boot 3.x usa Jakarta EE 9+ (não mais javax)

### Spring Boot 2.7.x (alternativa)
- **Java suportado**: 8 a 19
- Se você precisar manter Java 11, seria necessário fazer downgrade do Spring Boot

## 🚀 Próximos Passos

### 1. Verificar Versão do Java Instalada

Execute no terminal:
```bash
java -version
```

Você deve ver algo como:
```
openjdk version "17.0.x" ou superior
```

### 2. Instalar Java 17 (se necessário)

#### Windows:
1. Baixe o JDK 17 da Oracle ou OpenJDK:
   - Oracle: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - OpenJDK: https://adoptium.net/ (recomendado)

2. Instale o JDK 17

3. Configure a variável de ambiente `JAVA_HOME`:
   ```powershell
   # Defina JAVA_HOME apontando para a instalação do JDK 17
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'Machine')
   ```

4. Adicione ao PATH:
   ```powershell
   # Adicione ao PATH
   $env:PATH = "C:\Program Files\Java\jdk-17\bin;$env:PATH"
   [System.Environment]::SetEnvironmentVariable('PATH', $env:PATH, 'Machine')
   ```

#### Linux/Mac:
```bash
# Usando SDKMAN (recomendado)
sdk install java 17.0.9-tem

# Ou usando apt (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-17-jdk
```

### 3. Atualizar Configuração do IDE

#### IntelliJ IDEA:
1. File → Project Structure → Project
2. Defina "SDK" para Java 17
3. Defina "Language level" para 17
4. File → Settings → Build, Execution, Deployment → Compiler → Java Compiler
5. Defina "Project bytecode version" para 17

#### Eclipse:
1. Project → Properties → Java Build Path → Libraries
2. Remova JRE System Library antiga
3. Add Library → JRE System Library → Java 17
4. Project → Properties → Java Compiler
5. Defina "Compiler compliance level" para 17

#### VS Code:
1. Instale a extensão "Extension Pack for Java"
2. Configure `java.configuration.runtimes` no settings.json:
   ```json
   {
     "java.configuration.runtimes": [
       {
         "name": "JavaSE-17",
         "path": "C:\\Program Files\\Java\\jdk-17",
         "default": true
       }
     ]
   }
   ```

### 4. Atualizar Projeto Maven

Execute no terminal:
```bash
# Limpar e atualizar dependências
mvn clean install

# Ou se preferir apenas atualizar
mvn dependency:resolve
```

### 5. Verificar Compilação

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

## 🔄 Alternativa: Downgrade para Spring Boot 2.7.x

Se você **precisar manter Java 11**, você pode fazer downgrade do Spring Boot:

### Alterações necessárias no pom.xml:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>  <!-- Última versão 2.7.x -->
    <relativePath/>
</parent>

<properties>
    <java.version>11</java.version>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```

### ⚠️ Atenção: Mudanças Necessárias

Se fizer downgrade para Spring Boot 2.7.x, você precisará:

1. **Alterar imports**:
   - `jakarta.*` → `javax.*`
   - Exemplo: `jakarta.persistence.*` → `javax.persistence.*`

2. **Alterar dependências**:
   - MySQL: `mysql-connector-j` → `mysql-connector-java` (versão 8.x)

3. **Alterar configurações**:
   - `jakarta.validation.*` → `javax.validation.*`

## ✅ Recomendação

**Recomendo manter Spring Boot 3.2.0 com Java 17** porque:
- ✅ Java 17 é LTS (Long Term Support)
- ✅ Spring Boot 3.x tem melhorias significativas
- ✅ Melhor suporte a recursos modernos do Java
- ✅ Melhor performance
- ✅ Suporte a Jakarta EE 9+

## 📚 Referências

- [Spring Boot 3.2.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.2-Release-Notes)
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Spring Boot Upgrade Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Upgrade-Guide)

## 🐛 Resolução de Problemas

### Erro: "Unsupported class file major version"
- **Causa**: Java instalado é inferior à versão 17
- **Solução**: Instale Java 17 ou superior

### Erro: "Package jakarta.persistence does not exist"
- **Causa**: Projeto compilado com Spring Boot 2.x mas usando imports jakarta
- **Solução**: Certifique-se de usar Spring Boot 3.x ou altere imports para javax

### Erro: "JAVA_HOME not set"
- **Causa**: Variável de ambiente JAVA_HOME não configurada
- **Solução**: Configure JAVA_HOME apontando para JDK 17


