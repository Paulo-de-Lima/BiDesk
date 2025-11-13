# Resolução do Erro: Java 25 com Spring Boot 3.2.0

## 🐛 Problema Identificado

```
Unsupported class file major version 69
Starting BiDeskApplication using Java 25.0.1
```

O projeto foi compilado com **Java 25**, mas o **Spring Boot 3.2.0** suporta apenas até **Java 21**.

## ✅ Solução Aplicada

1. **Adicionada configuração explícita do Maven Compiler Plugin** para forçar Java 17
2. **Configurado release=17** para garantir compatibilidade

## 🔧 Alterações no pom.xml

Adicionado plugin Maven Compiler com configuração explícita:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <release>17</release>
        <encoding>UTF-8</encoding>
    </configuration>
</plugin>
```

## 🚀 Próximos Passos (IMPORTANTE)

### 1. Limpar o Diretório target (Classes Compiladas com Java 25)

Execute no terminal:
```bash
# Limpar todas as classes compiladas
mvn clean

# Ou delete manualmente a pasta target
rm -rf target
# Windows PowerShell:
Remove-Item -Recurse -Force target
```

### 2. Verificar Versão do Java no Terminal

```bash
java -version
javac -version
```

Se mostrar Java 25, você precisa:
- Configurar JAVA_HOME para Java 17
- Ou usar uma versão compatível (17, 19, 21)

### 3. Configurar JAVA_HOME para Java 17

#### Windows PowerShell:
```powershell
# Verificar Java instalado
Get-ChildItem "C:\Program Files\Java"

# Configurar JAVA_HOME (ajuste o caminho conforme sua instalação)
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'User')

# Adicionar ao PATH
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
[System.Environment]::SetEnvironmentVariable('PATH', $env:PATH, 'User')

# Verificar
$env:JAVA_HOME
java -version
```

#### Linux/Mac:
```bash
# Exportar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Verificar
echo $JAVA_HOME
java -version
```

### 4. Recompilar o Projeto

```bash
# Limpar e recompilar
mvn clean compile

# Ou fazer build completo
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### 5. Configurar o IDE

#### IntelliJ IDEA:
1. **File → Project Structure → Project**
   - Project SDK: Java 17
   - Project language level: 17

2. **File → Settings → Build, Execution, Deployment → Compiler → Java Compiler**
   - Project bytecode version: 17
   - Per-module bytecode version: 17

3. **File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner**
   - JRE: Use Project JDK (17)

4. **Limpar Cache:**
   - File → Invalidate Caches / Restart

#### Eclipse:
1. **Project → Properties → Java Build Path → Libraries**
   - Remover JRE System Library
   - Add Library → JRE System Library → Java 17

2. **Project → Properties → Java Compiler**
   - Compiler compliance level: 17

3. **Project → Clean → Clean all projects**

#### VS Code:
1. Configurar `settings.json`:
```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17",
      "default": true
    }
  ],
  "java.compile.nullAnalysis.mode": "automatic"
}
```

2. Reload Window: Ctrl+Shift+P → "Java: Reload Projects"

## 📋 Compatibilidade de Versões

| Spring Boot | Java Mínimo | Java Máximo Suportado |
|-------------|-------------|----------------------|
| 3.2.0       | 17          | 21                   |
| 3.1.x       | 17          | 21                   |
| 3.0.x       | 17          | 19                   |
| 2.7.x       | 8           | 19                   |

**Java 25 NÃO é suportado pelo Spring Boot 3.2.0**

## 🔍 Verificar Class File Version

Para verificar a versão do class file compilado:
```bash
# Linux/Mac
javap -verbose target/classes/com/bidesk/BiDeskApplication.class | grep "major version"

# Windows (PowerShell)
javap -verbose target\classes\com\bidesk\BiDeskApplication.class | Select-String "major version"
```

Versões esperadas:
- Java 17 → major version 61
- Java 21 → major version 65
- Java 25 → major version 69 (NÃO SUPORTADO)

## ✅ Solução Rápida

Execute estes comandos na ordem:

```bash
# 1. Limpar projeto
mvn clean

# 2. Verificar Java
java -version  # Deve ser 17, 19 ou 21

# 3. Recompilar
mvn clean compile

# 4. Executar
mvn spring-boot:run
```

## 🎯 Recomendação Final

**Use Java 17 LTS** porque:
- ✅ Suportado pelo Spring Boot 3.2.0
- ✅ Versão LTS (Long Term Support)
- ✅ Estável e amplamente testado
- ✅ Compatível com todas as dependências

## 🐛 Se o Problema Persistir

1. **Verifique múltiplas instalações de Java:**
   ```bash
   # Windows
   where java
   
   # Linux/Mac
   which java
   ```

2. **Force o Maven a usar Java 17:**
   ```bash
   # Definir JAVA_HOME antes de executar Maven
   export JAVA_HOME=/caminho/para/java17
   mvn clean install
   ```

3. **Use Maven Wrapper com Java específico:**
   ```bash
   # Configurar JAVA_HOME no mvnw
   JAVA_HOME=/caminho/para/java17 ./mvnw clean install
   ```


