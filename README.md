## 1.日志

在 Java 应用程序中，尤其是使用 Spring Boot 的项目里，日志可以在多个层级使用，包括 Service 层。使用日志的层级取决于你需要记录什么类型的信息以及这些信息的重要性。下面是一些常见的层级和在这些层级中使用日志的情况：

### 1. **Service 层**

在 Service 层使用日志是非常常见的做法，因为这一层通常包含业务逻辑的核心部分。在 Service 层记录日志可以帮助开发者了解业务流程的执行情况和状态。例如，你可能想要记录：

- 方法的调用和返回值。
- 重要的业务决策点。
- 警告和可能的错误处理。
- 外部服务调用的状态。

### 2. **Controller 层**

在 Controller 层添加日志可以帮助你跟踪客户端的请求和响应。通过记录这些信息，你可以了解哪些 API 被频繁调用，以及它们的响应时间和可能出现的错误。例如，可以记录：

- 请求的入口点。
- 请求参数。
- 响应状态码。
- 异常或错误信息。

### 3. **Repository/Data Access Layer**

在数据访问层（Repository）记录日志可以帮助你监控数据库操作，比如 CRUD 操作的执行情况和执行时间。在这一层记录日志可以用来：

- 跟踪数据库查询和结果。
- 监控数据库操作性能。
- 记录可能的数据库错误或异常。

### 4. **Component/Utility Classes**

如果你有一些工具类或者组件类，那么在这些类中添加日志同样很有用。这可以帮助你理解这些组件的使用情况和潜在的问题。比如，你可能会记录：

- 关键功能的使用。
- 重要计算的结果。
- 外部服务或API的调用情况。

## 2.测试Testing with boot testing framework

- **单元测试（Unit Testing）**:
  - 使用 `@MockBean` 来模拟服务层或存储层的依赖。
  - 通常不加载任何 Spring 应用上下文或只加载非常有限的上下文。
  - 快速且专注于一个非常具体的类或方法。
- **Web层测试（Web Layer Testing）**:
  - 使用 `@WebMvcTest` 仅加载 MVC 层，适合测试控制器而不启动完整的 HTTP 服务器。
  - 集成了 `MockMvc`，可以模拟 HTTP 请求与响应，检查控制器的行为。
- **服务层测试（Service Layer Testing）**:
  - 使用 `@SpringBootTest` 结合 `@MockBean` 来测试服务层的业务逻辑。
  - 可以加载完整的应用程序上下文或部分上下文。
  - 用于测试服务层的方法逻辑，确保它们在给定的输入下正确执行。
- **集成测试（Integration Testing）**:
  - 使用 `@SpringBootTest` 加载完整的应用程序上下文，通常结合使用 `@AutoConfigureMockMvc` 或 `@TestRestTemplate`。
  - 用于测试应用程序的多个组件是如何一起工作的，例如控制器、服务和数据访问层。
  - 可以进行全面的端到端测试，有时包括模拟外部服务。
- **数据访问层测试（Repository Testing）**:
  - 使用 `@DataJpaTest` 专门用于测试 JPA 仓库。此注解配置嵌入式数据库，开启 JPA 仓库，并关闭全应用上下文加载。
  - 通过这种方式可以专注于数据访问逻辑，确认 SQL 查询的正确性及数据的正确处理。
- **切面测试（Aspect Testing）**:
  - 使用 `@SpringBootTest` 或其他上下文加载方式，确保切面逻辑（如事务处理、日志等）正确应用于指定组件。
- **端到端测试（End-to-End Testing）**:
  - 使用 `@SpringBootTest` 结合 `@AutoConfigureMockMvc` 或 `WebTestClient`。
  - 模拟用户完整操作流程，从前端到后端，测试整个系统的行为以及各个部分的集成。
- **切片测试（Slice Tests）**:
  - 专门针对应用的某一层或组件进行隔离测试。
  - 如 `@JsonTest`、`@WebFluxTest`、`@WebMvcTest` 等，这些测试针对特定技术或层次结构进行优化。